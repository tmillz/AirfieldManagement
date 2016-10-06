package com.tmillz.airfieldmanagement;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class EditDisc extends Fragment {
	
	public EditDisc(){
		super();
	}
	
	public static EditDisc newInstance() {
        EditDisc frag = new EditDisc();
        return frag;
    }
	
	Button save;
	Cursor editmarker;
	Discrepancies activity;
	EditText editDisc;
	EditText editLat;
	EditText editLng;
	EditText editColor;
	ImageButton imageButton;
	ListView listview;
	LocationsDB locationsDB;
	ViewPager viewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		activity = (Discrepancies) getActivity();
		locationsDB = new LocationsDB(activity);
		viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
		View view = inflater.inflate(R.layout.editdisc, container, false);
		listview = (ListView) view.findViewById(android.R.id.list);
		//save = (Button) view.findViewById(R.id.save);
		//editDisc = (EditText) view.findViewById(R.id.editDisc);
		//editLat = (EditText) view.findViewById(R.id.editLat);
		//editLng = (EditText) view.findViewById(R.id.editLng);
		//imageButton = (ImageButton) view.findViewById(R.id.imageButton);
		//long id = editmarker.getLong(editmarker.getColumnIndex(LocationsDB.FIELD_ROW_ID));
		//editmarker = locationsDB.select(id);
		EditDiscCursorAdapter adapter = new EditDiscCursorAdapter(
                activity, R.layout.editdisc, editmarker, 0 );
        listview.setAdapter(adapter);
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
	
	public void editMarker(Long id) {
		editmarker = locationsDB.select(id);
		EditDiscCursorAdapter adapter = new EditDiscCursorAdapter(
                activity, R.layout.editdisc, editmarker, 0 );
        listview.setAdapter(adapter);
        viewPager.setCurrentItem(1);
	}
}