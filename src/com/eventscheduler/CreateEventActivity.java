package com.eventscheduler;

import java.util.Calendar;

import com.eventscheduler.datastore.DataSource;
import com.eventscheduler.util.MyEvent;
import com.eventscheduler.util.Util;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class CreateEventActivity extends FragmentActivity implements
		ActionBar.TabListener {
	static final String DEBUG_TAG = "CreateEventActivity";
	GeneralTabFragment generalTabFragment;
	ToggleTabFragment toggleTabFragment;
	MyEvent createdEvent;
	public static int POSN;
	int position ;
	boolean addNewEvent;
	public static final String ACTION_REFRESH_EVENT_LIST = "com.akshay.calendarsample.REFRESH_EVENT_LIST";
	Cursor cursor;
	boolean validset = false ;
	boolean selUnsel = true;

	/**
	 * 
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	public int getPosition() {
		return position;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Log.d(DEBUG_TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_event);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		position = getIntent().getExtras().getInt("clickedEvent");
		addNewEvent = getIntent().getExtras().getBoolean("addNewEvent");
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager(),getApplicationContext());

				
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
		.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				actionBar.setSelectedNavigationItem(position);
			}
		});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		//Log.d(DEBUG_TAG, "onCreate END");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()) {

		case R.id.item_aboutThisApp: 
			startActivity(new Intent(this, AboutTheApp.class));
			return true;
		case R.id.item_rateThisApp: 
			Uri uri = Uri.parse("market://details?id=" + getPackageName());
			Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
			try {
				startActivity(goToMarket);
			} catch (ActivityNotFoundException e) {
				Toast.makeText(this, "Couldn't launch the market", Toast.LENGTH_LONG).show();
			}
			return true;
		default:
			return false;
		}
	}



	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		//Log.d(DEBUG_TAG, "onTabSelected " + tab.getPosition() + " " + selUnsel);
		validset = false;
		if(selUnsel)
			mViewPager.setCurrentItem(tab.getPosition());
		else {
			mViewPager.setCurrentItem(tab.getPosition() - 1);
			selUnsel = false;
		}



	}


	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		//Log.d(DEBUG_TAG, "onTabUnselected " + + tab.getPosition());
		if (tab.getPosition() == 0 && validset == false) {
			selUnsel = validateGfragment();	
		}

	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		//Log.d(DEBUG_TAG, "onTabReselected");
	}

	public boolean validateGfragment() {
		populateGTabEvent();
		if(createdEvent.getEventName().equals("")) {
			showErrorDialog("Oops.. Event name is missing");
			return false;
		}

		if (createdEvent.getStartDate().getTime() <= Calendar.getInstance().getTimeInMillis()) {
			showErrorDialog("Oops.. Start time/date has passed");
			return false;
		}
		if (createdEvent.getEndDate().getTime() <= createdEvent.getStartDate().getTime()) {
			showErrorDialog("Oops.. End time is smaller than Start time");
			return false;
		}
		return true;
	}

	public void onNextButtonClick (View v) {
		//Log.d(DEBUG_TAG, "onNextButtonClick()");
		if (validateGfragment() && validset == false) {
			validset = true;
			selUnsel = true;
			mViewPager.setCurrentItem(1);
		}

	}

	public void onBackButtonClick (View v) {
		//Log.d(DEBUG_TAG, "onBackButtonClick()");
		mViewPager.setCurrentItem(0);
		
	}
	
	public void onCancelButtonClick (View v) {
		//Log.d(DEBUG_TAG, "onCancelButtonClick()");
		finish();
		
	}
	
	public void onFinishButtonClick (View v) {
		//Log.d(DEBUG_TAG, "onFinishButtonClick()");
		populateTTabEvent();
		DataSource ds = new DataSource(getApplicationContext());
		ContentValues cv = DataSource.eventToValues(createdEvent);
		Scheduler sch = new Scheduler(getApplicationContext());
		int pos = 0;
		ds.open();
		if (!addNewEvent) { 
			ds.update(cv,generalTabFragment.getRowId());
			pos = generalTabFragment.getRowId();
		} else {
			pos = ds.insert(cv);
		}
		ds.close();
		createdEvent.setId(pos);
		sch.setSchedule(createdEvent);
		//startActivity(new Intent(this, AddEventActivity.class));
		finish();
	}
	
	public void showErrorDialog(String errmsg) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Error");
		builder.setMessage(errmsg);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		});
		builder.show();
		//Log.d(DEBUG_TAG, "showErrorDialog");
	}
	
	private void populateGTabEvent () {
		createdEvent = new MyEvent();
		generalTabFragment = (GeneralTabFragment) mSectionsPagerAdapter.getItem(0);

		
		Editable text = ((EditText)findViewById(R.id.eventLable)).getText();
		createdEvent.setEventName(text.toString());
		
		String time = (String) generalTabFragment.startTimeBtn.getText();
		String date = (String) generalTabFragment.eventDateBtn.getText();

		createdEvent.setStartDate(Util.combineTimeDate(time, date));
		
		
		time = (String) generalTabFragment.stopTimeBtn.getText();
		date = (String) generalTabFragment.eventDateBtn.getText();

		createdEvent.setEndDate(Util.combineTimeDate(time, date));
		
		createdEvent.setUntilCheck(generalTabFragment.untilChkBox.isChecked());	// R.id.untilChkbox
		
		String endDt = (String) generalTabFragment.eventEndDateBtn.getText();
		
		if (endDt.equals("") || endDt.equals(date)) {
			createdEvent.setEventEndDate(Util.combineTimeDate(time, date));
		} else {
			createdEvent.setEventEndDate(Util.combineTimeDate("00:00 AM", endDt));
		}
		
		createdEvent.setRepeating(generalTabFragment.isRepeat);
		createdEvent.setMon(generalTabFragment.checkedDays[0]);
		createdEvent.setTue(generalTabFragment.checkedDays[1]);
		createdEvent.setWed(generalTabFragment.checkedDays[2]);
		createdEvent.setThu(generalTabFragment.checkedDays[3]);
		createdEvent.setFri(generalTabFragment.checkedDays[4]);
		createdEvent.setSat(generalTabFragment.checkedDays[5]);
		createdEvent.setSun(generalTabFragment.checkedDays[6]);
	}
	public void populateTTabEvent () {
		// add toggle tab variables in myevent
		toggleTabFragment = (ToggleTabFragment) mSectionsPagerAdapter.getItem(1);
		
		createdEvent.setRingerMode(toggleTabFragment.ringerModeSelected);
		createdEvent.setAlarmSetting(toggleTabFragment.alarmVolume);
		createdEvent.setMediaSetting(toggleTabFragment.mediaVolume);
		
		createdEvent.setWifiSetting(toggleTabFragment.wifiSetting);
		createdEvent.setBluetooth(toggleTabFragment.bluetoothSetting);
		createdEvent.setMobileData(toggleTabFragment.mobileDataSetting);
		//Log.d(DEBUG_TAG, "populateEvent - " +createdEvent.toString() );
	}
}
