package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

class MarkersCursorAdapter extends ResourceCursorAdapter{

	MarkersCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
		super(context, layout, cursor, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor arg1) {

		TextView title = (TextView) view.findViewById(R.id.title);
		String description = "Description: " +
				arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_DISC));
		title.setText(description);


		TextView lat = (TextView) view.findViewById(R.id.lat);
		String latitude = "Lat: " +
				Double.toString(arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT)));
        lat.setText(latitude);
        
        TextView lng = (TextView) view.findViewById(R.id.lng);
		String longitude = "Lng: " +
				Double.toString(arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG)));
        lng.setText(longitude);
	}
}
