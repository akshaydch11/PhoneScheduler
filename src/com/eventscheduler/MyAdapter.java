package com.eventscheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


import com.eventscheduler.datastore.MySQLiteHelper;
import com.eventscheduler.util.Util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class MyAdapter extends CursorAdapter {

	private final String DEBUG_TAG = "AddEventActivity$MyAdapter";
	private LayoutInflater mLayoutInflator ;
	
	public MyAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		mLayoutInflator = LayoutInflater.from(context);
		
	}

	@Override
	public void bindView(View view, Context context, Cursor c) {
		// showing event name
		String title =c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.EVENT_NAME));
		//if (title != null)
			TextView txtv = ((TextView)view.findViewById(R.id.event_name));
			txtv.setText(title);
		
		//showing start - end time
		String startTime = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.START_TIME));

		String endTime = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.END_TIME));
			((TextView)view.findViewById(R.id.start_end_time)).setText(startTime + "-" + endTime);
		
		String temp = "", temp1 = "";
		String startDate = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.START_DATE));
		String endDate = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.EVENT_END_DATE));
		try {
			Date dt = new SimpleDateFormat("MMM dd, yyyy",Locale.US).parse(startDate);
			 temp =new SimpleDateFormat("MMM dd",Locale.US).format(dt);
			
			dt = new SimpleDateFormat("MMM dd, yyyy",Locale.US).parse(endDate);
			 temp1 =new SimpleDateFormat("MMM dd",Locale.US).format(dt);
		} catch (ParseException e) {
			Log.d(DEBUG_TAG, "parseException");
			e.printStackTrace();
		}
		//Log.d(DEBUG_TAG, "isEvent " + c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.IS_EVENT_END_DATE)));
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.IS_EVENT_END_DATE)).equals("1")) {
			((TextView)view.findViewById(R.id.start_end_date)).setText(temp + " until " + temp1);
		} else
			((TextView)view.findViewById(R.id.start_end_date)).setText(temp );
		
		//showing repeating days
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.IS_REPEAT)).equals("0")) {
			((TextView)view.findViewById(R.id.repeated_days)).setText("No Repeat");
		} else {
			String days = Util.daysToString(c);
			((TextView)view.findViewById(R.id.repeated_days)).setText(days);
		}
		
		
		// toggle images
		// ringerMode
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.RINGER_MODE)).equals("0")) {
			((ImageView)view.findViewById(R.id.imageRinger)).setImageResource(R.drawable.ic_action_silent);
		} else 	if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.RINGER_MODE)).equals("1")) {
			((ImageView)view.findViewById(R.id.imageRinger)).setImageResource(R.drawable.ic_action_vibrate);
		} else 	if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.RINGER_MODE)).equals("2")) {
			((ImageView)view.findViewById(R.id.imageRinger)).setImageResource(R.drawable.ic_action_normal);
		}
		
		// alarm
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.ALARM_SETTING)).equals("0")) {
			((ImageView)view.findViewById(R.id.imageAlarm)).setImageResource(R.drawable.ic_action_alarms_off);
		} else {
			((ImageView)view.findViewById(R.id.imageAlarm)).setImageResource(R.drawable.ic_action_alarms_on);
		}
		
		// media
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.MEDIA_SETTING)).equals("0")) {
			((ImageView)view.findViewById(R.id.imageMedia)).setImageResource(R.drawable.ic_action_media_off);
		} else {
			((ImageView)view.findViewById(R.id.imageMedia)).setImageResource(R.drawable.ic_action_media_on);
		}
		
		// wifi
		
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.WIFI_SETTING)).equals(String.valueOf(WifiManager.WIFI_STATE_DISABLED))) {
			((ImageView)view.findViewById(R.id.imageWifi)).setImageResource(R.drawable.ic_action_network_wifi_off);
		} else if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.WIFI_SETTING)).equals(WifiManager.WIFI_STATE_ENABLED)){
			((ImageView)view.findViewById(R.id.imageWifi)).setImageResource(R.drawable.ic_action_network_wifi_on);
		}
		
		// bluetooth
		
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.BLUETOOTH)).equals(String.valueOf(BluetoothAdapter.STATE_OFF))) {
			((ImageView)view.findViewById(R.id.imageBluetooth)).setImageResource(R.drawable.ic_action_bluetooth_off);
		} else if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.BLUETOOTH)).equals(BluetoothAdapter.STATE_ON)) {
			((ImageView)view.findViewById(R.id.imageBluetooth)).setImageResource(R.drawable.ic_action_bluetooth_on);
		}
		
		// mobiledata
		if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.MOBILE_DATA)).equals("0")) {
			((ImageView)view.findViewById(R.id.imageMobileData)).setImageResource(R.drawable.ic_action_network_cell_off);
		} else if (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.MOBILE_DATA)).equals("1")){
			((ImageView)view.findViewById(R.id.imageMobileData)).setImageResource(R.drawable.ic_action_network_cell_on);
		}		
		
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = mLayoutInflator.inflate(R.layout.event_row, parent, false);
		return v;
	}
	
	
}

