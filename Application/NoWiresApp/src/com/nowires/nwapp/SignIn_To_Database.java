package com.nowires.nwapp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

public class SignIn_To_Database  extends AsyncTask<String,Void,ArrayList<PrinterDataObj>>{

   private TextView statusField,roleField;
   private Context context;
   private int byGetOrPost = 0; 
   String result = null;
   PrinterDataObj pDObj;
   ArrayList<PrinterDataObj> prinDObjAL;
   

   public SignIn_To_Database(Context context,int flag){
      this.context = context;
      byGetOrPost = flag;
      
      prinDObjAL = new ArrayList<PrinterDataObj>();
   }

   protected void onPreExecute(){

   }
   
   @Override
   protected ArrayList<PrinterDataObj> doInBackground(String... arg0) {
      if(byGetOrPost == 0){ 
	         try{
	            String username = (String)arg0[0];
	            String password = (String)arg0[1];
	            String link = "http://kirolous.com/static/php/logindb.php?username="
	            +username+"&password="+password;
	            URL url = new URL(link);
	            HttpClient client = new DefaultHttpClient();
	            HttpGet request = new HttpGet();
	            request.setURI(new URI(link));
	            HttpResponse response = client.execute(request);
	            BufferedReader in = new BufferedReader
	           (new InputStreamReader(response.getEntity().getContent()));
	
	           StringBuffer sb = new StringBuffer("");
	           String line="";
	           while ((line = in.readLine()) != null) {
	              sb.append(line);
	              break;
	            }
	            in.close();
	            
	            result = sb.toString();
	            
	            //*********************
	            //parse json data
	            try{
	            	JSONArray jArray = new JSONArray(result);
	            	
	            	int flag = 1;
	            	for (int i = 0; i < jArray.length();i++){
	            			JSONObject json_data = jArray.getJSONObject(i);
	            			Log.d("trying to parse json","trying");
	            			Log.d("printer name",json_data.getString("Printer_Name"));
	            			prinDObjAL.add(new PrinterDataObj (json_data.getString("Printer_Name"), json_data.getString("Printer_URL"), json_data.getString("Printer_APIKey")));
	            	}
	            	
	            } catch (JSONException e){
	            	Log.d("we have exception",e.toString());
	            }
	            
	            
	            //**********************
	            if(!prinDObjAL.isEmpty())
	            Log.d("in signin",prinDObjAL.get(0).getPrinterName().toString());
	            else{
	            	Log.d("arraylist","empty");
	            }            
	            return prinDObjAL;
	      }catch(Exception e){
	    	  Log.d("Exception",e.toString());
	      }
      }
	return prinDObjAL;
      
   }
}