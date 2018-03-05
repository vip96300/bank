package org.cloud.bank.client.util;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class RequestUtil {
	/**
	 * 自定义请求
	 * @param url
	 * @param params
	 */
	public static String request(String url,Map<String,String> params){
        CloseableHttpClient httpclient = HttpClients.createDefault();  
        HttpPost httppost = new HttpPost(url);  
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        for(Entry<String, String> map:params.entrySet()){
        	formparams.add(new BasicNameValuePair(map.getKey(),map.getValue()));
        }
        HttpEntity entity=null;
        CloseableHttpResponse response=null;
        String json=null;
        try {  
        	UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");  
            httppost.setEntity(uefEntity);  
            System.out.println("executing request " + httppost.getURI());  
            response = httpclient.execute(httppost);
            StatusLine statusLine=response.getStatusLine();
            System.out.println("response status "+statusLine.getStatusCode());
            if(statusLine.getStatusCode()!=200){
            	throw new RuntimeException("response error");
            }
            entity = response.getEntity();   
            json=EntityUtils.toString(entity, "UTF-8");
            httpclient.close();
            response.close();
        } catch (Exception e) {  
            e.printStackTrace();  
        } 
		return json;
	}
	
	/**
	 * 自定义请求
	 * @param url 请求地址
	 * @param params 文本参数
	 * @param files k:文件名，V：文件绝对路径
	 */
	public static String request(String url,Map<String,String> params,List<File> files){
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		httpClient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		HttpEntity requestEntity = null;
		MultipartEntityBuilder multipartEntityBuilder=MultipartEntityBuilder.create();
		for(Map.Entry<String, String> map:params.entrySet()){
			multipartEntityBuilder.addPart(map.getKey(),new StringBody(map.getValue(),ContentType.create("text/plain", Consts.UTF_8)));
		}
		files.forEach(file ->{
			multipartEntityBuilder.addPart(file.getName(),new FileBody(file));
		});
			
		requestEntity=multipartEntityBuilder.build();
		httpPost.setEntity(requestEntity);
		String result=null;
		try {
			response = httpClient.execute(httpPost);
			HttpEntity responseEntity = response.getEntity();
			if (responseEntity != null) {
				result=EntityUtils.toString(responseEntity, Charset.forName("UTF-8"));
			}
			//destroy
			EntityUtils.consume(responseEntity);
			if(response != null){
				response.close();
			}
			if(httpClient != null){
				httpClient.close();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 通过接口下载图片，需要在header中返回fileName
	 * @param url
	 * @param localpath
	 * @return
	 */
	public static void download(String url,File destFile) {
		OutputStream os =null;
		try {
			URLConnection conn = new URL(url).openConnection();
			int read=0;
			byte[] buffer = new byte[1024];
			os = new FileOutputStream(destFile);
			while ((read = conn.getInputStream().read(buffer, 0,buffer.length)) != -1) {
				os.write(buffer, 0, read);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(os!=null){
					os.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/*public static void main(String [] orgs){
		File file=RequestUtil.download("https://www.baidu.com/img/baidu_jgylogo3.gif",File.listRoots()[0]+"file/0030030004117/hezuoshangziliao/");
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getName());
	}*/
}
