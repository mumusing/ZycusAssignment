package com.zycus.common.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateHelper
{
	   public static String getCurrentDateTime(String format)
	  {
		  DateFormat dateFormat=new SimpleDateFormat(format);
		  Calendar calender=Calendar.getInstance();
		  String date=dateFormat.format(calender.getTime());
		  return date;
	  }
}
