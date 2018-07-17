package com.luminroy.component.util.common;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;

/**
 * 使用easypoi导入导出excel的工具类
 * 
 * @author huangtf
 * @date 2017/03/15
 *
 */
public class ExcelUtil {
	
    /**
     * excel导出
     * 
     * @param exportParams
     *            表格标题属性
     * @param pojoClass
     *            Excel对象Class
     * @param dataSet
     *            Excel对象数据List
     */
	 public static Workbook exportExcel(ExportParams exportParams, Class<?> pojoClass,Collection<?> dataSet) {
    	return ExcelExportUtil.exportExcel(exportParams, pojoClass, dataSet); 
    }
	 
    /**
     * excel导入
     * 
     * @param inputstream
     *            Excel文件对应的输入流
     * @param pojoClass
     *            Excel对象Class
     * @param params
     *            Excel导入参数
     * @throws Exception 
     */
    public static List<?> importExcel(InputStream inputstream, Class<?> pojoClass,ImportParams params) throws Exception {
    	return ExcelImportUtil.importExcel(inputstream, pojoClass, params); 
    }
    
   

}
