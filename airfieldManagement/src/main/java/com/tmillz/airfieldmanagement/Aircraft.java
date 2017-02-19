package com.tmillz.airfieldmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class Aircraft extends ListFragment {

	AircraftCursorAdapter adapter;
	MainActivity activity;
	ListView lv;
	LocationsDB locationsDB;
	Spinner aircraft;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		activity = (MainActivity) getActivity();
		locationsDB = new LocationsDB(activity);
		View view = inflater.inflate(R.layout.aircraft_specs, container, false);
		lv = (ListView) view.findViewById(android.R.id.list);
		adapter = new AircraftCursorAdapter(activity, R.layout.aircraft_specs, null, 0);
		View header = activity.getLayoutInflater().inflate(R.layout.aircraft_specs_header, null);
		lv.addHeaderView(header);
		lv.setAdapter(adapter);
		aircraft = (Spinner) header.findViewById(R.id.aircraft);
		((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Aircraft");

		final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
		            R.array.aircraft_array, android.R.layout.simple_spinner_item);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    	aircraft.setAdapter(adapter);

		aircraft.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				String acftquery = aircraft.getSelectedItem().toString();
				getAircraft(acftquery);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void getAircraft(String acftquery) {

 		AircraftListAdapter mDbHelper = new AircraftListAdapter(activity);
        mDbHelper.open();

		String sql = "SELECT * FROM aircraft WHERE LOWER(aircraft)LIKE ?";

     	Cursor testdata = mDbHelper.getAirport(sql, new String[] {acftquery});
		testdata.moveToFirst();

		//Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(testdata));

		AircraftCursorAdapter adapter = new AircraftCursorAdapter(
	                    activity, R.layout.aircraft_specs, testdata, 0 );
		this.setListAdapter(adapter);
	}
}
