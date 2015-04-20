package com.nowires.nwapp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.*;
import android.util.Log;



public class MySQLiteHelper extends SQLiteOpenHelper {
	
	//Database version
	private static final int dbVer = 1;
	private static final String dbName = "nowireslocal.db";
	private static final String dbPath = "/data/data/com.nowires.nwapp/databases/";
	private SQLiteDatabase mydb;
    GetContext getContext;
    Context myContext;

    
	public MySQLiteHelper(Context context) {
		super(context, dbName, null, dbVer);
		myContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		getContext = new GetContext();
		//myContext = getContext.getmyContext();
	}
	
	public void createdb(){
		if(databaseExist()){ 
				Log.d("Copied Database","Copied Database");
		}
		else this.getReadableDatabase();	
	}
	
	public boolean databaseExist()
	{
	    SQLiteDatabase checkDB = null;
	    String dbFullPath = dbPath+dbName;
	    try{
	    	checkDB = SQLiteDatabase.openDatabase(dbFullPath,null,SQLiteDatabase.OPEN_READONLY);
	    }catch(SQLiteException e){}
	    return checkDB != null ? true : false;
	}
	
	@Override 
	public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
		db.execSQL("drop table if exists",null);
		onCreate(db);
	}
	
	public void openDataBaseRead() throws SQLException{
		 
    	//Open the database
        String myPath = dbPath + dbName;
    	mydb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
	
	public void openDataBaseWrite() throws SQLException{
		 
    	//Open the database
        String myPath = dbPath + dbName;
    	mydb = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
 
    }
	
	 public void CopyDB() throws IOException {
		 OutputStream outputStream = new FileOutputStream(dbPath);
		 
		 FileInputStream inputStream =  (FileInputStream) (myContext.getAssets()).open(dbName);
		 			byte[] buffer = new byte[1024];
			        int length;
			        while ((length = inputStream.read(buffer)) > 0) {
			        	outputStream.write(buffer, 0, length);
			        }
			        inputStream.close();
			        outputStream.close();
		}
 	
	public void closedb(){
		mydb.close();
	}
	
	
	//Database manipulations
	
 	public void insertInitialTables(){
 		try{mydb.execSQL("CREATE TABLE Jobs_Tbl(Job_ID integer primary key autoincrement,Job_Name varchar,Job_Total_Time varchar(50), Job_Total_Layers int, Job_Temp int, Job_Status varchar(20), Job_Layer_Height long, Job_Finished_Time varchar);");}catch(SQLException e){}	
 		try{mydb.execSQL("CREATE TABLE Jobs_Queue_Tbl(Job_Queue_ID integer primary key autoincrement,Job_ID_FK integer,Job_Total_Time varchar(50),foreign key (Job_ID_FK) references Jobs_Tbl(Job_ID));");}catch(SQLException e){}	
		try{mydb.execSQL("CREATE TABLE Jobs_Path_Tbl(Job_Path_ID integer primary key autoincrement,Job_Name varchar,Job_Path varchar,Job_Upload_Status varchar);");}catch(SQLException e){}
		try{mydb.execSQL("CREATE TABLE Printer_Tbl(Printer_ID integer primary key autoincrement,Printer_Name varchar,Printer_URL varchar,Printer_APIKey varchar);");}catch(SQLException e){}

 	}
	
 	//Inserts
 	
 	public void insertNewJob(String jobName,String jobTotalTime,String jobTotalLayers, int jobTemp, String jobStatus, long jobLayerHeight, String jobFinishedTime){
		try{
			
			ContentValues values = new ContentValues();
			
			values.put("Job_Name", jobName);
			values.put("Job_Total_Time", jobTotalTime);
			values.put("Job_Total_Layers", jobTotalLayers);
			values.put("Job_Temp", jobTemp);
			values.put("Job_Status", jobStatus);
			values.put("Job_Layer_Height", jobLayerHeight);
			values.put("Job_Finished_Time", jobFinishedTime);
			mydb.insert("Jobs_Tbl", null, values);

		}catch(SQLException e){}
	}
 	
 

 	public void insertNewJobPath(String jobName,String jobContentPath){
 			try{
 				
 				ContentValues values = new ContentValues();
 				
 				values.put("Job_Path", jobContentPath);
 				values.put("Job_Name", jobName);
 				//mydb.update("Jobs_Path_Tbl",values,"recipe_name like'"+jobName+"'",null);
 				mydb.insert("Jobs_Path_Tbl", null, values);


 			}catch(SQLException e){}
 		}
 	
 	public void insertNewPrinter(String printerName,String printerURL,String printerAPIKey){
			try{
				
				Log.d("inserting into printer-table","in database");
				
				ContentValues values = new ContentValues();
				
				values.put("printer_name", printerName);
				values.put("printer_url", printerURL);
				values.put("printer_apikey", printerAPIKey);
				mydb.insert("Printer_Tbl", null, values);


			}catch(SQLException e){}
		}

 	//Gets
 	public ArrayList<String> getJobFromJobsTbl(){
		ArrayList<String> jobNamesAL= new ArrayList<String>();
		Cursor c=null;
		String temp;
		
		try{
			c = mydb.rawQuery("select job_name from jobs_tbl",null);

			while (c.moveToNext()){	
					temp = c.getString(c.getColumnIndex("Job_Name"));
					if(!jobNamesAL.contains(temp)){jobNamesAL.add(c.getString(c.getColumnIndex("Job_Name")));}
					}	
			}catch(SQLException e){} 
			c.close();

		return jobNamesAL;
	}
 	
 	public ArrayList<String> getJobFromJobsPathTbl(){
		ArrayList<String> jobNamesAL= new ArrayList<String>();
		Cursor c=null;
		String temp;
		
		try{
			c = mydb.rawQuery("select job_name from Jobs_Path_Tbl",null);

			while (c.moveToNext()){	
					temp = c.getString(c.getColumnIndex("Job_Name"));
					if(!jobNamesAL.contains(temp)){jobNamesAL.add(c.getString(c.getColumnIndex("Job_Name")));}
					}	
			}catch(SQLException e){} 
			c.close();

		return jobNamesAL;
	}
 	
 	public ArrayList<String> getPrintersFromPrintersTbl(){
		ArrayList<String> printerNamesAL= new ArrayList<String>();
		Cursor c=null;
		String temp;
		
		try{
			c = mydb.rawQuery("select Printer_Name from Printer_Tbl",null);

			while (c.moveToNext()){	
					temp = c.getString(c.getColumnIndex("Printer_Name"));
					if(!printerNamesAL.contains(temp)){printerNamesAL.add(c.getString(c.getColumnIndex("Printer_Name")));}
					}	
			}catch(SQLException e){} 
			c.close();

		return printerNamesAL;
	}
 	
 	public String getPrinterURL(String printerName){
		Cursor c=null;
		String temp=null;
		
		try{
			c = mydb.rawQuery("select Printer_URL from Printer_Tbl where Printer_Name like '"+printerName+"'",null);

			while (c.moveToNext()){	
					temp = c.getString(c.getColumnIndex("Printer_URL"));
					}	
			}catch(SQLException e){} 
			c.close();

		return temp;
	}
 	
 	public String getJobPath(String jobName){
		Cursor c=null;
		String temp=null;
		
		try{
			c = mydb.rawQuery("select Job_Path from Jobs_Path_Tbl where Job_Name like '"+jobName+"'",null);

			while (c.moveToNext()){	
					temp = c.getString(c.getColumnIndex("Job_Path"));
					}	
			}catch(SQLException e){} 
			c.close();

		return temp;
	}
 	
 	public String getPrinterAPIKey(String printerName){
		Cursor c=null;
		String temp=null;
		
		try{
			c = mydb.rawQuery("select Printer_APIKey from Printer_Tbl where Printer_Name like '"+printerName+"'",null);

			while (c.moveToNext()){	
					temp = c.getString(c.getColumnIndex("Printer_APIKey"));
					}	
			}catch(SQLException e){} 
			c.close();

		return temp;
	}
 	
 	
 	
 	public boolean checkIfExists(String jobName){
 		boolean check=false;
		Cursor c=null;

 		try{
			c = mydb.rawQuery("select job_name from job_name where job_name like '"+jobName+"'",null);

			while (c.moveToNext()){	
					if(c.getString(c.getColumnIndex("Job_Name")).equals(jobName)) check=true;
					}	
			
			c.close();
			}catch(SQLException e){} 
 		
 		return check;
 	}
 	
 	
 	//Removes
 	
 	public void removeJob(String jobName){

		try{		
			//Log.d("Deleteing",jobName);
			mydb.delete("Jobs_Tbl",  "Job_Name like '"+jobName+"'", null);
			mydb.delete("Jobs_Path_Tbl",  "Job_Name like '"+jobName+"'", null);
		}catch(SQLException e){
			Log.d("SQLException in mySQL",e.toString());
		} 

	}
 	
 	
}//end of MySQLiteHelper class


