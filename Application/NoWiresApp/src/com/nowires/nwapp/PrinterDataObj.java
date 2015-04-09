package com.nowires.nwapp;


public class PrinterDataObj {
	
	private String printerName;
	private String printerURL;
	private String printerAPIKey;

	public PrinterDataObj (String pName, String pURL, String pAPIKey){
		printerName = pName;
		printerURL = pURL;
		printerAPIKey = pAPIKey;
	}
	
	public String getPrinterName() {
		return printerName;
	}
	public void setprinterName(String printerName) {
		this.printerName = printerName;
	}

	public  String getprinterURL() {
		return printerURL;
	}
	public void setprinterURL(String printerURL) {
		this.printerURL = printerURL;
	}
	
	public String getprinterAPIKey() {
		return printerAPIKey;
	}
	
	public void setprinterAPIKey() {
		this.printerAPIKey = printerAPIKey;
	}
		
	
}
