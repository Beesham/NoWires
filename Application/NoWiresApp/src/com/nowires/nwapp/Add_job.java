package com.nowires.nwapp;

import com.example.fileexplorer.FileChooser;
import com.example.fileexplorer.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Add_job extends Activity {

	private ActionBar actionBar;
	private static final int REQUEST_PATH = 1;
	String curFileName="",curFilePath="";
	EditText edittext;
	
	MySQLiteHelper mydb;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.nowires.nwapp.R.layout.activity_add_job);
        
        actionBar = getActionBar();
		//actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		mydb = new MySQLiteHelper(this);
		
		edittext = (EditText)findViewById(com.nowires.nwapp.R.id.jobPathTF);
		        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.nowires.nwapp.R.menu.add_job, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
  
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
           // NavUtils.navigateUpFromSameTask(this);
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
        // See which child activity is calling us back.
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
    	    .setTitle("Invalide File")
    	    .setMessage(getResources().getText(com.nowires.nwapp.R.string.badFile))
    	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
    	           
    	        }
    	     })
    	    .setIcon(android.R.drawable.ic_dialog_alert)
    	     .show();
    	}
    	else{
    		new AlertDialog.Builder(this)
    	    .setTitle("Valide File")
    	    .setMessage(getResources().getText(com.nowires.nwapp.R.string.goodFile))
    	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
    	        public void onClick(DialogInterface dialog, int which) { 
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
    	        }
    	     })
    	    .setIcon(android.R.drawable.ic_dialog_alert)
    	     .show();
    	}
    }//end of validateFile 
    
    public void back(){
    	Intent i = new Intent (this,Job_Queue.class);
    	System.exit(RESULT_OK);
    	startActivity(i);
    }
    
}//end of Add_Job class
