package com.tmillz.airfieldmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.Objects;

public class AircraftList extends Fragment {

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_listview, container, false);
		ListView listView = view.findViewById(android.R.id.list);

		DataBaseHelper mDbHelper = new DataBaseHelper(getActivity());
		mDbHelper.open();

		Cursor aircraftData = mDbHelper.getAllAircraft();
		aircraftData.moveToFirst();
		Objects.requireNonNull(getActivity()).startManagingCursor(aircraftData);

		AircraftListCursorAdapter adapter = new AircraftListCursorAdapter(
				getActivity(), aircraftData);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener((arg0, view1, pos, id) -> {
            Intent intent = new Intent(getActivity(), AircraftSpecsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });
		return view;
	}
}
