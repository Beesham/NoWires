package com.nowires.nwapp;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

public class Check_Printer_Status extends AsyncTask<String,Void,Boolean> {

	String pName,pURL,pAPIKey;
	private MySQLiteHelper mydb;
	GetContext getContext;
    Context myContext;
    Check_Printer_Status(Context context){
    	this.myContext=context;
    }
    
	@Override
	protected Boolean doInBackground(String... params) {
		HttpClient client = new DefaultHttpClient();

		HttpGet request = new HttpGet();
        HttpResponse response=null;
        Boolean retVal=false;
        
        mydb = new MySQLiteHelper(myContext);
        
        try{
			mydb.openDataBaseRead();
			pURL = mydb.getPrinterURL(params[0]);
			pAPIKey = mydb.getPrinterAPIKey(params[0]);
			mydb.closedb();
		}catch(SQLException e){
			Log.d("Exception in cps",e.toString());
		}
		finally{mydb.closedb();};
        
        try {
			request.setURI(new URI(pURL+"/api/files?apikey="+pAPIKey));
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return false;
		} 
        
        try {
			response = client.execute(request);
			if(response.getStatusLine().getStatusCode()==200) retVal = true;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			Log.d("returniong flase","false");
			retVal = false;
		}
        return retVal;
	}
	

	
}
