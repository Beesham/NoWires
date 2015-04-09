package com.nowires.nwapp;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class File_Manipulation_On_Printer extends AsyncTask<String,Void,ArrayList<String>> {
	
	int flag;
	String result = null;
	String file;
	File myFile;
	
	ArrayList<String> jobsFromPrinter;
	
	public File_Manipulation_On_Printer(int f,String fNameS,String curFilePath){
		Log.d("trying","toconncto to printer");
		flag = f;
		file = fNameS;
		if(!(curFilePath == null))
			myFile = new File (curFilePath);
	}
	
	@Override
	protected ArrayList<String> doInBackground(String... arg0) {
		 try{
			 
			 String printerURL = (String)arg0[0];
	         String apiKey = (String)arg0[1];
	         String link = printerURL; 
	         
	         jobsFromPrinter = new ArrayList<String>();
	         
	         HttpClient client = new DefaultHttpClient();
	         HttpGet request = new HttpGet();
	         HttpPost post = new HttpPost();
	         HttpDelete delete = new HttpDelete();
	         HttpResponse response=null;
	         BufferedReader in=null;
	         StringBuffer sb=null;
	         String line;
	         String fileCont=null;
			 
			 switch(flag){
			 
			 case(0)://gets all files from printer
				request.setURI(new URI(printerURL+"/api/files?apikey="+apiKey)); //must use http
            
	            response = client.execute(request);
	            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

	            sb = new StringBuffer("");
	            line="";
	            while ((line = in.readLine()) !=null) {
	            	sb.append(line);
	            	//break;
	            }
	            in.close();
	            
	            result = sb.toString();
	            Log.d("results",result);
	            
	            //*********************
	            //parse json data
	            try{

	            	JSONObject json_data = new JSONObject(result);//jArray.getJSONObject(i);
	            	JSONArray json_data2 = new JSONArray(json_data.getString("files"));
	            	for (int i = 0; i < json_data2.length();i++){
	            			Log.d("printer files2",json_data2.getJSONObject(i).getString("name"));
	            			jobsFromPrinter.add(json_data2.getJSONObject(i).getString("name"));
	            	}

	            } catch (JSONException e){
	            	Log.d("we have exception",e.toString());
	            }
	             return jobsFromPrinter;
 
			 case(1)://uploads a file to printer local
				 
				int size = (int) myFile.length();
			 	int bytesRead = 0;
			    byte[] bytes = new byte[1024];			    
			    			     			    
				String BOUNDARY= "----WebKitFormBoundaryDeC2E3iWbTv1PwMC";
			 	String towDash = "--";
			 	String contentType = "Content-Type: application/octet-stream";
			 	String contentDisposition = "Content-Disposition: form-data; name=\"file\"; filename=\"" + file + "\"";

				post.setURI(new URI(printerURL+"/api/files/local?apikey="+apiKey)); //must use http
				post.setHeader("Content-Type", "multipart/form-data; boundary="+BOUNDARY);
			 
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,BOUNDARY, null);
				 
				entity.addPart("file", new FileBody(myFile));
				 
				post.setEntity(entity);				 
				post.setHeader("Content-Disposition", "form-data; name=\"select\"");
				 
	            response = client.execute(post);

				try{
					in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	
		            sb = new StringBuffer("");
		            line="";
		            while ((line = in.readLine()) !=null) {
		            	sb.append(line);
		            	//break;
		            }
		            in.close();
					}
				catch(Exception e){
					Log.d("response Excep",e.toString());
				}
	            result = sb.toString();
	            Log.d("results",result);
	            
	            //*********************
	            //parse json data
	            try{
	            	JSONObject json_data = new JSONObject(result);//jArray.getJSONObject(i);
	            } catch (JSONException e){
	            	Log.d("we have exception",e.toString());
	            }
	            
				 break;
				 
			 case(2)://delete file
				 delete.setURI(new URI(printerURL+"/api/files/local/"+file+"?apikey="+apiKey)); //must use http
			 	 client.execute(delete);
				 //delete
				 break;
			 case(3):// select a file from printer to print
				 post.setURI(new URI(printerURL+"/api/files/local/"+file+"?apikey="+apiKey)); //must use http
				    
				 JSONObject data = new JSONObject();
				 data.put("command", "select");
				 data.put("print", true);
				
	
				 StringEntity se = new StringEntity(data.toString());
				 post.setEntity(se);
				 post.setHeader("Content-type", "application/json");
	
				 client.execute(post);
				 break;
			 }
	            
	      }catch(Exception e){
	    	  Log.d("Exception",e.toString());
	      }
	      
		return null;
	}
}
