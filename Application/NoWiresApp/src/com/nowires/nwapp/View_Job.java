package com.nowires.nwapp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

public class View_Job extends Activity {

	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view__job);
		
		WebView webview = (WebView)findViewById(R.id.viewJobWV);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setScrollBarStyle(webview.SCROLLBARS_INSIDE_OVERLAY);
		webview.loadUrl(getString(R.string.jobsInPrinterURL));
		
		actionBar = getActionBar();
			//actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view__job, menu);
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
	
	public void back(){
    	Intent i = new Intent (this,Job_Queue.class);
    	System.exit(RESULT_OK);
    	startActivity(i);
    }
}
