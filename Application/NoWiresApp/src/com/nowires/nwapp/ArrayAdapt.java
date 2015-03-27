package com.nowires.nwapp;

import java.util.ArrayList;
import java.util.List;

//import com.pocket.chef.EditActArrayAdapter.ViewHolder;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ArrayAdapt extends ArrayAdapter<String> {

	private MySQLiteHelper mydb;
	 private final List<String> list;
	Context context;
	
	Object objName;
	public ArrayAdapt(Context context,List<String> list) {
		super(context,R.layout.customlist, list);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		mydb = new MySQLiteHelper(context);
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.customlist, null);
		}
			//objName = obj.get(position);
			//if(objName != null){
				//ImageView img = (ImageView) convertView.findViewById(R.id.imageView1);
				
				TextView textTitle = (TextView) convertView.findViewById(R.id.title);
				TextView textDes = (TextView) convertView.findViewById(R.id.des);
				textTitle.setText(list.get(position));
				//textDes.setText("sample des");
				
			//	textTitle.setText(objName.getTitle());
			//	textDes.setText(objName.getDescription());
			//	if(objName.getImg()==null){
			//		img.setImageDrawable((Drawable)context.getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher));
			//	}
			//	else new ImageDownload(objName.getImg(),img).execute();
				
		//	}
			
		

		return convertView;
	}
	
}
