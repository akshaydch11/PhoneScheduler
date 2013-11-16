package com.eventscheduler.util;

import java.util.Date;

public class MyEvent {
	private int id;
	private String eventName;

	private boolean isRepeating;
	private boolean isScheduled = false;
	private boolean mon;
	private boolean tue;
	private boolean wed;
	private boolean thu;
	private boolean fri;
	private boolean sat;
	private boolean sun;
	

	private boolean untilCheck;
	private Date startDate;
	private Date endDate;
	private Date eventEndDate;
	
	private int ringerMode;
	private int alarmSetting;
	private int mediaSetting;
	private int wifiSetting;
	private int bluetooth;
	private int mobileData;
	
	
	public int getMobileData() {
		return mobileData;
	}
	public void setMobileData(int mobileData) {
		this.mobileData = mobileData;
	}
	public int getBluetooth() {
		return bluetooth;
	}
	public void setBluetooth(int bluetooth) {
		this.bluetooth = bluetooth;
	}
	public int getWifiSetting() {
		return wifiSetting;
	}
	public void setWifiSetting(int wifiSetting) {
		this.wifiSetting = wifiSetting;
	}
	public int getMediaSetting() {
		return mediaSetting;
	}
	public void setMediaSetting(int mediaSetting) {
		this.mediaSetting = mediaSetting;
	}
	public int getAlarmSetting() {
		return alarmSetting;
	}
	public void setAlarmSetting(int alarmSetting) {
		this.alarmSetting = alarmSetting;
	}
	public int getRingerMode() {
		return ringerMode;
	}
	public void setRingerMode(int ringerMode) {
		this.ringerMode = ringerMode;
	}
	public int getId() {
		return id;
	}
	public String getEventName() {
		return eventName;
	}
	public boolean isRepeating() {
		return isRepeating;
	}
	public boolean isScheduled() {
		return isScheduled;
	}
	public boolean isMon() {
		return mon;
	}
	public boolean isTue() {
		return tue;
	}
	public boolean isWed() {
		return wed;
	}
	public boolean isThu() {
		return thu;
	}
	public boolean isFri() {
		return fri;
	}
	public boolean isSat() {
		return sat;
	}
	public boolean isSun() {
		return sun;
	}
	
	public boolean isUntilCheck() {
		return untilCheck;
	}

	public Date getStartDate() {
		return startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public Date getEventEndDate() {
		return eventEndDate;
	}
	
	
	/*
	 *  Setter methods 
	 * 
	 */
	public void setId(int id) {
		this.id = id;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public void setRepeating(boolean isRepeating) {
		this.isRepeating = isRepeating;
	}
	public void setScheduled(boolean isScheduled) {
		this.isScheduled = isScheduled;
	}
	public void setMon(boolean mon) {
		this.mon = mon;
	}
	public void setTue(boolean tue) {
		this.tue = tue;
	}
	public void setWed(boolean wed) {
		this.wed = wed;
	}
	public void setThu(boolean thu) {
		this.thu = thu;
	}
	public void setFri(boolean fri) {
		this.fri = fri;
	}
	public void setSat(boolean sat) {
		this.sat = sat;
	}
	public void setSun(boolean sun) {
		this.sun = sun;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public void setEventEndDate(Date eventEndDate) {
		this.eventEndDate = eventEndDate;
	}
	public void setUntilCheck(boolean untilCheck) {
		this.untilCheck = untilCheck;
	}
	@Override
	public String toString() {
		return "MyEvent [id="+ id + ", eventName=" + eventName + ", isRepeating="
				+ isRepeating + ", isScheduled=" + isScheduled + ", mon=" + mon
				+ ", tue=" + tue + ", wed=" + wed + ", thu=" + thu + ", fri="
				+ fri + ", sat=" + sat + ", sun=" + sun + ", untilCheck="
				+ untilCheck + ", startDate=" + startDate + ", endDate="
				+ endDate + ", eventEndDate=" + eventEndDate + ", ringerMode=" + ringerMode 
				+ ", alarmSetting=" + alarmSetting + ", mediaSetting=" + mediaSetting 
				+ ", wifiSetting=" + wifiSetting + ", bluetooth=" + bluetooth 
				+ ", mobileData=" + mobileData + "]";
	}
	
	

}
