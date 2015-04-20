package com.nowires.nwapp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class Job_Queue extends Activity implements
ActionBar.TabListener,ActionBar.OnNavigationListener {

	private ActionBar actionBar;
	private MySQLiteHelper mydb;
	String pName,pURL,pAPIKey;
	String curFileName="",curFilePath="";
	
	ListView jobListLV;
	EditActArrayAdapter adapter=null;
	ArrayList<String> jobAL;
	
	String jobNameS,printerInfoS,printerSelectedS;
	String fileToDelAftUpld;
	
	Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job__queue);
		
		mydb = new MySQLiteHelper(this);
		b = this.getIntent().getExtras();
		
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		if(b.containsKey("printerSelectedS")&&(!b.isEmpty())){
			printerSelectedS = b.getString("printerSelectedS");
			
			try{
				mydb.openDataBaseRead();
				pURL = mydb.getPrinterURL(printerSelectedS);
				pAPIKey = mydb.getPrinterAPIKey(printerSelectedS);
				mydb.closedb();
			}catch(SQLException e){}
			finally{mydb.closedb();};
		}
		
		jobAL = new ArrayList<String>();
		getJobs();
		
		if (check_Connection()){
			
			if(!jobAL.isEmpty())
			
			for(int i=0;i<jobAL.size();i++){
				ArrayList<String> jobsinPrinter = new ArrayList<String>();
				try {
					mydb.openDataBaseRead();
					curFilePath=mydb.getJobPath(jobAL.get(i));
				    mydb.closedb();
				}catch(SQLException sqle){throw sqle;}
				
				try {
					jobsinPrinter = new File_Manipulation_On_Printer(0,jobAL.get(i),curFilePath+"/"+jobAL.get(i),this).execute(pURL,pAPIKey).get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				
				for(int j=0;j<jobsinPrinter.size();j++){
					if((jobsinPrinter.get(j).equals(jobAL.get(i)))){
						fileToDelAftUpld = jobsinPrinter.get(j);
						delete(1);
					}
					else new File_Manipulation_On_Printer(1,jobAL.get(i),curFilePath+"/"+jobAL.get(i),this).execute(pURL,pAPIKey);
				}
						

				jobListLV = (ListView)findViewById(R.id.joblist);	
				adapter = new EditActArrayAdapter (this,jobAL);
				jobListLV.setAdapter(adapter);
			    adapter.notifyDataSetChanged();
			}
		    
		}
				
    }//end of onCreate

	public void delete(int j){
		switch(j)
		{
		case(0):{
				for(int i=0;i<(((EditActArrayAdapter) adapter).getSelectedChkBx()).size();i++){//for(int i=0;i<recipesAL.size();i++){
					try{
							mydb.openDataBaseWrite();
							mydb.removeJob((((EditActArrayAdapter) adapter).getSelectedChkBx()).get(i));//recipesAL.get(i));
							mydb.closedb();
					}catch(SQLException e){Log.d("SQLException",e.toString());}
					finally{mydb.closedb();}
				}
			}
		case(1):{
					try{
						mydb.openDataBaseWrite();
						mydb.removeJob(fileToDelAftUpld);//recipesAL.get(i));
						mydb.closedb();
					}catch(SQLException e){Log.d("SQLException2",e.toString());}
					finally{mydb.closedb();}
				}
		}	
		recreate();	//refreshes the screen to display updated data
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tabs_actions, menu);
		
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		// Take appropriate action for each action item click
				switch (item.getItemId()) {
				case R.id.action_addJob:
					startAddJobAct();
					return true;
				case R.id.action_viewJobs:
					startViewJobAct();
					return true;
				case R.id.action_delete_job :
					delete(0);
					return true;
				case android.R.id.home:
					back();
					return true;
				default:
					return super.onOptionsItemSelected(item);
				}
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onContentChanged() {
	    super.onContentChanged();

	    View empty = findViewById(R.id.empty);
	    ListView list = (ListView) findViewById(R.id.joblist);
	    list.setEmptyView(empty);
	}
	
	public void copyAssets(String[] fileList) {
		    AssetManager assetManager = getAssets();
		    String[] files = fileList;
		    try {
		        files = assetManager.list("Recipes");
		    } catch (IOException e) {
		        Log.e("tag", "Failed to get asset file list.", e);
		    }
		    
		    for(String filename : files) {
		    	Log.d("file in assets",filename);
		    }
		    
		    for(String filename : files) {
		    	Log.d("file in assets",filename);

		        InputStream in = null;
		        OutputStream out = null;
		        try {
		          in = assetManager.open("Recipes/"+filename);
		          File outFile = new File( Environment.getExternalStorageDirectory().getAbsolutePath() + "/Recipes/" + filename);
		          out = new FileOutputStream(outFile);
		          copyFile(in, out);
		        } catch(IOException e) {
		            Log.e("tag", "Failed to copy asset file: " + filename, e);
		        }     
		        finally {
		            if (in != null) {
		                try {
		                    in.close();
		                } catch (IOException e) {
		                    // NOOP
		                }
		            }
		            if (out != null) {
		                try {
		                    out.close();
		                } catch (IOException e) {
		                    // NOOP
		                }
		            }
		        }  
		    }
		}
	  
	private void copyFile(InputStream in, OutputStream out) throws IOException {
		    byte[] buffer = new byte[1024];
		    int read;
		    while((read = in.read(buffer)) != -1){
		      out.write(buffer, 0, read);
		    }
		}
	
	public boolean check_Connection()  // determines if the network is available or not
	{
	    ConnectivityManager connectivityManager = (ConnectivityManager)
	    		getSystemService(Context.CONNECTIVITY_SERVICE);
	    
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();  //return true if network is available| false if not available
	}
		
	
	//gets job from database 
	public void getJobs(){
		if(!jobAL.isEmpty())
		Log.d("from al",jobAL.get(0));		
		try {
			mydb.openDataBaseRead();
			jobAL = mydb.getJobFromJobsPathTbl();
		    mydb.closedb();
		}catch(SQLException sqle){throw sqle;}

		jobListLV = (ListView)findViewById(R.id.joblist);//getListView();		
		adapter = new EditActArrayAdapter (this,jobAL);
		jobListLV.setAdapter(adapter);
	    adapter.notifyDataSetChanged();		
	}
	
	public void back(){
    	Intent i = new Intent (this,Select_Printer.class);
    	System.exit(RESULT_OK);
    	startActivity(i);
    }
	
	//starts add_job activity
    public void startAddJobAct(){
    	Intent i = new Intent(this,Add_job.class);
    	i.putExtras(b);
    	startActivity(i);
    }//end of startCategories
    
    //starts view_job activity
    public void startViewJobAct(){
    	if(check_Connection()){
	    	Intent i = new Intent(this,View_Job.class);
	    	i.putExtras(b);
	    	startActivity(i);
    	}
    }//end of startCategories
	
    public void startSelectPrinterAct(){
    	Intent i = new Intent(this,Select_Printer.class);
    	startActivity(i);
    }
    
}//end of Job_Queue class
