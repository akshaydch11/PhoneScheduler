package com.eventscheduler.receiver;


import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.app.AlarmManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

	static final String DEBUG_TAG = "AlarmManagerBroadcastReceiver";
	public static final String EVENT_PREFS = "Events";
	AlarmManager alarm ;
	Context c;
	int ringMode, alarmSetting, mediaSetting, wifiSetting,blueSetting,mobileData;
	public AlarmManagerBroadcastReceiver() {

	}

	public AlarmManagerBroadcastReceiver(Context context) {
		c = context;
		alarm = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
	}

	private void setMobileDataEnabled(Context context, boolean enabled) {
		//Log.d(DEBUG_TAG, "setMobileDataEnabled enabled " + enabled);
		try {
			final ConnectivityManager conman = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			final Class conmanClass = Class.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);
			final Object iConnectivityManager = iConnectivityManagerField.get(conman);
			final Class iConnectivityManagerClass = Class.forName(iConnectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);
			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "setMobileData error");
			e.printStackTrace();
		}
	}

	@Override
	public void onReceive(Context c, Intent in) {
		//Log.d(DEBUG_TAG, "onReceive");
		AudioManager adm = (AudioManager) c.getSystemService(Context.AUDIO_SERVICE);
		WifiManager wifi = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
		BluetoothAdapter blue = BluetoothAdapter.getDefaultAdapter();
		TelephonyManager teleManager = (TelephonyManager) c.getSystemService(Context.TELEPHONY_SERVICE);
		SharedPreferences prefs = c.getSharedPreferences(EVENT_PREFS, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		StringBuilder sb = new StringBuilder();

		if (in.getExtras().getString("eventtime").equals("start")) {
			ringMode = in.getExtras().getInt("ringerMode");
			alarmSetting = in.getExtras().getInt("alarmSetting");
			mediaSetting = in.getExtras().getInt("mediaSetting");
			wifiSetting = in.getExtras().getInt("wifiSetting");
			blueSetting = in.getExtras().getInt("bluetoothSetting");
			mobileData = in.getExtras().getInt("mobileData");
			
			int mobileDataTemp = -1;
			if (teleManager.getDataState() == 2 || teleManager.getDataState() == 1) 
				mobileDataTemp = 1;
			else if (teleManager.getDataState() == 0 || teleManager.getDataState() == 3) 
				mobileDataTemp = 0;
			
			sb.append(adm.getRingerMode() + ":" + adm.getStreamVolume(AudioManager.STREAM_ALARM) + ":");
			sb.append(adm.getStreamVolume(AudioManager.STREAM_MUSIC) + ":" + wifi.getWifiState() + ":");
			sb.append(blue.getState() + ":");
			sb.append(mobileDataTemp);
			editor.putString(String.valueOf(in.getExtras().getInt("id")), sb.toString());
			editor.commit();
			//Log.d(DEBUG_TAG, "sb : " + sb.toString());
			
		}
		if (in.getExtras().getString("eventtime").equals("end")) {
			String temp = prefs.getString(String.valueOf(in.getExtras().getInt("id")), "");
			String[] settings = temp.split(":");
			//Log.d(DEBUG_TAG, "id "+ String.valueOf(in.getExtras().getInt("id")) + " settings " + temp);
			
			ringMode = Integer.parseInt(settings[0]);
			alarmSetting = Integer.parseInt(settings[1]);
			mediaSetting = Integer.parseInt(settings[2]);
			wifiSetting = Integer.parseInt(settings[3]);
			blueSetting = Integer.parseInt(settings[4]);
			mobileData = Integer.parseInt(settings[4]);
			
			editor.remove(String.valueOf(in.getExtras().getInt("id")));
			editor.commit();
		}
		
		/*Log.d(DEBUG_TAG, "onReceive ring " + ringMode + " alarmSetting " + alarmSetting 
				+ " mediaSetting " + mediaSetting + " wifi " + wifiSetting
				+ " blueSetting " + blueSetting );
		 */
		// setting ringer and notification
		if (ringMode == AudioManager.RINGER_MODE_SILENT)
			adm.setRingerMode(AudioManager.RINGER_MODE_SILENT);
		else if (ringMode == AudioManager.RINGER_MODE_VIBRATE)
			adm.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
		else if (ringMode == AudioManager.RINGER_MODE_NORMAL)
			adm.setRingerMode(AudioManager.RINGER_MODE_NORMAL);

		// setting alarm volume in system
		if (alarmSetting == 0) {
			adm.setStreamVolume(AudioManager.STREAM_ALARM, alarmSetting,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		} else {
			adm.setStreamVolume(AudioManager.STREAM_ALARM, alarmSetting, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		}

		// setting media volume in system
		if (mediaSetting == 0) {
			adm.setStreamVolume(AudioManager.STREAM_MUSIC, mediaSetting,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		} else {
			adm.setStreamVolume(AudioManager.STREAM_MUSIC, mediaSetting, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
		}

		// set wifi ON/OFF
		if(wifiSetting == WifiManager.WIFI_STATE_ENABLED) {
			wifi.setWifiEnabled(true);
		}
		if(wifiSetting == WifiManager.WIFI_STATE_DISABLED) {
			wifi.setWifiEnabled(false);
		}

		//set bluetooth ON/OFF
		if (blueSetting == BluetoothAdapter.STATE_OFF) {
			blue.disable();
		} 
		if (blueSetting == BluetoothAdapter.STATE_ON) {
			blue.enable();
		}

		// set mobile data ON/OFF
		boolean bool = (mobileData == 1) ? true :false;
		setMobileDataEnabled(c, bool);

	}


}
