package com.eventscheduler.receiver;

import com.eventscheduler.Scheduler;
import com.eventscheduler.datastore.DataSource;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class UnscheduleBroadcastReceiver extends BroadcastReceiver {
	static final String DEBUG_TAG = "UnscheduleBroadcastReceiver";
	
	@Override
	public void onReceive(Context c, Intent in) {
		
		int position = in.getExtras().getInt("eventid");;
		//Log.d(DEBUG_TAG, "onReceive ID " + position );
		Scheduler sch = new Scheduler(c);
		sch.unSchedule(position);
		DataSource ds = new DataSource(c);
		ds.open();
	}

}
