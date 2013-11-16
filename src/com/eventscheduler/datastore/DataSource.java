package com.eventscheduler.datastore;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.eventscheduler.util.MyEvent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSource {

	final String DEBUG_TAG = "DataSource";
	SQLiteDatabase db;
	MySQLiteHelper dbHelper;
	
	public DataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}
	
	public int insert(ContentValues values) {
		Long l = db.insert(MySQLiteHelper.TABLE_NAME, null, values);
		//Log.d(DEBUG_TAG, "insert " + l);
		return l.intValue();
		
	}
	
	public Cursor query () {
		Cursor temp = db.query(MySQLiteHelper.TABLE_NAME, null, null, null, null, null, null);
		return temp;
	}
	
	public void update (ContentValues val, int id) {
		db.update(MySQLiteHelper.TABLE_NAME, val, MySQLiteHelper.ID + " = " + id, null);
		//Log.d(DEBUG_TAG, "update done");
	}
	public void close () {
		db.close();
	}
	public void open () {
		
		db = dbHelper.getWritableDatabase();
	}
	
	public void delete (int id) {
		db.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.ID + " = " + id, null);
	}
	
	
	public static ContentValues eventToValues (MyEvent event) {
		ContentValues values = new ContentValues();
		SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US);
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
		
		values.put(MySQLiteHelper.EVENT_NAME, event.getEventName());
		values.put(MySQLiteHelper.START_TIME,timeFormat.format(event.getStartDate()));
		values.put(MySQLiteHelper.END_TIME,timeFormat.format(event.getEndDate()));
		
		values.put(MySQLiteHelper.START_DATE,dateFormat.format(event.getStartDate()));
		values.put(MySQLiteHelper.IS_EVENT_END_DATE, event.isUntilCheck());
		values.put(MySQLiteHelper.EVENT_END_DATE, dateFormat.format(event.getEventEndDate()));
		
		values.put(MySQLiteHelper.IS_REPEAT, event.isRepeating());
		values.put(MySQLiteHelper.MONDAY, event.isMon());
		values.put(MySQLiteHelper.TUESDAY, event.isTue());
		values.put(MySQLiteHelper.WEDNESDAY, event.isWed());
		values.put(MySQLiteHelper.THURSDAY, event.isThu());
		values.put(MySQLiteHelper.FRIDAY, event.isFri());
		values.put(MySQLiteHelper.SATURDAY, event.isSat());
		values.put(MySQLiteHelper.SUNDAY, event.isSun());
		
		values.put(MySQLiteHelper.RINGER_MODE, event.getRingerMode());
		values.put(MySQLiteHelper.ALARM_SETTING, event.getAlarmSetting());
		values.put(MySQLiteHelper.MEDIA_SETTING, event.getMediaSetting());
		
		values.put(MySQLiteHelper.WIFI_SETTING, event.getWifiSetting());
		values.put(MySQLiteHelper.BLUETOOTH, event.getBluetooth());
		values.put(MySQLiteHelper.MOBILE_DATA, event.getMobileData());
		
		return values;
	}
}
