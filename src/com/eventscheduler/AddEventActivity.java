package com.eventscheduler;


import com.eventscheduler.datastore.DataSource;
import com.eventscheduler.datastore.MySQLiteHelper;
import com.eventscheduler.receiver.AlarmManagerBroadcastReceiver;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class AddEventActivity extends ListActivity {
	static final String DEBUG_TAG = "AddEventActivity";
	Cursor cursor;
	MyAdapter myAdapter;
	DataSource ds;
	Activity act;
	Scheduler sch;


	@Override
	protected void onStart() {
		super.onStart();
		//Log.d(DEBUG_TAG, "onStart()");
		refreshList();
	}

	@Override
	protected void onStop() {
		super.onStop();
		//Log.d(DEBUG_TAG, "onStop()");
		ds.close();
	}

	@Override
	protected void onDestroy() {
		super.onStop();
		//Log.d(DEBUG_TAG, "onDestroy()");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Log.d(DEBUG_TAG, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);


		TextView lEmpty =new TextView(AddEventActivity.this);
		lEmpty.setText("No scheduled event found");
		lEmpty.setY(120);
		getListView().setEmptyView(lEmpty);
		lEmpty.setVisibility(View.GONE);
		((ViewGroup)getListView().getParent()).addView(lEmpty);

		ds = new DataSource(getApplicationContext());
		ds.open();
		cursor = ds.query();
		myAdapter = new MyAdapter(this, cursor, 1);
		setListAdapter(myAdapter);
		this.getListView().setLongClickable(true);
		act = this;
		sch = new Scheduler(getApplicationContext());
		this.getListView().setOnItemLongClickListener(longListen);
		ds.close();
	}

	OnItemLongClickListener longListen = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> adapterView, View v,
				final int position, long id) {

			Cursor cr = (Cursor) adapterView.getItemAtPosition(position);
			final String str = cr.getString(cr.getColumnIndex(MySQLiteHelper.ID));
			//Log.d(DEBUG_TAG, "setOnItemLongClickListener pos " + position + " id " + id + " str " + str);

			AlertDialog.Builder builder = new AlertDialog.Builder(act);
			builder.setTitle("Delete Event");
			builder.setMessage("Delete selected Event ?");
			builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					sch.unSchedule(Integer.parseInt(str));
					//Log.d(DEBUG_TAG, "Delete done");
					refreshList();
					// remove entry from shared prefs if user delelets event
					SharedPreferences prefs = getApplicationContext().getSharedPreferences(AlarmManagerBroadcastReceiver.EVENT_PREFS, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = prefs.edit();
					Log.d(DEBUG_TAG, "prefs exist " + prefs.getString(str, ""));
					editor.remove(str);
					editor.commit();
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			builder.show();
			return true;
		}

	};

	public void refreshList () {
		ds.open();
		cursor = ds.query();
		myAdapter.changeCursor(cursor);
		ds.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//Log.d(DEBUG_TAG, "onCreateOptionsMenu()");
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
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		//Log.d(DEBUG_TAG, "ID : " + id + " pos " + position);
		startActivity(new Intent(this, CreateEventActivity.class).putExtra("clickedEvent", position)
				.putExtra("addNewEvent", false));
	}


	public void showAddEventDialog() {

		final Activity act = this;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setItems(R.array.add_event_dialog_array, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Log.d(DEBUG_TAG, " you selected " + which + " from list");
				switch (which) {
				case 0 : 
					startActivity(new Intent(act, CreateEventActivity.class).putExtra("addNewEvent", true));
					break;
				case 1 :
					break;
				}
			}
		});
		builder.show();
		//Log.d(DEBUG_TAG, "showAddEventDialog");
	}

	public void onAddEventClick(View v) {
		//Log.d(DEBUG_TAG, "onAddEventDialog");
		startActivity(new Intent(act, CreateEventActivity.class).putExtra("addNewEvent", true));
		//showAddEventDialog();
	}



}
