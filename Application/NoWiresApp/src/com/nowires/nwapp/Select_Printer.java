package com.nowires.nwapp;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Select_Printer extends Activity {

	ListView printerListLV;
	ArrayAdapt adapter=null;
	ArrayList<String> printerAL;
	String printerNameS;
	String printerSelectedS;
	ArrayList<PrinterDataObj> printerDObjAL;
	Bundle b;
	Intent i;
	
	SharedPreferences prefs = null;
	
	private MySQLiteHelper mydb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select__printer);
		
		mydb = new MySQLiteHelper(this);
		i = new Intent(this,Job_Queue.class);
		b = new Bundle();
		
		printerDObjAL = new ArrayList<PrinterDataObj>();
		
		prefs = getSharedPreferences(getString(R.string.package_name),MODE_PRIVATE);
		
		if(prefs.getBoolean("firstrun", true)){
			//copyAssests();
			 try {
		        	mydb.createdb();
		        	mydb.openDataBaseWrite();
		        	mydb.insertInitialTables();
		            mydb.closedb();
		    	}catch(SQLException sqle){throw sqle;}   
			 finally{
				 mydb.closedb();
			 }
		        prefs.edit().putBoolean("firstrun", false).commit();
		}
		
		if(check_Connection()){
			login();
		    for(int y=0;y<printerDObjAL.size();y++){
		    	insertPrinters(printerDObjAL.get(y).getPrinterName(), printerDObjAL.get(y).getprinterURL(), printerDObjAL.get(y).getprinterAPIKey());
		    }
			
			printerAL = new ArrayList<String>();
			getPrinters();
			
			if(printerAL.isEmpty()){
				printerListLV = (ListView)findViewById(R.id.printerList);//getListView();		
				adapter = new ArrayAdapt (this,null);
				printerListLV.setAdapter(adapter);
			    adapter.notifyDataSetChanged();
			}
			else{
				Log.d("from al",printerAL.get(0));	
				printerListLV = (ListView)findViewById(R.id.printerList);//getListView();		
				adapter = new ArrayAdapt (this,printerAL);
				printerListLV.setAdapter(adapter);
			    adapter.notifyDataSetChanged();
			}
			
			
			
			printerListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					printerSelectedS = (String) printerListLV.getItemAtPosition(position);
					
					b.putString("printerSelectedS",printerSelectedS);
					i.putExtras(b);
					startActivity(i);
				}
			});
		}//end of check connection
		else{
			i.putExtras(b);
			startActivity(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select__printer, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public boolean check_Connection()  // determines if the network is available or not
	{
	    ConnectivityManager connectivityManager = (ConnectivityManager)
	    		getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();  //return true if network is available| false if not available
	}
	
	//gets job from database 
	public void getPrinters(){
		try {
			mydb.openDataBaseRead();
			printerAL = mydb.getPrintersFromPrintersTbl();
			mydb.closedb();
		}catch(SQLException sqle){throw sqle;}
		finally{
		    mydb.closedb();
		}
		Log.d("Joq_Queue", "getting jobs0");
	}
	
	public void login(){
	     String username = "beesham";//usernameField.getText().toString();
	     String password = "nowires";//passwordField.getText().toString();
	     try {
	    	 printerDObjAL = new SignIn_To_Database(this,0).execute(username,password).get();
			Log.d("printer info from wordpress",printerDObjAL.get(0).getPrinterName().toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void insertPrinters(String printerName,String printerURL,String printerAPIKey){
		//call the db and put the printerdataobjectAL
		 try {
	        	mydb.openDataBaseWrite();
	        	mydb.insertNewPrinter(printerName, printerURL, printerAPIKey);
	            mydb.closedb();
	    	}catch(SQLException sqle){throw sqle;}   
		 finally{
			 mydb.closedb();
		 }
	}
	
	
}
