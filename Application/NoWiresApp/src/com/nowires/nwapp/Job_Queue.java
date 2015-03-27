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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Job_Queue extends Activity implements
ActionBar.TabListener,ActionBar.OnNavigationListener {

	private MySQLiteHelper mydb;
	SharedPreferences prefs = null;
	
	ListView jobListLV;
	ArrayAdapt adapter=null;
	ArrayList<String> jobAL;
	String jobNameS,printerInfoS;
	Bundle b;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_job__queue);
		
		mydb = new MySQLiteHelper(this);
		
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
		
		jobAL = new ArrayList<String>();
		getJobs();
		
		if(!jobAL.isEmpty())
		Log.d("from al",jobAL.get(0));
		
		jobListLV = (ListView)findViewById(R.id.joblist);//getListView();		
		adapter = new ArrayAdapt (this,jobAL);
		jobListLV.setAdapter(adapter);
	    adapter.notifyDataSetChanged();
	    
	    final Intent i = new Intent(this, View_Job.class);
	    
	    /*
	    jobListLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				jobNameS = (String) jobListLV.getItemAtPosition(position);
				
				//b.putString("jobNameS",jobNameS);
				//i.putExtras(b);
				startActivity(i);
			}
		});
		*/
		
	    login();
    }//end of onCreate

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
				case android.R.id.home:
					//back();
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
	
	public void login(){
	     String username = "beesham";//usernameField.getText().toString();
	     String password = "nowires";//passwordField.getText().toString();
	     //method.setText("Get Method");
	     try {
			printerInfoS = new SignIn_To_Database(this,0).execute(username,password).get();
			Log.d("printer info from wordpress",printerInfoS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	  
	//gets job from database 
	public void getJobs(){
			try {
				mydb.openDataBaseRead();
				jobAL = mydb.getJobFromJobsPathTbl();
			    mydb.closedb();
			}catch(SQLException sqle){throw sqle;}
			Log.d("Joq_Queue", "getting jobs0");
		}
	  
	//starts add_job activity
    public void startAddJobAct(){
    	Intent i = new Intent(this,Add_job.class);
    	startActivity(i);
    }//end of startCategories
    
    //starts view_job activity
    public void startViewJobAct(){
    	Intent i = new Intent(this,View_Job.class);
    	startActivity(i);
    }//end of startCategories
	
    
}//end of Job_Queue class
