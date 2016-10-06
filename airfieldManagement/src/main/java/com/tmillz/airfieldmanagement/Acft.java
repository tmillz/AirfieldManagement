package com.tmillz.airfieldmanagement;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

public class Acft extends ListFragment {

	public Acft(){
		super();
	}

	public static Acft newInstance() {
        Acft frag = new Acft();
        return frag;
    }

	private AircraftCursorAdapter adapter;
	MainActivity activity;
	ListView lv;
	LocationsDB locationsDB;
	Spinner aircraft;

	private String sql = "SELECT * FROM aircraft WHERE LOWER(aircraft)LIKE ?";

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
		((ActionBarActivity) getActivity()).getSupportActionBar().setTitle("Aircraft");

		final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
		            R.array.aircraft_array, android.R.layout.simple_spinner_item);
		    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    	aircraft.setAdapter(adapter);

		aircraft.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String acftquery = aircraft.getSelectedItem().toString();
				getAircraft(acftquery);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
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

	public interface OnRefreshListener {
	    public void onRefresh();
	}

	private void getAircraft(String acftquery) {

 		TestAdapter mDbHelper = new TestAdapter(activity);
        mDbHelper.open();

     	Cursor testdata = mDbHelper.getAirport(sql, new String[] {acftquery});
     	if (testdata.moveToFirst()==false){
     		// To Do
     	} else {
	     	if (testdata !=null) {
				testdata.moveToFirst();

				Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(testdata));

	            AircraftCursorAdapter adapter = new AircraftCursorAdapter(
	                    activity, R.layout.aircraft_specs, testdata, 0 );

	            this.setListAdapter(adapter);
	        }
     	}
	}
}
