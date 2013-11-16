package com.eventscheduler.datastore;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{

	 static final String DEBUG_TAG = "MySQLiteHelper";
	
	private static final String DATABASE_NAME = "events.db";
	private static final int DATABASE_VERSION = 1;

	 static final String TABLE_NAME = "myevents";


	 public static final String ID = "_id";
	 public static final String EVENT_NAME = "eventname" ;
	
	 public static final String START_TIME = "starttime";
	 public static final String END_TIME = "endtime";
	
	 public static final String START_DATE = "startdate";
	 public static final String IS_EVENT_END_DATE = "iseventend";
	 public static final String EVENT_END_DATE = "eventenddate";
	
	 public static final String IS_REPEAT = "repeat";
	 public static final String MONDAY = "mon";
	 public static final String TUESDAY = "tue";
	 public static final String WEDNESDAY = "wed";
	 public static final String THURSDAY = "thur";
	 public static final String FRIDAY = "fri";
	 public static final String SATURDAY = "sat";
	 public static final String SUNDAY = "sun";
	 
	 public static final String RINGER_MODE = "ringermode";
	 public static final String ALARM_SETTING = "alarmsetting"; 
	 public static final String MEDIA_SETTING = "mediasetting";
	
	 public static final String WIFI_SETTING = "wifisetting";
	 public static final String BLUETOOTH = "bluetooth";
	 public static final String MOBILE_DATA = "mobiledata";
	
	public MySQLiteHelper(Context context) {
		// Cursorfactory is null ... changeable
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = String.format("create table %s " + "( %s integer primary key autoincrement,  %s text, %s text, " 
				+"%s text, %s text, %s text, %s text, %s text, %s text, %s text, %s text, %s text, " 
				+ "%s text, %s text, %s text, %s integer, %s integer, %s integer, %s integer, %s integer, %s integer )",
				TABLE_NAME, ID, EVENT_NAME, START_TIME, END_TIME, START_DATE, IS_EVENT_END_DATE, 
				EVENT_END_DATE, IS_REPEAT, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, 
				SUNDAY, RINGER_MODE, ALARM_SETTING, MEDIA_SETTING, WIFI_SETTING, BLUETOOTH, MOBILE_DATA);
		//Log.d(DEBUG_TAG, "onCreate with sql : " + sql);
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop if exists "+ TABLE_NAME);
		onCreate(db);
		
	}

}
