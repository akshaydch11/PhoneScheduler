package com.eventscheduler;


import com.eventscheduler.datastore.DataSource;
import com.eventscheduler.datastore.MySQLiteHelper;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

public class ToggleTabFragment extends Fragment {
	static final String DEBUG_TAG = "ToggleTabFragment";

	RadioGroup ringerRadio;
	Button ringerToggleBtn;
	ToggleButton  alarmToggleBtn,mediaToggleBtn;
	ToggleButton wifiToggleBtn, bluetoothToggleBtn, mobileDataBtn;
	int ringerModeSelected, alarmVolume, mediaVolume;
	int wifiSetting, bluetoothSetting, mobileDataSetting;
	AudioManager adm;
	View view;
	boolean addNewEvent;
	int position;
	boolean[] checkedRinger;
	private CharSequence[] ringerSilent = { "Silent", "Vibrate", "Normal" };
	
	public ToggleTabFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//Log.d(DEBUG_TAG, "onCreateView()");
		view = inflater.inflate(R.layout.toggle_event,
				container, false);
		addNewEvent = getActivity().getIntent().getExtras().getBoolean("addNewEvent");
		position = getActivity().getIntent().getExtras().getInt("clickedEvent");
		initComponents();
		if(!addNewEvent) 
			setAll(position);
		return view;
	}

	private void initComponents() {
		adm = (AudioManager)(getActivity().getApplicationContext().getSystemService(Context.AUDIO_SERVICE));
		ringerModeSelected = AudioManager.RINGER_MODE_NORMAL;
		ringerToggleBtn = (Button) view.findViewById(R.id.ringerToggle);
		ringerToggleBtn.setText("Ringer Mode\n" + ringerSilent[ringerModeSelected]);
		ringerToggleBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Choose Ringer Mode");
				builder.setItems(ringerSilent, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//Log.d(DEBUG_TAG, "which " + which);
						ringerModeSelected = which;
						ringerToggleBtn.setText("Ringer Mode\n" + ringerSilent[ringerModeSelected]);
					}
				});
				builder.show();
				//Log.d(DEBUG_TAG, "ringerMode selected " + ringerModeSelected);
			}
		});
		alarmToggleBtn = (ToggleButton) view.findViewById(R.id.alarmToggle);
		mediaToggleBtn = (ToggleButton) view.findViewById(R.id.mediaToggle);
		
		alarmVolume = 5;//adm.getStreamVolume(AudioManager.STREAM_ALARM);
		alarmToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					//Log.d(DEBUG_TAG, "alarmToggleBtn isChecked");
					alarmVolume = 0;
				} else {
					alarmVolume = 5;//adm.getStreamVolume(AudioManager.STREAM_ALARM);
					//Log.d(DEBUG_TAG, "alarmToggleBtn No alarmVol " + alarmVolume);
				}
			}
		});
		
		mediaVolume = 10;//adm.getStreamVolume(AudioManager.STREAM_MUSIC);
		mediaToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					//Log.d(DEBUG_TAG, "mediaToggleBtn isChecked");
					mediaVolume = 0;
				} else {
					mediaVolume = 10;//adm.getStreamVolume(AudioManager.STREAM_MUSIC);
					//Log.d(DEBUG_TAG, "mediaToggleBtn No mediaVolume " + mediaVolume);
				}
			}
		});
		
		//WifiManager wifiManager = (WifiManager)getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		wifiSetting = WifiManager.WIFI_STATE_ENABLED;
		wifiToggleBtn = (ToggleButton) view.findViewById(R.id.wifiToggle);
		wifiToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					wifiSetting = WifiManager.WIFI_STATE_DISABLED;
					//Log.d(DEBUG_TAG, "wifiToggleBtn isChecked " + wifiSetting);
				} else {
					wifiSetting = WifiManager.WIFI_STATE_ENABLED;
					//Log.d(DEBUG_TAG, "wifiToggleBtn No wifiSetting " + wifiSetting);
				}
				
			}
		});
		
		//BluetoothAdapter blue = BluetoothAdapter.getDefaultAdapter();
		bluetoothSetting = BluetoothAdapter.STATE_ON;
		bluetoothToggleBtn = (ToggleButton) view.findViewById(R.id.bluetoothToggle);
		bluetoothToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					bluetoothSetting = BluetoothAdapter.STATE_OFF;
					//Log.d(DEBUG_TAG, "bluetoothToggleBtn isChecked");
				} else {
					bluetoothSetting = BluetoothAdapter.STATE_ON;
					//Log.d(DEBUG_TAG, "bluetoothToggleBtn No " + bluetoothSetting);
				}
			}
		});
		
		mobileDataSetting = 1;
		mobileDataBtn = (ToggleButton) view.findViewById(R.id.mobileDataToggle);
		mobileDataBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					mobileDataSetting = 0;
					//Log.d(DEBUG_TAG, "mobileDataBtn isChecked");
				} else {
					mobileDataSetting = 1;
					//Log.d(DEBUG_TAG, "mobileDataBtn No " + mobileDataSetting);
				}
			}
		});
		
	}
	
	private void setAll(int position) {
		//Log.d(DEBUG_TAG, "get position " + position);
		DataSource ds = new DataSource(getActivity().getApplicationContext());
		ds.open();
		Cursor c = ds.query();

		if (c.moveToPosition(position)) {
			ringerModeSelected = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.RINGER_MODE)));
			ringerToggleBtn.setText("Ringer Mode \n" + ringerSilent[ringerModeSelected]);

			alarmVolume = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.ALARM_SETTING)));
			boolean checked =  (alarmVolume == 0)?true:false;
			alarmToggleBtn.setChecked(checked);
			
			mediaVolume = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.MEDIA_SETTING))); 
			checked =  (mediaVolume == 0)?true:false;
			mediaToggleBtn.setChecked(checked);
			
			wifiSetting = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.WIFI_SETTING)));
			checked =  (wifiSetting == WifiManager.WIFI_STATE_DISABLED)?true:false;
			wifiToggleBtn.setChecked(checked);
			
			bluetoothSetting = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.BLUETOOTH)));
			checked =  (bluetoothSetting == BluetoothAdapter.STATE_OFF)?true:false;
			bluetoothToggleBtn.setChecked(checked);
			
			mobileDataSetting = Integer.parseInt(c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.MOBILE_DATA)));
			checked =  (mobileDataSetting == 0)?true:false;
			mobileDataBtn.setChecked(checked);
			
		}
		
		ds.open();
	}
	
	
}