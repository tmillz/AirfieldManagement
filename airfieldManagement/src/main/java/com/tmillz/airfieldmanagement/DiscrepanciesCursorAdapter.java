package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

public class DiscrepanciesCursorAdapter extends ResourceCursorAdapter{
	
	TextView lat;
	TextView lng;
	TextView title;

	public DiscrepanciesCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
		super(context, layout, cursor, flags);
		
	}

	@Override
	public void bindView(View view, Context context, Cursor arg1) {
		
		title = (TextView) view.findViewById(R.id.title);
		title.setText("Description: " + arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_DISC)));
		
		lat = (TextView) view.findViewById(R.id.lat);
        lat.setText("Lat: " + Double.toString(arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT))));
        
        lng = (TextView) view.findViewById(R.id.lng);
        lng.setText("Lng: " + Double.toString(arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG))));
        
	}
	
}
