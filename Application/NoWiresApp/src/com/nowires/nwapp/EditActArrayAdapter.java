package com.nowires.nwapp;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;


public class EditActArrayAdapter extends ArrayAdapter<String> {
	
	  private final List<String> list;
	  private final Activity context;
	  ArrayList<String> selectedChkBx;

	  public EditActArrayAdapter(Activity context, List<String> list) {
	    super(context, R.layout.rowbuttonlayout, list);
	    this.context = context;
	    this.list = list;
	    selectedChkBx = new ArrayList<String>();
	  }

	  //holds the view so as when a checkbox is selected and the list is scrolled 
	  //the checked checkbox is still checked
	  static class ViewHolder {
	    protected TextView text;
	    protected CheckBox checkbox;
	  }
		
	 @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    View view = null;
	   
	    
	    if (convertView == null) {
	      LayoutInflater inflator = context.getLayoutInflater();
	      view = inflator.inflate(R.layout.rowbuttonlayout, null);
	      final ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) view.findViewById(R.id.label);
	      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
	      viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

	            @Override
	            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	            	String s;
	            	if(viewHolder.checkbox.isChecked()){
	            		Log.d("checkbox checked:", viewHolder.text.getText().toString());
	            		s=viewHolder.text.getText().toString();
	            	    setSelectedChBx(s);	            	   
	            	}
	            	
	            	if(!(viewHolder.checkbox.isChecked())){
	            		s=viewHolder.text.getText().toString();
	            	    unsetSelectedChBx(s);	            	   
	            	}
	            }
	          });
	      
	      view.setTag(viewHolder);
	      viewHolder.checkbox.setTag(list.get(position));
	    } else {
	      view = convertView;
	      ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
	    }
	    
	    ViewHolder holder = (ViewHolder) view.getTag();
	    holder.text.setText(list.get(position));
	    return view;
	  }

	 public void setSelectedChBx(String s){
		 selectedChkBx.add(s);
	 }
	 
	 public void unsetSelectedChBx(String s){
		 selectedChkBx.remove(s);
	 }
	 
	 public ArrayList<String> getSelectedChkBx(){
		 return selectedChkBx;
	 }
	 
} 

