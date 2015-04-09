package com.nowires.nwapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class Printer_State extends AsyncTask<String,Void,String> {

	String result = null;
	
	public Printer_State(){
		Log.d("trying","toconncto to printer");
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		 try{
	            String printerURL = (String)arg0[0];
	            String apiKey = (String)arg0[1];
	            String link = printerURL;
	            
	            HttpClient client = new DefaultHttpClient();
	            HttpGet request = new HttpGet();
	            request.setURI(new URI(printerURL+"/api/printer?history=true&limit=2&apikey="+apiKey)); //must use http
	                        
	            HttpResponse response = client.execute(request);
	            BufferedReader in = new BufferedReader
	           (new InputStreamReader(response.getEntity().getContent()));

	           StringBuffer sb = new StringBuffer("");
	           String line="";
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
	            } catch (JSONException e){
	            	Log.d("we have exception",e.toString());
	            }
	         
	      }catch(Exception e){
	    	  Log.d("Exception",e.toString());
	      }
	      
		return null;
	}
	
	@Override
	   protected void onPostExecute(String result){
		   Log.d("login", "success");
	   }
}
