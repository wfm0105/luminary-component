package com.luminary.component.util.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;  

public class HttpDownloadUtil {

	/** 
	 * 说明 
	 * 利用httpclient下载文件 
	 * maven依赖 
	 * <dependency> 
	*           <groupId>org.apache.httpcomponents</groupId> 
	*           <artifactId>httpclient</artifactId> 
	*           <version>4.0.1</version> 
	*       </dependency> 
	*  可下载http文件、图片、压缩文件 
	*  bug：获取response header中Content-Disposition中filename中文乱码问题 
	 * @author tanjundong 
	 * 
	 */  
	  
	    public static final int cache = 10 * 1024;  
	    public static final boolean isWindows;  
	    public static final String splash;  
	    public static final String root;  
	    static {  
	        if (System.getProperty("os.name") != null && System.getProperty("os.name").toLowerCase().contains("windows")) {  
	            isWindows = true;  
	            splash = "\\";  
	            root="D:";  
	        } else {  
	            isWindows = false;  
	            splash = "/";  
	            root="/search";  
	        }  
	    }  
	      
	    /** 
	     * 根据url下载文件，文件名从response header头中获取 
	     * @param url 
	     * @return 
	     */  
	    public static String download(String url) {  
	        return download(url, null);  
	    }  
	  
	    /** 
	     * 根据url下载文件，保存到filepath中 
	     * @param url 
	     * @param filepath 
	     * @return 
	     */  
	    public static String download(String url, String filepath) {  
	        try {  
	            HttpClient client = new DefaultHttpClient();  
	            HttpGet httpget = new HttpGet(url);  
	            HttpResponse response = client.execute(httpget);  
	  
	            HttpEntity entity = response.getEntity();  
	            InputStream is = entity.getContent();  
	            if (filepath == null)  
	                filepath = getFilePath(response);  
	            File file = new File(filepath);  
	            file.getParentFile().mkdirs();  
	            FileOutputStream fileout = new FileOutputStream(file);  
	            /** 
	             * 根据实际运行效果 设置缓冲区大小 
	             */  
	            byte[] buffer=new byte[cache];  
	            int ch = 0;  
	            while ((ch = is.read(buffer)) != -1) {  
	                fileout.write(buffer,0,ch);  
	            }  
	            is.close();  
	            fileout.flush();  
	            fileout.close();  
	  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
	    }  
	    /** 
	     * 获取response要下载的文件的默认路径 
	     * @param response 
	     * @return 
	     */  
	    public static String getFilePath(HttpResponse response) {  
	        String filepath = root + splash;  
	        String filename = getFileName(response);  
	  
	        if (filename != null) {  
	            filepath += filename;  
	        } else {  
	            filepath += getRandomFileName();  
	        }  
	        return filepath;  
	    }  
	    /** 
	     * 获取response header中Content-Disposition中的filename值 
	     * @param response 
	     * @return 
	     */  
	    public static String getFileName(HttpResponse response) {  
	        Header contentHeader = response.getFirstHeader("Content-Disposition");  
	        String filename = null;  
	        if (contentHeader != null) {  
	            HeaderElement[] values = contentHeader.getElements();  
	            if (values.length == 1) {  
	                NameValuePair param = values[0].getParameterByName("filename");  
	                if (param != null) {  
	                    try {  
	                        //filename = new String(param.getValue().toString().getBytes(), "utf-8");  
	                        //filename=URLDecoder.decode(param.getValue(),"utf-8");  
	                        filename = param.getValue();  
	                    } catch (Exception e) {  
	                        e.printStackTrace();  
	                    }  
	                }  
	            }  
	        }  
	        return filename;  
	    }  
	    /** 
	     * 获取随机文件名 
	     * @return 
	     */  
	    public static String getRandomFileName() {  
	        return String.valueOf(System.currentTimeMillis());  
	    }  
	    public static void outHeaders(HttpResponse response) {  
	        Header[] headers = response.getAllHeaders();  
	        for (int i = 0; i < headers.length; i++) {  
	            System.out.println(headers[i]);  
	        }  
	    }  
	    
	    public static void main(String[] args) {  
//	      String url = "http://bbs.btwuji.com/job.php?action=download&pid=tpc&tid=320678&aid=216617";  
	        String url="https://api.weixin.qq.com/cgi-bin/media/get?access_token=5_zLDWgoRN7c8n3D8OdrEKQLSGC8t_6DMtQPV5Zx1Zwm5ZYMN1oakTdgKNeOGO9BvFynnCGW_chDaoZJmFpnnNm82YgbmuFKu4t7vZjgEKfNyIjMpwZ0G2aLj-dXrC8BYKuxvQMkTwucDp-uIzPUOjADAFKE&media_id=w7YHJI4EA-x6_kQJ0IOj61psMbcUeZblJW0BOPGW19uS-1ZPfVfTlq1fI5e3h6BY";  
//	      String filepath = "D:\\test\\a.torrent";  
	        String filepath = "G:\\a.jpg";  
	        HttpDownloadUtil.download(url, filepath);  
	    }  
}
