package com.eventscheduler.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.eventscheduler.datastore.MySQLiteHelper;

import android.database.Cursor;
import android.util.Log;

public class Util {
	static final String DEBUG_TAG = "Util";

	static public Date combineTimeDate (String time, String date) {
		date = date + " " + time;
		Date dt;
		try {
			dt = new SimpleDateFormat("MMM dd, yyyy h:mm a",Locale.US).parse(date);
		} catch (ParseException e) {
			dt = Calendar.getInstance().getTime();
			e.printStackTrace();
		}
		return dt;
	}


	static public MyEvent fillMyEvent (Cursor crsr) {
		//Log.d(DEBUG_TAG, "fillMyEvent");
		MyEvent myEvent= new MyEvent(); 

		String id = crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.ID));
		String uCheck = crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.IS_EVENT_END_DATE));
		String check = crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.IS_REPEAT));


		myEvent.setId(Integer.parseInt(id));
		myEvent.setEventName(crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.EVENT_NAME)));

		myEvent.setRepeating((check.equals("1"))? true:false);
		myEvent.setUntilCheck((uCheck.equals("1"))? true:false);

		myEvent.setMon((crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.MONDAY))).equals("1")? true: false);
		myEvent.setTue((crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.TUESDAY))).equals("1")? true: false);
		myEvent.setWed((crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.WEDNESDAY))).equals("1")? true: false);
		myEvent.setThu((crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.THURSDAY))).equals("1")? true: false);
		myEvent.setFri((crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.FRIDAY))).equals("1")? true: false);
		myEvent.setSat((crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.SATURDAY))).equals("1")? true: false);
		myEvent.setSun((crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.SUNDAY))).equals("1")? true: false);

		myEvent.setRingerMode(crsr.getInt(crsr.getColumnIndex(MySQLiteHelper.RINGER_MODE)));


		String time = crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.START_TIME));
		String date = crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.START_DATE));

		myEvent.setStartDate(Util.combineTimeDate(time, date));

		time = crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.END_TIME));
		date = crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.START_DATE));

		myEvent.setEndDate(Util.combineTimeDate(time, date));
		String endDt = crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.EVENT_END_DATE));

		myEvent.setEventEndDate(Util.combineTimeDate("00:00 AM", endDt));

		return myEvent;
	}

	
	public static String daysToString (Cursor c) {
		StringBuilder sb = new StringBuilder();
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.MONDAY)).equals("1")) {
			sb.append("Mon ");
		}  
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.TUESDAY)).equals("1")) {
			sb.append("Tue ");
		} 
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.WEDNESDAY)).equals("1")) {
			sb.append("Wed ");
		}
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.THURSDAY)).equals("1")) {
			sb.append("Thu ");
		}
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.FRIDAY)).equals("1")) {
			sb.append("Fri ");
		}
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.SATURDAY)).equals("1")) {
			sb.append("Sat ");
		}
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.SUNDAY)).equals("1")) {
			sb.append("Sun");
		} 
		if (sb.toString().equals(""))
			sb.append("No Repeat Days");
		
		return sb.toString();
	}
}
