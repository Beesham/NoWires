package com.nowires.nwapp;

import com.example.fileexplorer.FileChooser;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class Add_job extends Activity {

	private ActionBar actionBar;
	private static final int REQUEST_PATH = 1;
	String curFileName="",curFilePath="";
	EditText edittext;
	Bundle b;
	String pName,pURL,pAPIKey;
	
	MySQLiteHelper mydb;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.nowires.nwapp.R.layout.activity_add_job);
        
        actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		mydb = new MySQLiteHelper(this);
		b = this.getIntent().getExtras();
		edittext = (EditText)findViewById(com.nowires.nwapp.R.id.jobPathTF);
		
		if(b.containsKey("printerSelectedS")) pName = b.getString("printerSelectedS");
		
		try{
			mydb.openDataBaseRead();
			pURL = mydb.getPrinterURL(pName);
			pAPIKey = mydb.getPrinterAPIKey(pName);
			mydb.closedb();
		}catch(SQLException e){}
		finally{mydb.closedb();};
	        
    }//end of onCreate
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(com.nowires.nwapp.R.menu.add_job, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
        	back();
            return true;
        }
        return super.onOptionsItemSelected(item); 
    }

    public void getfile(View view){ 
    	Intent i = new Intent(this, FileChooser.class);
    	 startActivityForResult(i,REQUEST_PATH);
    }

    // Listen for results.
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	if (requestCode == REQUEST_PATH){
    		if (resultCode == RESULT_OK) { 
    			curFileName = data.getStringExtra("GetFileName"); 
    			curFilePath = data.getStringExtra("GetPath");
            	edittext.setText(curFileName);
    		}
    	 }
    }
    
    //Validate File
    public void validateFile(View v){
    	ValidateFile valF = new ValidateFile();
    	if(!valF.validateFile(curFileName)){
    		new AlertDialog.Builder(this)
    	    .setTitle("Invalid File")
    	    .setMessage(getResources().getText(com.nowires.nwapp.R.string.badFile))
    	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	           
    	        }
    	     })
    	    .setIcon(android.R.drawable.ic_dialog_alert)
    	    .show();
    	}
    	else{
    		 if(check_Connection())	
    			 new File_Manipulation_On_Printer(1,curFileName,curFilePath+"/"+curFileName,this).execute(pURL,pAPIKey);
	         else{
	  	           try{
	  	        	   mydb.openDataBaseWrite();
	  	        	   mydb.insertNewJobPath(curFileName, curFilePath);
	  	        	   mydb.closedb();
	  	        	   Log.d("Add_Job", "Inserting path");
	  	           	}
	  	           catch(SQLException e){}
	  	           finally{
	  	        	   mydb.closedb();
	  	           	}

		    		new AlertDialog.Builder(this)
		    	    .setTitle("Valid File")
		    	    .setMessage(getResources().getText(com.nowires.nwapp.R.string.goodFile))
		    	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		    	        public void onClick(DialogInterface dialog, int which) { 
		    	        	
		    	          
		    	        }
		    	     })
		    	    .setIcon(android.R.drawable.ic_dialog_alert)
		    	     .show();
	          }
    	}
    }//end of validateFile 
    
    public boolean check_Connection()  // determines if the network is available or not
	{
	    ConnectivityManager connectivityManager = (ConnectivityManager)
	    		getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();  
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();  //return true if network is available| false if not available
	}
    
    public void back(){
    	Intent i = new Intent (this,Job_Queue.class);
    	System.exit(RESULT_OK);
    	startActivity(i);
    }
    
}//end of Add_Job class
