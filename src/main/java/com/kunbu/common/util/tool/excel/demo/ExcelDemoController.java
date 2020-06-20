package com.kunbu.common.util.tool.excel.demo;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.IOUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.kunbu.common.util.tool.excel.ExcelExportUtil;
import com.kunbu.common.util.tool.excel.ExcelReadUtil;
import com.kunbu.common.util.tool.excel.common.*;
import com.kunbu.common.util.tool.file.FileUtil;
import com.kunbu.common.util.web.ApiResult;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * https://www.cnblogs.com/liyafei/p/8146136.html
 *
 * VM options: -Xms100m -Xmx100m
 *
 * 测试简单的excel读取,即用户模式user model
 * 测试大数据情况下的问题,OOM,GC
 *
 *
 * @author: kunbu
 * @create: 2019-11-19 13:37
 **/
@RestController
@RequestMapping("/excel")
public class ExcelDemoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelDemoController.class);

    /** 导出 */
    private static List<String> exportHeaders =
            Lists.newArrayList("序号", "小区名称", "审核状态", "用户类型", "用户名称", "性别", "身份证号", "手机号", "备注");
    private static List<String> exportKeys =
            Lists.newArrayList("number", "orgName", "auditState", "userType", "userName", "sex", "idCardNum", "userPhone", "userRemark");
    /** 导入（模板） */
    private static List<String> importHeaders =
            Lists.newArrayList("用户类型(业主，租户)", "用户姓名", "性别(男，女)", "身份证号", "手机号", "备注");

    private static final String EXCEL_TEMPLATE_FILENAME = "excel模板(单元格必须为文本)";

    @Autowired
    private ExcelService excelService;

    /**
     * 通过实时生成模板的方式下载
     *
     * @param request
     * @param response
     * @author kunbu
     * @time 2020/2/25 9:34
     * @return
     **/
    @GetMapping("/export/template")
    @ResponseBody
    public void exportExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ExcelExportUtil.exportExcelTemplate(importHeaders, baos);
        byte[] content = baos.toByteArray();
        FileUtil.download(request, response, content, EXCEL_TEMPLATE_FILENAME + "." + ExcelConst.EXCEL_XLSX_2007);
    }

    /**
     * 直接下载保存好的模板文件
     *
     * @param request
     * @param response
     * @author kunbu
     * @time 2020/2/25 9:35
     * @return
     **/
    @GetMapping("/export/template2")
    @ResponseBody
    public void exportExcelTemplate2(HttpServletRequest request, HttpServletResponse response) {
        String tempFileName = ExcelConst.EXCEL_TEMPLATE_FILE_NAME;
        try {
            // 获取静态资源文件路径
            String tempPath =  ResourceUtils.getURL(ResourceUtils.CLASSPATH_URL_PREFIX).getPath() + "static" + File.separator + tempFileName;
//        String tempPath = request.getSession().getServletContext().getRealPath("/") + "static" + File.separator + tempFileName;

            // /D:/kunbu/tool/target/classes/static\订单导入模板.xlsx
            LOGGER.info(">>> path: " + tempPath);
            FileUtil.download(request, response, tempPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/export/data")
    @ResponseBody
    public void exportExcelData(HttpServletRequest request, HttpServletResponse response) {
        long start = System.currentTimeMillis();
        try {
            // TODO 模拟获取业务数据
            List<ExcelEntity> beanList = Lists.newArrayList();
            Random random = new Random();
            // 测试大数据下OOM
            for (int i = 0; i < 1000_000; i++) {
                ExcelEntity eb = new ExcelEntity(
                        random.nextInt(10000) + "小区",
                        random.nextBoolean() ? "待审核" : "审核通过",
                        random.nextBoolean() ? "业主" : "租户",
                        random.nextInt(10000) + "号用户",
                        "3302" + random.nextInt(10000),
                        random.nextBoolean() ? "男" : "女",
                        "1" + random.nextInt(10000),
                        "这是备注的" + i,
                        "",
                        "");
                beanList.add(eb);
            }
            LOGGER.info(">>> data from db:{}", System.currentTimeMillis() - start);

            // 将实体类转换成数据集合
            List<Map<String, Object>> dataList = Lists.newArrayList();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            if (true) {
                // TODO 动态选择导出字段
                List<String> showFieldList = Lists.newArrayList("orgName", "userName", "idCardNum", "userPhone", "auditState");
                List<String> headerList = Lists.newArrayList();

                start = System.currentTimeMillis();
                Map<String, String> field2MethodMap = getField2Method(ExcelEntity.class, showFieldList, headerList);
                LOGGER.info(">>> get field:{}", System.currentTimeMillis() - start);

                start = System.currentTimeMillis();
                List<Map<String, Object>> dataMap = getExcelData(beanList, field2MethodMap);
                LOGGER.info(">>> get data:{}", System.currentTimeMillis() - start);

                start = System.currentTimeMillis();
                ExcelExportUtil.exportExcelSimpleBigData(headerList, showFieldList, dataMap, baos);
                LOGGER.info(">>> export excel:{}", System.currentTimeMillis() - start);
            } else {
                // TODO 固定字段的填充数据
                start = System.currentTimeMillis();
                // 手动增加序号
                int number = 1;
                for (ExcelEntity eb : beanList) {
                    Map<String, Object> dataMap = Maps.newHashMap();
                    dataMap.put(exportKeys.get(0), number++);
                    dataMap.put(exportKeys.get(1), eb.getOrgName());
                    dataMap.put(exportKeys.get(2), eb.getAuditState());
                    dataMap.put(exportKeys.get(3), eb.getUserType());
                    dataMap.put(exportKeys.get(4), eb.getUserName());
                    dataMap.put(exportKeys.get(5), eb.getSex());
                    dataMap.put(exportKeys.get(6), eb.getIdCardNum());
                    dataMap.put(exportKeys.get(7), eb.getUserPhone());
                    dataMap.put(exportKeys.get(8), eb.getUserRemark());
                    dataList.add(dataMap);
                }
                LOGGER.info(">>> excel:{}", System.currentTimeMillis() - start);

                start = System.currentTimeMillis();
                // 导出2007格式
///            ExcelExportUtil.exportExcelSimple2007(exportHeaders, exportKeys, dataList, baos);
                // 测试大数据下导出（速度更快）
                ExcelExportUtil.exportExcelSimpleBigData(exportHeaders, exportKeys, dataList, baos);
                LOGGER.info(">>> excel:{}", System.currentTimeMillis() - start);
            }

            byte[] content = baos.toByteArray();
            String fileName = ExcelConst.EXCEL_EXPORT_FILE_NAME_PREFIX + System.currentTimeMillis();
            FileUtil.download(request, response, content, fileName + "." + ExcelConst.EXCEL_XLSX_2007);
        } catch (Exception e) {
            LOGGER.error(">>> exportExcelData fail", e);
        }
    }

    private Map<String, String> getField2Method(Class clazz, List<String> showFields, List<String> headers) {
        Map<String, String> field2MethodMap = Maps.newHashMap();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            LOGGER.info(">>> fieldName:{}", fieldName);
            if (showFields.contains(fieldName)) {
                ExcelAnnotation annotation = field.getAnnotation(ExcelAnnotation.class);
                if (annotation != null) {
                    String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    field2MethodMap.put(fieldName, getMethodName);
                    LOGGER.info(">>> tile:{}", annotation.title());
                    headers.add(fieldName);
                }
            }
        }
        return field2MethodMap;
    }

    private List<Map<String, Object>> getExcelData(List<ExcelEntity> all, Map<String, String> field2MethodMap) throws Exception {
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (ExcelEntity entity : all) {
            Map<String, Object> deviceData = new HashMap<>();
            Class clazz = entity.getClass();
            for (Map.Entry<String, String> entry : field2MethodMap.entrySet()) {
                Method method = clazz.getMethod(entry.getValue());
                deviceData.put(entry.getKey(), method.invoke(entity));
            }
            dataList.add(deviceData);
        }
        return dataList;
    }

    
    private static final List<String> headers = Lists.newArrayList("序号", "订单编号", "资产编号", "产品条码", "RFID",
            "模块IMEI号", "SIM卡号", "模块IMSI号", "WiFi设备号", "蓝牙设备号", "品牌", "型号", "发货日期");

    /**
     * 导入excel（按照模板）
     *
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/import/data")
    @ResponseBody
    public ApiResult importExcelData(HttpServletRequest request, @RequestParam MultipartFile file)  {
        // TODO 业务判断，比如导入者必须是小区管理员，只能导入该小区下用户，因为小区id从session或token中拿到
        String orgId = request.getHeader("orgId");
        if (StringUtils.isBlank(orgId)) {
            LOGGER.error(">>> importExcelData orgId null");
            return ApiResult.fail("请用物业管理员，或小区管理员的身份进行用户导入");
        }

        InputStream input = null;
        List<List<String>> dataList = null;
        try {
            input = file.getInputStream();
            dataList = ExcelReadUtil.readExcelSimpleWithHeader(input, file.getOriginalFilename(), headers);
        } catch (Exception e) {
            LOGGER.error(">>> read excel error", e);
        } finally {
            IOUtils.close(input);
        }

        JSONObject errMsg = new JSONObject();
        // 转化成实体类，并检查校验
        List<ExcelEntity> importDeviceList = convertExcelEntity(dataList, errMsg);
        if (errMsg.size() > 0) {
            return ApiResult.success(errMsg);
        }
        if (CollectionUtils.isEmpty(importDeviceList)) {
            return ApiResult.fail("导入数据为空");
        }

        // TODO 业务操作
        
        return ApiResult.success();
    }

    private List<ExcelEntity> convertExcelEntity(List<List<String>> dataList, JSONObject errMsg) {
        // 用于检查和获取小区id
        Set<String> orgNameSet = Sets.newHashSet();
        // 用于DB中检查唯一性
        Map<String, String> idCardNum2IndexMap = Maps.newHashMap();
        Map<String, String> userPhone2IndexMap = Maps.newHashMap();

        List<ExcelEntity> excelEntityList = Lists.newArrayList();
        for (List<String> data : dataList) {
            LOGGER.info(">>> excel import data:{}", data);
            if (CollectionUtils.isEmpty(data)) {
                break;
            }
            // 按照第一列序号开始检查
            String index = data.get(0);
            if (StringUtils.isBlank(index) || !NumberUtils.isCreatable(index)) {
                continue;
            }
            ExcelEntity entity = new ExcelEntity();
            entity.setIndex(index);
            // 小区名称
            String orgName = data.get(1);
            if (StringUtils.isBlank(orgName)) {
                errMsg.put(index, "小区名称不可为空");
                break;
            }
            orgNameSet.add(orgName);
            entity.setOrgName(orgName);
            // 审核状态
            String auditState = data.get(2);
            if (StringUtils.isBlank(auditState)) {
                errMsg.put(index, "审核状态不可为空");
                break;
            }
            UserAuditStateEnum stateEnum = UserAuditStateEnum.getByValue(auditState);
            if (stateEnum == null) {
                errMsg.put(index, "审核状态填写有误");
                break;
            }
            entity.setAuditState(stateEnum.name());
            // 用户类型
            String userType = data.get(3);
            if (StringUtils.isBlank(userType)) {
                errMsg.put(index, "用户类型不可为空");
                break;
            }
            UserTypeEnum userEnum = UserTypeEnum.getByValue(userType);
            if (userEnum == null) {
                errMsg.put(index, "用户类型填写有误");
                break;
            }
            entity.setUserType(userEnum.name());
            // 用户姓名
            String userName = data.get(4);
            if (StringUtils.isBlank(userName)) {
                errMsg.put(index, "用户姓名不可为空");
                break;
            }
            entity.setUserName(userName);
            // 身份证号
            String idCardNum = data.get(5);
            if (StringUtils.isBlank(idCardNum)) {
                errMsg.put(index, "身份证号不可为空");
                break;
            }
            if (idCardNum2IndexMap.containsKey(idCardNum)) {
                errMsg.put(index, "身份证号【" + idCardNum + "】重复导入");
                break;
            }
            idCardNum2IndexMap.put(idCardNum, index);
            entity.setIdCardNum(idCardNum);
            // 性别
            String sex = data.get(6);
            if (StringUtils.isBlank(sex)) {
                errMsg.put(index, "性别不可为空");
                break;
            }
            SexEnum sexEnum = SexEnum.getByValue(sex);
            if (sexEnum == null) {
                errMsg.put(index, "性别填写有误");
                break;
            }
            entity.setSex(sexEnum.name());
            // 手机号
            String userPhone = data.get(7);
            if (StringUtils.isBlank(userPhone)) {
                errMsg.put(index, "手机号不可为空");
                break;
            }
            if (userPhone2IndexMap.containsKey(userPhone)) {
                errMsg.put(index, "手机号【" + userPhone + "】重复导入");
                break;
            }
            userPhone2IndexMap.put(userName, index);
            entity.setUserPhone(userPhone);

            excelEntityList.add(entity);
        }
        if (errMsg.size() > 0) {
            return excelEntityList;
        }
        if (CollectionUtils.isEmpty(excelEntityList)) {
            return excelEntityList;
        }

        if (!idCardNum2IndexMap.isEmpty()) {
            List<ExcelEntity> checkIdCardNum = excelService.getByKeyAndValue("idCardNum", Lists.newArrayList(idCardNum2IndexMap.keySet()));
            if (CollectionUtils.isNotEmpty(checkIdCardNum)) {
                for (ExcelEntity e : checkIdCardNum) {
                    errMsg.put(idCardNum2IndexMap.get(e.getIdCardNum()), "身份证号【" + e.getIdCardNum() + "】已存在");
                    break;
                }
            }
        }
        if (!userPhone2IndexMap.isEmpty()) {
            List<ExcelEntity> checkUserPhone = excelService.getByKeyAndValue("userPhone", Lists.newArrayList(userPhone2IndexMap.keySet()));
            if (CollectionUtils.isNotEmpty(checkUserPhone)) {
                for (ExcelEntity e : checkUserPhone) {
                    errMsg.put(userPhone2IndexMap.get(e.getUserPhone()), "手机号【" + e.getUserPhone() + "】已存在");
                    break;
                }
            }
        }

        Map<String, String> orgName2IdMap = excelService.getOrgName2IdMap(Lists.newArrayList(orgNameSet));
        LOGGER.info(">>> orgName2IdMap:{}", orgName2IdMap);
        for (ExcelEntity e : excelEntityList) {
            String orgId = orgName2IdMap.get(e.getOrgName());
            if (StringUtils.isNotBlank(orgId)) {
                e.setOrgId(orgId);
            } else {
                errMsg.put(e.getIndex(), "小区名称【" + e.getOrgName() + "】不存在");
                break;
            }
        }

        return excelEntityList;
    }
}