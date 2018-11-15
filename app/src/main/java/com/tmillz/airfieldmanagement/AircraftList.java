package com.tmillz.airfieldmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class AircraftList extends Fragment {

	DataBaseHelper mDbHelper;
	Cursor aircraftData;
	ListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_listview, container, false);
		listView = view.findViewById(android.R.id.list);

		mDbHelper = new DataBaseHelper(getActivity());
		mDbHelper.open();

		aircraftData = mDbHelper.getAllAircraft();
		aircraftData.moveToFirst();
		getActivity().startManagingCursor(aircraftData);

		AircraftListCursorAdapter adapter = new AircraftListCursorAdapter(
				getActivity(), R.layout.aircraft_list, aircraftData, 0 );
		listView.setAdapter(adapter);

		listView.setOnItemClickListener((arg0, view1, pos, id) -> {
            Intent intent = new Intent(getActivity(), AircraftSpecsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });
		return view;
	}
}
