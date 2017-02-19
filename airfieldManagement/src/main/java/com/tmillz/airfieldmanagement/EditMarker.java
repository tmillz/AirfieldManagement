package com.tmillz.airfieldmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class EditMarker extends Fragment {

	Cursor editmarker;
	Button save;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		long id = getArguments().getLong("id");

		View view = inflater.inflate(R.layout.edit_marker, container, false);
		ListView listView = (ListView) view.findViewById(android.R.id.list);

		save = (Button) view.findViewById(R.id.save);

		LocationsDB locationsDB = new LocationsDB(getActivity());
		locationsDB.getReadableDatabase();

		editmarker = locationsDB.select(id);
		EditMarkerCursorAdapter adapter = new EditMarkerCursorAdapter(
				getActivity(), R.layout.edit_marker, editmarker, 0 );
		listView.setAdapter(adapter);

		save.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		
		return view;
	}
}