package com.eventscheduler;

import java.util.Calendar;
import java.util.Date;

import com.eventscheduler.datastore.DataSource;
import com.eventscheduler.datastore.MySQLiteHelper;
import com.eventscheduler.receiver.AlarmManagerBroadcastReceiver;
import com.eventscheduler.receiver.UnscheduleBroadcastReceiver;
import com.eventscheduler.util.MyEvent;
import com.eventscheduler.util.Util;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class Scheduler {

	static final String DEBUG_TAG = "Scheduler"; 
	AlarmManager alarm;
	Context c;
	MyEvent createdEvent;
	public Scheduler(Context ct) {
		c = ct;
		alarm = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
	}

	public void setSchedule ( MyEvent my) {
		//Log.d(DEBUG_TAG, "setSchedule");

		
		Intent localIntent1 = new Intent(c, AlarmManagerBroadcastReceiver.class);
		Intent localIntent2 = new Intent(c, AlarmManagerBroadcastReceiver.class);
		
		localIntent1.putExtra("eventtime", "start").putExtra("id", my.getId());
		localIntent1.putExtra("ringerMode", my.getRingerMode()).putExtra("alarmSetting", my.getAlarmSetting());
		localIntent1.putExtra("mediaSetting", my.getMediaSetting()).putExtra("wifiSetting", my.getWifiSetting());
		localIntent1.putExtra("bluetoothSetting", my.getBluetooth()).putExtra("mobileData", my.getMobileData());
		
		localIntent2.putExtra("eventtime", "end").putExtra("id", my.getId());
		
		if (!my.isRepeating()) {
			localIntent1.setData(Uri.parse("STRT" + my.getId() + "NR"));
			PendingIntent pi2 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.set(AlarmManager.RTC, my.getStartDate().getTime(), pi2);

			localIntent2.setData(Uri.parse("STOP" + my.getId() + "NR"));
			PendingIntent spi2 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.set(AlarmManager.RTC, my.getEndDate().getTime(), spi2);

			Intent localIntent4 = new Intent(c,UnscheduleBroadcastReceiver.class);
			localIntent4.setData(Uri.parse("UNTL" + my.getId() + "NR"));
			localIntent4.putExtra("eventid", my.getId());
			PendingIntent pending = PendingIntent.getBroadcast(c, 0, localIntent4, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.set(AlarmManager.RTC, my.getEndDate().getTime(), pending);
			//Log.d(DEBUG_TAG, "untilIntent set data " + (Uri.parse("UNTL" + my.getId() + "RE")).toString());
		}

		if (my.isRepeating()) {
			if (my.isMon()) {
				localIntent1.setData(Uri.parse("STRT" + my.getId() + "MO"));
				PendingIntent pi3 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getStartDate(), Calendar.MONDAY), 
						AlarmManager.INTERVAL_DAY * 7, pi3);

				localIntent2.setData(Uri.parse("STOP" + my.getId() + "MO"));
				PendingIntent spi3 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getEndDate(), Calendar.MONDAY), 
						AlarmManager.INTERVAL_DAY * 7, spi3);

			}
			if (my.isTue()) {
				localIntent1.setData(Uri.parse("STRT" + my.getId() + "TU"));
				PendingIntent pi4 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getStartDate(), Calendar.TUESDAY), 
						AlarmManager.INTERVAL_DAY * 7, pi4);

				localIntent2.setData(Uri.parse("STOP" + my.getId() + "TU"));
				PendingIntent spi4 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getEndDate(), Calendar.TUESDAY), 
						AlarmManager.INTERVAL_DAY * 7, spi4);
			}

			if (my.isWed()) {
				localIntent1.setData(Uri.parse("STRT" + my.getId() + "WE"));
				PendingIntent pi5 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getStartDate(), Calendar.WEDNESDAY), 
						AlarmManager.INTERVAL_DAY * 7, pi5);

				localIntent2.setData(Uri.parse("STOP" + my.getId() + "WE"));
				PendingIntent spi5 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getEndDate(), Calendar.WEDNESDAY), 
						AlarmManager.INTERVAL_DAY * 7, spi5);
			}
			if (my.isThu()) {
				localIntent1.setData(Uri.parse("STRT" + my.getId() + "TH"));
				PendingIntent pi6 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getStartDate(), Calendar.THURSDAY), 
						AlarmManager.INTERVAL_DAY * 7, pi6);

				localIntent2.setData(Uri.parse("STOP" + my.getId() + "TH"));
				PendingIntent spi6 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getEndDate(), Calendar.THURSDAY), 
						AlarmManager.INTERVAL_DAY * 7, spi6);
			}
			if (my.isFri()) {
				localIntent1.setData(Uri.parse("STRT" + my.getId() + "FR"));
				PendingIntent pi7 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getStartDate(), Calendar.FRIDAY), 
						AlarmManager.INTERVAL_DAY * 7, pi7);

				localIntent2.setData(Uri.parse("STOP" + my.getId() + "FR"));
				PendingIntent spi7 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getEndDate(), Calendar.FRIDAY), 
						AlarmManager.INTERVAL_DAY * 7, spi7);
			}
			if (my.isSat()) {
				localIntent1.setData(Uri.parse("STRT" + my.getId() + "SA"));
				PendingIntent pi8 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getStartDate(), Calendar.SATURDAY), 
						AlarmManager.INTERVAL_DAY * 7, pi8);
				

				localIntent2.setData(Uri.parse("STOP" + my.getId() + "SA"));
				PendingIntent spi8 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getEndDate(), Calendar.SATURDAY), 
						AlarmManager.INTERVAL_DAY * 7, spi8);
				
			}
			if (my.isSun()) {
				localIntent1.setData(Uri.parse("STRT" + my.getId() + "SU"));
				PendingIntent pi9 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getStartDate(), Calendar.SUNDAY), 
						AlarmManager.INTERVAL_DAY * 7, pi9);

				localIntent2.setData(Uri.parse("STOP" + my.getId() + "SU"));
				PendingIntent spi9 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.setRepeating(AlarmManager.RTC, getTriggerTime(my.getEndDate(), Calendar.SUNDAY), 
						AlarmManager.INTERVAL_DAY * 7, spi9);

			}
		}
		// event is repeating and until date is specified
		if (my.isRepeating() && my.isUntilCheck()) {
			Intent localIntent3 = new Intent(c,UnscheduleBroadcastReceiver.class);
			localIntent3.setData(Uri.parse("UNTL" + my.getId() + "RE"));
			localIntent3.putExtra("eventid", my.getId());
			PendingIntent pending = PendingIntent.getBroadcast(c, 0, localIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.set(AlarmManager.RTC, my.getEventEndDate().getTime(), pending);
		}
	}

	public long getTriggerTime(Date dt, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		if (cal.get(Calendar.DAY_OF_WEEK) == day) {
			return dt.getTime();
		} 
		while(true) {
			cal.add(Calendar.DAY_OF_WEEK, 1);
			if (cal.get(Calendar.DAY_OF_WEEK) == day)
				break;
		}
		return cal.getTimeInMillis();

	}

	public void unSchedule(int position) {

		DataSource ds = new DataSource(c);
		ds.open();
		Cursor crsr = ds.query();
		
		crsr.moveToFirst();
		
		do {
			int i = Integer.parseInt(crsr.getString(crsr.getColumnIndexOrThrow(MySQLiteHelper.ID)));
			if (i == position) {
				//Log.d(DEBUG_TAG, "success i " + i );
				break;
			}
		} while(crsr.moveToNext());
		
		createdEvent = Util.fillMyEvent(crsr);
		//Log.d(DEBUG_TAG, "unSchedule myevent " + createdEvent.toString());
		Intent localIntent1 = new Intent(c, AlarmManagerBroadcastReceiver.class);
		Intent localIntent2 = new Intent(c, AlarmManagerBroadcastReceiver.class);
		Intent localIntent3 = new Intent(c, UnscheduleBroadcastReceiver.class);
	
		if (!createdEvent.isRepeating()) {
			localIntent1.setData(Uri.parse("STRT" + createdEvent.getId() + "NR"));
			PendingIntent pi2 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(pi2);

			localIntent2.setData(Uri.parse("STOP" + createdEvent.getId() + "NR"));
			PendingIntent spi2 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(spi2);
			
			localIntent3.setData(Uri.parse("UNTL" + createdEvent.getId() + "NR"));
			PendingIntent upi = PendingIntent.getBroadcast(c, 0, localIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(upi);
			
			
		}

		if (createdEvent.isRepeating()) {
			if (createdEvent.isMon()) {
				localIntent1.setData(Uri.parse("STRT" + createdEvent.getId() + "MO"));
				PendingIntent pi3 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(pi3);

				localIntent2.setData(Uri.parse("STOP" + createdEvent.getId() + "MO"));
				PendingIntent spi3 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(spi3);

			}
			if (createdEvent.isTue()) {
				localIntent1.setData(Uri.parse("STRT" + createdEvent.getId() + "TU"));
				PendingIntent pi4 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(pi4);

				localIntent2.setData(Uri.parse("STOP" + createdEvent.getId() + "TU"));
				PendingIntent spi4 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(spi4);
			}

			if (createdEvent.isWed()) {
				localIntent1.setData(Uri.parse("STRT" + createdEvent.getId() + "WE"));
				PendingIntent pi5 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(pi5);

				localIntent2.setData(Uri.parse("STOP" + createdEvent.getId() + "WE"));
				PendingIntent spi5 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(spi5);
			}

			if (createdEvent.isThu()) {
				localIntent1.setData(Uri.parse("STRT" + createdEvent.getId() + "TH"));
				PendingIntent pi6 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(pi6);

				localIntent2.setData(Uri.parse("STOP" + createdEvent.getId() + "TH"));
				PendingIntent spi6 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(spi6);
			}
			if (createdEvent.isFri()) {
				localIntent1.setData(Uri.parse("STRT" + createdEvent.getId() + "FR"));
				PendingIntent pi7 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(pi7);

				localIntent2.setData(Uri.parse("STOP" + createdEvent.getId() + "FR"));
				PendingIntent spi7 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(spi7);
			}
			if (createdEvent.isSat()) {
				
				localIntent1.setData(Uri.parse("STRT" + createdEvent.getId() + "SA"));
				PendingIntent pi8 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				pi8.cancel();
				alarm.cancel(pi8);
				
				localIntent2.setData(Uri.parse("STOP" + createdEvent.getId() + "SA"));
				PendingIntent spi8 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				spi8.cancel();
				alarm.cancel(spi8);
				
			}
			if (createdEvent.isSun()) {
				localIntent1.setData(Uri.parse("STRT" + createdEvent.getId() + "SU"));
				PendingIntent pi9 = PendingIntent.getBroadcast(c, 0, localIntent1, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(pi9);

				localIntent2.setData(Uri.parse("STOP" + createdEvent.getId() + "SU"));
				PendingIntent spi9 = PendingIntent.getBroadcast(c, 0, localIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
				alarm.cancel(spi9);
				
			}
		}
		if (createdEvent.isRepeating() && createdEvent.isUntilCheck()) {
			localIntent3.setData(Uri.parse("UNTL" + createdEvent.getId() + "RE"));
			PendingIntent pending = PendingIntent.getBroadcast(c, 0, localIntent3, PendingIntent.FLAG_UPDATE_CURRENT);
			alarm.cancel(pending);
		}
		
		ds.delete(position);
		ds.close();
		//Log.d(DEBUG_TAG, "usche deleted");

	}



}
