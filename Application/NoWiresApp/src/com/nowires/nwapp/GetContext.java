package com.nowires.nwapp;

import android.app.Activity;
import android.content.Context;

class GetContext extends Activity{
	Context myContext;
	
	public Context getmyContext(){
		myContext = (Context) getBaseContext();
		return myContext;
	}
	
}