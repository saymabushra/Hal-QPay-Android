package com.haltechbd.app.android.qpay.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateAndTime {

	// Method retrieving current date and time
	// Method retrieving current date and time
	// Method retrieving current date and time
	public static String getCurrentDateAndTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		String strCurrentDateAndTime = sdf.format(new Date());
		return strCurrentDateAndTime;
	}

	// Method retrieving current date
	// Method retrieving current date
	// Method retrieving current date
	public static String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String strCurrentDate = sdf.format(new Date());
		return strCurrentDate;
	}

	// Method retrieving current time
	// Method retrieving current time
	// Method retrieving current time
	public static String getCurrentTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
		String strCurrentTime = sdf.format(new Date());
		return strCurrentTime;
	}
}
