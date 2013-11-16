package com.eventscheduler.receiver;

import com.eventscheduler.Scheduler;
import com.eventscheduler.datastore.DataSource;
import com.eventscheduler.util.MyEvent;
import com.eventscheduler.util.Util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {

	final String DEUBG_TAG = "BootReceiver";
	
	@Override
	public void onReceive(Context context, Intent in) {
		//Log.d(DEUBG_TAG, "onReceive");
		Scheduler sch = new Scheduler(context);
		MyEvent my = new MyEvent();
		DataSource ds = new DataSource(context);
		ds.open();
		Cursor c = ds.query();
		
		for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			my = Util.fillMyEvent(c);
			//Log.d(DEUBG_TAG, "id " + my.getId());
			sch.setSchedule(my);
		}
		ds.close();
		//Log.d(DEUBG_TAG, "onReceive  end");
	}

}
