package com.eventscheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


import com.eventscheduler.datastore.DataSource;
import com.eventscheduler.datastore.MySQLiteHelper;
import com.eventscheduler.util.Util;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class GeneralTabFragment extends Fragment {
	static final String DEBUG_TAG = "GeneralTabFragment";
	EditText eventName;
	Calendar startDate;
	Calendar endDate;
	Calendar eventEndDate;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.US); 
	private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US); 
	Button startTimeBtn;
	Button stopTimeBtn;
	Button eventDateBtn;
	Button eventEndDateBtn;
	Button repeatDaysBtn;
	CheckBox untilChkBox;
	int rowId;
	TextView eventEndText;
	boolean addNewEvent;
	private View view;
	boolean[] checkedDays;
	boolean isRepeat = false;

	int position;

	// context.gettext and specify array in arrrays.xml
	private CharSequence[] daysOfWeek = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
	private List<CharSequence> mSelectedItems = new ArrayList<CharSequence>();

	public GeneralTabFragment() {
		//Log.d(DEBUG_TAG, "GeneralTabFragment");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//Log.d(DEBUG_TAG, "onCreateView()");
		view = inflater.inflate(R.layout.general_tab_create,
				container, false);
		// put init in activity created
		addNewEvent = getActivity().getIntent().getExtras().getBoolean("addNewEvent");
		position = getActivity().getIntent().getExtras().getInt("clickedEvent");
		initComponents();
		if (!addNewEvent)
			setAll(position);

		return view;
	}

	public void initComponents() {
		//Log.d(DEBUG_TAG, "initComponents");
		eventName = (EditText) view.findViewById(R.id.eventLable);
		startDate = Calendar.getInstance();
		endDate = Calendar.getInstance();
		eventEndDate = Calendar.getInstance();
		checkedDays = new boolean[daysOfWeek.length];
		untilChkBox = (CheckBox) view.findViewById(R.id.untilChkbox);
		untilChkBox.setOnClickListener(new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				//Log.d(DEBUG_TAG, "checkbox onClick " + ((CheckBox)v).isChecked());
				if(((CheckBox)v).isChecked()) {
					eventEndText.setVisibility(View.VISIBLE);
					eventEndDateBtn.setVisibility(View.VISIBLE);

				} else {
					eventEndText.setVisibility(View.INVISIBLE);
					eventEndDateBtn.setVisibility(View.INVISIBLE);

				}
			}

		});
		startTimeBtn = (Button) view.findViewById(R.id.addEventActivity);
		startTimeBtn.setText(timeFormat.format(startDate.getTime()));
		startTimeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onStartTimeClick(v);
			}
		});

		stopTimeBtn = (Button) view.findViewById(R.id.stopTime);
		stopTimeBtn.setText(timeFormat.format(endDate.getTime()));
		stopTimeBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onStopTimeClick(v);
			}
		});

		eventDateBtn = (Button) view.findViewById(R.id.eventDate);
		eventDateBtn.setText(dateFormat.format(startDate.getTime()));
		eventDateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onEventDateClick(v);
			}
		});

		eventEndText = (TextView) view.findViewById(R.id.eventEndTextView);
		eventEndDateBtn = (Button) view.findViewById(R.id.eventEndDate);
		eventEndDateBtn.setText(dateFormat.format(eventEndDate.getTime()));
		untilChkBox.setVisibility(View.INVISIBLE);
		eventEndText.setVisibility(View.INVISIBLE);
		eventEndDateBtn.setVisibility(View.INVISIBLE);

		eventEndDateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onEventEndDateClick(v);
			}
		});	

		repeatDaysBtn = (Button) view.findViewById(R.id.repeatDays);
		repeatDaysBtn.setText("No Repeat Days");
		repeatDaysBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDaysSelectionDialog(v);
			}
		});

	}

	public void showDaysSelectionDialog(View v) {
		//Log.d(DEBUG_TAG, "onCreateDialog()");

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.repeating_days)
		.setMultiChoiceItems(daysOfWeek, checkedDays,
				new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {
				if (isChecked) {
					mSelectedItems.add(daysOfWeek[which]);
					checkedDays[which] = mSelectedItems.contains(daysOfWeek[which]);
				} else { 
					mSelectedItems.remove(daysOfWeek[which]);
					checkedDays[which] = mSelectedItems.contains(daysOfWeek[which]);
				}

				StringBuilder stringBuilder = new StringBuilder();

				for(CharSequence days : mSelectedItems) {
					days = days.subSequence(0, 3);
					stringBuilder.append(days + " ");
				}

				repeatDaysBtn.setText("");
				if (stringBuilder.toString() == "") {
					repeatDaysBtn.setText("No Repeat Days");
					isRepeat = false;
					untilChkBox.setVisibility(View.INVISIBLE);
				} else {
					repeatDaysBtn.setText(stringBuilder.toString());
					isRepeat = true;
					untilChkBox.setVisibility(View.VISIBLE);
				}

			}
		});

		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				//Log.d(DEBUG_TAG, "setPositiveButton() :" + temp );
			}

		});
		builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				//Log.d(DEBUG_TAG, "setNegativeButton()");
			}
		});

		builder.show();
	}

	public void onStartTimeClick (View v) {
		//Log.d(DEBUG_TAG, "onStartTimeClick()");
		TimePickerDialog tpd = new TimePickerDialog(getActivity(), new StartTimeListener(), 
				startDate.get(Calendar.HOUR_OF_DAY), startDate.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
		tpd.show();
	}

	public void onStopTimeClick (View v) {
		//Log.d(DEBUG_TAG, "onStopTimeClick()");
		TimePickerDialog tpd = new TimePickerDialog(getActivity(), new StopTimeListener(), 
				endDate.get(Calendar.HOUR_OF_DAY), endDate.get(Calendar.MINUTE), DateFormat.is24HourFormat(getActivity()));
		tpd.show();	
	}

	public void onEventDateClick (View v) {
		//Log.d(DEBUG_TAG, "onEventDateClick()");
		DatePickerDialog dpd = new DatePickerDialog(getActivity(), DialogFragment.STYLE_NORMAL, 
				new EventDateListener(), startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), 
				startDate.get(Calendar.DAY_OF_MONTH));
		dpd.show();
	}


	public void onEventEndDateClick(View v) {
		//Log.d(DEBUG_TAG, "onEventEndDateClick()");
		DatePickerDialog dpd = new DatePickerDialog(getActivity(), DialogFragment.STYLE_NORMAL, 
				new EventEndDateListener(), eventEndDate.get(Calendar.YEAR), eventEndDate.get(Calendar.MONTH), 
				eventEndDate.get(Calendar.DAY_OF_MONTH));
		dpd.show();
	}

	private class StartTimeListener implements TimePickerDialog.OnTimeSetListener {

		// TO-DO pass button and date as constructor params
		// to make more generalize

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			//Log.d(DEBUG_TAG , "StartTimeListener onTimeSet()");
			startDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
			startDate.set(Calendar.MINUTE, minute);
			startTimeBtn.setText(timeFormat.format(startDate.getTime()));
		}
	}

	private class StopTimeListener implements TimePickerDialog.OnTimeSetListener {

		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			//Log.d(DEBUG_TAG , "StopTimeListener onTimeSet()");
			endDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
			endDate.set(Calendar.MINUTE, minute);
			stopTimeBtn.setText(timeFormat.format(endDate.getTime()));	
		}
	}

	private class EventDateListener implements DatePickerDialog.OnDateSetListener {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			startDate.set(Calendar.YEAR,year);
			startDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			startDate.set(Calendar.MONTH, monthOfYear);
			eventDateBtn.setText(dateFormat.format(startDate.getTime()));
		}
	}

	private class EventEndDateListener implements DatePickerDialog.OnDateSetListener {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			eventEndDate.set(Calendar.YEAR,year);
			eventEndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			eventEndDate.set(Calendar.MONTH, monthOfYear);
			eventEndDateBtn.setText(dateFormat.format(eventEndDate.getTime()));
		}
	}

	public int getRowId() {
		return rowId;
	}


	private void setAll (int position) {
		//Log.d(DEBUG_TAG, "get position " + position);
		DataSource ds = new DataSource(getActivity().getApplicationContext());
		ds.open();
		Cursor c = ds.query();

		if (c.moveToPosition(position)) {
			String id = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.ID));
			rowId = Integer.parseInt(id);
			String check = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.IS_EVENT_END_DATE));
			eventName.setText(c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.EVENT_NAME)));

			String time = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.START_TIME));
			String dt = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.START_DATE));
			startTimeBtn.setText(time);
			startDate.setTime(Util.combineTimeDate(time, dt));

			time = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.END_TIME));
			endDate.setTime(Util.combineTimeDate(time, dt));
			stopTimeBtn.setText(time);

			eventDateBtn.setText(c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.START_DATE)));

			boolean checkStat =  (check.equals("1"))? true:false; 
			untilChkBox.setChecked(checkStat);
			if(untilChkBox.isChecked()) {
				eventEndText.setVisibility(View.VISIBLE);
				eventEndDateBtn.setVisibility(View.VISIBLE);
			} else {
				eventEndText.setVisibility(View.INVISIBLE);
				eventEndDateBtn.setVisibility(View.INVISIBLE);
			}
			dt = c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.EVENT_END_DATE));
			eventEndDate.setTime(Util.combineTimeDate("00:00 AM", dt));
			eventEndDateBtn.setText(dt);
			if ((Util.daysToString(c)).equals("No Repeat Days")) {
				untilChkBox.setVisibility(View.INVISIBLE);
			} else {
				untilChkBox.setVisibility(View.VISIBLE);
			}
			repeatDaysBtn.setText(Util.daysToString(c));
			checkedDays[0] = (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.MONDAY))).equals("1") ? true :false;
			checkedDays[1] = (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.TUESDAY))).equals("1") ? true :false;
			checkedDays[2] = (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.WEDNESDAY))).equals("1") ? true :false;
			checkedDays[3] = (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.THURSDAY))).equals("1") ? true :false;
			checkedDays[4] = (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.FRIDAY))).equals("1") ? true :false;
			checkedDays[5] = (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.SATURDAY))).equals("1") ? true :false;
			checkedDays[6] = (c.getString(c.getColumnIndexOrThrow(MySQLiteHelper.SUNDAY))).equals("1") ? true :false;

		}
		ds.close();
		//Log.d(DEBUG_TAG, "get END ");
	}


}
