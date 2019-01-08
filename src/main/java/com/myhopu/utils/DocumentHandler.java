package com.myhopu.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;  
import freemarker.template.Template;  
import freemarker.template.TemplateException;  

/**
 * 公用的word文档导出工具类
 * @author zero
 *
 */
public class DocumentHandler {

	private static Configuration configuration = null;  
	
	static {
		configuration = new Configuration();  
        configuration.setDefaultEncoding("utf-8");
	}
	
//    public DocumentHandler() {  
//    	configuration = new Configuration();  
//        configuration.setDefaultEncoding("utf-8");
//    }  
  
    public static String createDoc(Map dataMap,String ftlName,String fileName) {//数据，ftl模板名字，文件名(用户id加时间戳)
         // 要填入模本的数据文件  
        //Map dataMap = new HashMap();  
        //getData(dataMap);  
        // 设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，    
          // 这里我们的模板是放在com.template包下面    
        configuration.setClassForTemplateLoading(DocumentHandler.class, "/com/myhopu/template");  
        Template t = null;  
        try {  
             // test.ftl为要装载的模板   
            //t = configuration.getTemplate("word.ftl");  
        	t = configuration.getTemplate(ftlName);
            t.setEncoding("utf-8");  
  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        // 输出文档路径及名称  
        String outUrl = DocumentHandler.class.getClassLoader().getResource("").getPath()+"wordExcle/"+fileName+".doc";
        File outFile = new File(outUrl);
        if(!outFile.exists()) {//查看上级目录是否存在，不存在就创建，不能直接全部创建否则.doc也会被视为目录
        	outFile.getParentFile().mkdirs();
        }
        
        Writer out = null;  
          
        try {  
            out = new BufferedWriter(new OutputStreamWriter(  
            new FileOutputStream(outFile), "utf-8"));  
        } catch (Exception e1) {  
            e1.printStackTrace();  
        }  
  
        try {  
            t.process(dataMap, out);  
            out.close();  
        } catch (TemplateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        
        return outUrl;
    }  
      
    /** 
     * 注意dataMap里存放的数据Key值要与模板中的参数相对应  
     * 下面是一种通用的格式
     * 
     * 填充测试数据格式
     * @param dataMap 
     *  
     */  
    @SuppressWarnings("unchecked")
	public static Map getData(Map dataMap) {  
         dataMap.put("title", "请假表");    
           
         List<Map<String, Object>> newsList=new ArrayList<Map<String,Object>>();  
            for(int i=0;i<=5;i++){  
             Map<String, Object> map=new HashMap<String, Object>();  
             map.put("num", i+1);  
             map.put("leaveType", "事假"+i);  
             map.put("timeStart", "2018年11月15日01:11:34");  
             map.put("timeEnd", "2018-11-15 01:11:49");  
             map.put("timeUsed", "2");
             map.put("username", "测试用户");
             map.put("leaveStatus", "已通过");
             newsList.add(map);  
            }  
            dataMap.put("listTest",newsList);  
            
         return dataMap;
    }  
    
    /**
     * 下载调用示例
     * @param args
     */
	public static void main(String[] args) {
		String outUrl = DocumentHandler.createDoc(getData(new HashMap()),"word.ftl","请假表");  
        System.out.println(outUrl);
		
		
//		String url =  DocumentHandler.class.getClassLoader().getResource("").getPath();
//		System.out.println(url);
	}
    
}
