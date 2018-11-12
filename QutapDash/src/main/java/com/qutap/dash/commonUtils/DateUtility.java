package com.qutap.dash.commonUtils;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.zip.DataFormatException;



public class DateUtility {
		
	public static String FORMAT_1 ="dd-M-yyyy hh:mm:ss";
	
	
	public static String getDate(Date date,String format){
		SimpleDateFormat dateFormat=new SimpleDateFormat(format);		
		return dateFormat.format(date);	
		
	}

}
