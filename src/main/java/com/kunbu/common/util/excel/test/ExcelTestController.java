package com.kunbu.common.util.excel.test;

import com.alibaba.fastjson.util.IOUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kunbu.common.util.ResultMap;
import com.kunbu.common.util.excel.ExcelExportUtil;
import com.kunbu.common.util.excel.ExcelReadUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
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
public class ExcelTestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelTestController.class);

    /** 导出 */
    private static List<String> exportHeaders =
            Lists.newArrayList("序号", "小区名称", "审核状态", "用户类型", "用户名称", "性别", "身份证号", "手机号", "备注");
    private static List<String> exportKeys =
            Lists.newArrayList("number", "orgName", "auditState", "userType", "userName", "idCardNum", "sex", "userPhone", "userRemark");
    /** 导入（模板） */
    private static List<String> importHeaders =
            Lists.newArrayList("用户类型(业主，租户)", "用户姓名", "性别(男，女)", "身份证号", "手机号", "备注");

    private static final String EXCEL_TEMPLATE_FILENAME = "excel模板(单元格必须为文本)";


    @GetMapping("/export/template")
    @ResponseBody
    public void exportExcelTemplate(HttpServletRequest request, HttpServletResponse response) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ExcelExportUtil.exportExcelTemplate(importHeaders, baos);

        byte[] content = baos.toByteArray();
        responseExportExcel(EXCEL_TEMPLATE_FILENAME, content, request, response);
    }

    @GetMapping("/export/data")
    @ResponseBody
    public void exportExcelData(HttpServletRequest request, HttpServletResponse response) {
        // 模拟业务的实体类
        List<ExcelTestEntity> beanList = getExcelDataList();
        // 将实体类转换成数据集合
        List<Map<String, Object>> dataList = Lists.newArrayList();
        // 手动增加序号
        int number = 1;
        for (ExcelTestEntity eb : beanList) {
            Map<String, Object> dataMap = Maps.newHashMap();
            dataMap.put(exportKeys.get(0), number++);
            dataMap.put(exportKeys.get(1), eb.getOrgName());
            dataMap.put(exportKeys.get(2), eb.getAuditState());
            dataMap.put(exportKeys.get(3), eb.getUserType());
            dataMap.put(exportKeys.get(4), eb.getUserName());
            dataMap.put(exportKeys.get(5), eb.getIdCardNum());
            dataMap.put(exportKeys.get(6), eb.getSex());
            dataMap.put(exportKeys.get(7), eb.getUserPhone());
            dataMap.put(exportKeys.get(8), eb.getUserRemark());
            dataList.add(dataMap);
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 导出2007格式
        ExcelExportUtil.exportExcelSimple2007(exportHeaders, exportKeys, dataList, baos);
        // 测试大数据下导出
///        ExcelExportUtil.exportExcelSimpleBigData(exportHeaders, exportKeys, dataList, baos);

        byte[] content = baos.toByteArray();
        String fileName = "数据列表-" + System.currentTimeMillis();
        responseExportExcel(fileName, content, request, response);
    }

    private static List<ExcelTestEntity> getExcelDataList() {
        List<ExcelTestEntity> beanList = Lists.newArrayList();
        Random random = new Random();
        // 测试大数据下OOM
        for (int i = 1; i < 1000_0; i++) {
            ExcelTestEntity eb = new ExcelTestEntity(
                    random.nextInt() + "小区",
                    random.nextBoolean() ? "待审核" : "审核通过",
                    random.nextBoolean() ? "业主" : "租户",
                    random.nextInt(10000) + "号用户",
                    "3302" + random.nextInt(10000),
                    random.nextBoolean() ? "男" : "女",
                    "1" + random.nextInt(10000),
                    null);
            beanList.add(eb);
        }
        return beanList;
    }

    private void responseExportExcel(String fileName, byte[] content, HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        try {
            response.reset();
            response.addHeader("Cache-Control", "no-cache");
            response.addHeader("Pragma", "no-cache");
            response.setStatus(200);
            response.setDateHeader("Expires", 0L);
            String fn;
            // 如果是苹果浏览器，特殊处理
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            if (userAgent.contains("safari")) {
                byte[] bytes = fileName.getBytes("UTF-8");
                fn = new String(bytes, "ISO-8859-1");
            } else {
                fn = URLEncoder.encode(fileName,"UTF-8");
            }
            //.xlsx
            response.addHeader("Content-Disposition", "attachment;filename="+ fn + ".xlsx");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            outputStream = response.getOutputStream();
            outputStream.write(content);
            outputStream.flush();
        } catch (Exception e) {
            LOGGER.error(">>> 导出excel异常", e);
        } finally {
            IOUtils.close(outputStream);
        }
    }


    @RequestMapping("/import/data")
    @ResponseBody
    public ResultMap importExcelData(@RequestParam MultipartFile excel, HttpServletRequest request, HttpServletResponse response) {
        // 业务判断，比如导入者必须是小区管理员，只能导入该小区下用户，因为小区id从session或token中拿到
        String orgId = null;
        if (StringUtils.isBlank(orgId)) {
            LOGGER.error(">>> importExcelData orgId null");
            return ResultMap.error("请用物业管理员，或小区管理员的身份进行用户导入");
        }
        InputStream input = null;
        try {
            input = excel.getInputStream();
            List<List<String>> dataList = ExcelReadUtil.readExcelSimpleWithHeader(input, excel.getOriginalFilename(), importHeaders);
            if (dataList != null && dataList.size() > 0) {
                List<ExcelTestEntity> importDataList = Lists.newArrayList();
                LOGGER.info(">>> importExcelData, dataList size:{}", dataList.size());
                // 这里因为以模板导入，所以有首行，所以带上了importHeaders，以便除去
                for (int i = 0; i < dataList.size(); i++) {
                    List<String> values = dataList.get(i);
                    //"userType", "userName", "sex", "idCardNum", "userPhone", "userRemark"
                    LOGGER.info(">>> row value :{}", values);
                    if (values.size() < importHeaders.size()) {
                        throw new RuntimeException("参数缺失");
                    }
                    //因为模板种设置了空白行位文本，所以实际上传进来的是空字符串，需要过滤
                    if (StringUtils.isAnyBlank(values.get(0), values.get(1), values.get(2), values.get(3))) {
                        continue;
                    }
                    // 检查导入value，只有全部通过后，才进行批量插入DB
                    ResultMap result = checkImportData(values);
                    if (result.getCode().compareTo(ResultMap.CODE_ERROR) == 0) {
                        return result;
                    } else {
                        LOGGER.info(">>> 住户：{} check成功", values.get(3));
                        importDataList.add((ExcelTestEntity) result.getData());
                    }
                    //
                    for (ExcelTestEntity entity : importDataList) {
                        // 插入DB
                    }
                }
            } else {
                return ResultMap.error("上传的excel内容为空");
            }
        } catch (Exception e) {
            LOGGER.error(">>> 导入excel异常", e);
            return ResultMap.error("导入电梯excel失败，" + e.getMessage());
        } finally {
            IOUtils.close(input);
        }
        return ResultMap.success();
    }

    private ResultMap checkImportData(List<String> values) {
        ExcelTestEntity entity = new ExcelTestEntity();
        // 检查各项value，并作转换（用户类型-业主转换成owner）
        entity.setUserType(values.get(0));
        entity.setUserName(values.get(1));
        entity.setSex(values.get(2));
        entity.setIdCardNum(values.get(3));
        entity.setUserPhone(values.get(4));
        entity.setUserRemark(values.get(5));
        return ResultMap.success(entity);
    }
}