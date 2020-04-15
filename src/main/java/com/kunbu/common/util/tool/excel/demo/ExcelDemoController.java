package com.kunbu.common.util.tool.excel.demo;

import com.alibaba.fastjson.util.IOUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kunbu.common.util.ResultMap;
import com.kunbu.common.util.tool.file.FileUtil;
import com.kunbu.common.util.tool.excel.ExcelConst;
import com.kunbu.common.util.tool.excel.ExcelExportUtil;
import com.kunbu.common.util.tool.excel.ExcelReadUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
        FileUtil.downloadFile(request, response, content, EXCEL_TEMPLATE_FILENAME + "." + ExcelConst.EXCEL_XLSX_2007);
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
            FileUtil.downloadFile(request, response, tempPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/export/data")
    @ResponseBody
    public void exportExcelData(HttpServletRequest request, HttpServletResponse response) {
        try {
            // TODO 模拟获取业务数据
            List<ExcelEntity> beanList = Lists.newArrayList();
            Random random = new Random();
            // 测试大数据下OOM
            for (int i = 1; i < 1000_0; i++) {
                ExcelEntity eb = new ExcelEntity(
                        random.nextInt(10000) + "小区",
                        random.nextBoolean() ? "待审核" : "审核通过",
                        random.nextBoolean() ? "业主" : "租户",
                        random.nextInt(10000) + "号用户",
                        "3302" + random.nextInt(10000),
                        random.nextBoolean() ? "男" : "女",
                        "1" + random.nextInt(10000),
                        null);
                beanList.add(eb);
            }
            
            // 将实体类转换成数据集合
            List<Map<String, Object>> dataList = Lists.newArrayList();
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
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // 导出2007格式
///            ExcelExportUtil.exportExcelSimple2007(exportHeaders, exportKeys, dataList, baos);
            // 测试大数据下导出（速度更快）
            ExcelExportUtil.exportExcelSimpleBigData(exportHeaders, exportKeys, dataList, baos);

            byte[] content = baos.toByteArray();
            String fileName = ExcelConst.EXCEL_EXPORT_FILE_NAME_PREFIX + System.currentTimeMillis();
            FileUtil.downloadFile(request, response, content, fileName + "." + ExcelConst.EXCEL_XLSX_2007);
        } catch (Exception e) {
            LOGGER.error(">>> exportExcelData error", e);
        }
    }

    /**
     * 导入excel（按照模板）
     *
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("/import/data")
    @ResponseBody
    public ResultMap importExcelData(@RequestParam MultipartFile file, HttpServletRequest request) {
        // TODO 业务判断，比如导入者必须是小区管理员，只能导入该小区下用户，因为小区id从session或token中拿到
        String orgId = request.getHeader("orgId");
        if (StringUtils.isBlank(orgId)) {
            LOGGER.error(">>> importExcelData orgId null");
            return ResultMap.error("请用物业管理员，或小区管理员的身份进行用户导入");
        }

        InputStream input = null;
        try {
            input = file.getInputStream();
            // 读取excel
            List<List<String>> dataList = ExcelReadUtil.readExcelSimpleWithHeader(input, file.getOriginalFilename(), importHeaders);
            if (dataList != null && dataList.size() > 0) {
                List<String> errorMsgList = Lists.newArrayList();
                List<ExcelEntity> importDataList = Lists.newArrayList();
                LOGGER.info(">>> importExcelData, dataList size:{}", dataList.size());
                // 如果是按模板导入，因为有首行，所以带上importHeaders，以便除去首行，以下还是按第一行算起
                int idx = 1;
                for (List<String> values : dataList) {
                    //"userType", "userName", "sex", "idCardNum", "userPhone", "userRemark"
                    LOGGER.info(">>> row {} value :{}", idx++, values);
                    
                    // TODO 检查必填项，同时检查是否空字符串（因为如果按模板中设置了空白行文本格式）
                    if (StringUtils.isAnyBlank(values.get(0), values.get(1), values.get(2), values.get(3), values.get(4))) {
                        // 除了备注其他为必填 TODO 这里的必填非必填是否可以用面向对象思想处理
                        continue;
                    }
                    // TODO 校验数据格式并生成业务模型（只有全部通过后，才进行批量插入DB）
                    ResultMap checkResult = checkImportData(values);
                    if (!checkResult.isSuccess()) {
                        errorMsgList.add(checkResult.getMsg());
                    } else {
                        importDataList.add((ExcelEntity) checkResult.getData());
                    }
                }
                // 如果有导入失败的错误信息，集中返回
                if (CollectionUtils.isNotEmpty(errorMsgList)) {
                    return ResultMap.error(errorMsgList);
                }
                // TODO 导入数据全部校验完成后插入DB
                for (ExcelEntity entity : importDataList) {
                }
            } else {
                return ResultMap.error("上传的excel内容为空");
            }
        } catch (Exception e) {
            LOGGER.error(">>> 导入excel异常", e);
            return ResultMap.error("导入电梯excel失败，请重新尝试");
        } finally {
            IOUtils.close(input);
        }
        return ResultMap.success();
    }

    /**
     * 1.检查数据格式
     * 2.生成业务实体类
     *
     * @param values
     */
    private ResultMap checkImportData(List<String> values) {

        // TODO 检查数据格式，如错误注明其内容位置等用于返回

        // 生成业务实体类
        ExcelEntity entity = new ExcelEntity();
        entity.setUserType(values.get(0));
        entity.setUserName(values.get(1));
        entity.setSex(values.get(2));
        entity.setIdCardNum(values.get(3));
        entity.setUserPhone(values.get(4));
        entity.setUserRemark(values.get(5));
        
        return ResultMap.success(entity);
    }
}