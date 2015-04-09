package com.nowires.nwapp;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.ProgressBar;

public class View_Job extends Activity implements
ActionBar.TabListener,ActionBar.OnNavigationListener {

	private ActionBar actionBar;
	boolean loadingFinished = true;
	boolean redirect = false;
	ProgressBar progb;
	
	ListView jobListOnPrinterLV;
	EditActArrayAdapter adapter=null;
	ArrayList<String> jobonPrinterAL;
	
	Bundle b;
	String pName,pURL,pAPIKey,fileNameS;
	
	MySQLiteHelper mydb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view__job);
		
		mydb = new MySQLiteHelper(this);
		b = this.getIntent().getExtras();
		
		if(b.containsKey("printerSelectedS")) pName = b.getString("printerSelectedS");
		
		try{
			mydb.openDataBaseRead();
			pURL = mydb.getPrinterURL(pName);
			pAPIKey = mydb.getPrinterAPIKey(pName);
			mydb.closedb();
		}catch(SQLException e){}
		finally{mydb.closedb();};
		
		
		actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		jobonPrinterAL = new ArrayList<String>();

		try {
			jobonPrinterAL = new File_Manipulation_On_Printer(0,null,null).execute(pURL,pAPIKey).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		
		
		if(!jobonPrinterAL.isEmpty())
			Log.d("from al",jobonPrinterAL.get(0));
		
		jobListOnPrinterLV = (ListView)findViewById(R.id.joblistonPrinter);//getListView();		
		adapter = new EditActArrayAdapter (this,jobonPrinterAL);
		jobListOnPrinterLV.setAdapter(adapter);
	    adapter.notifyDataSetChanged();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_job_tabs_actions, menu);
		return true;
	}
	
	public void delete(){
		try {
			for(int i=0;i<(((EditActArrayAdapter) adapter).getSelectedChkBx()).size();i++){//for(int i=0;i<recipesAL.size();i++){
				try{
					new File_Manipulation_On_Printer(2,((((EditActArrayAdapter) adapter).getSelectedChkBx()).get(i)),null).execute(pURL,pAPIKey).get();
				}catch(SQLException e){}
			}
		
			recreate();	//refreshes the screen to display updated data
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void back(){
    	Intent i = new Intent (this,Job_Queue.class);
    	System.exit(RESULT_OK);
    	startActivity(i);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		
		// Take appropriate action for each action item click
				switch (item.getItemId()) {
				case R.id.action_delete_job :
					delete();
					return true;
				case android.R.id.home:
					back();
					return true;
				default:
					return super.onOptionsItemSelected(item);
				}
	}

	@Override
	public void onContentChanged() {
	    super.onContentChanged();

	    View empty = findViewById(R.id.empty);
	    ListView list = (ListView) findViewById(R.id.joblistonPrinter);
	    list.setEmptyView(empty);
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
}
