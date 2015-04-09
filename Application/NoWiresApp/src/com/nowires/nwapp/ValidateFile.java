package com.nowires.nwapp;

public class ValidateFile {

	boolean validateFile(String str){
		if(str.endsWith(".gcode")) return true;		
		else return false;		
	}	
}//end of ValidateFile class
