package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

class MarkersCursorAdapter extends ResourceCursorAdapter {

	MarkersCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
		super(context, layout, cursor, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor arg1) {

		TextView title = (TextView) view.findViewById(R.id.title);
		String description = arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_DISC));
		title.setText(description);

		TextView idBy = (TextView) view.findViewById(R.id.idBy);
		String idByString = "ID'd By: "+arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_ZOOM));
        idBy.setText(idByString);
        
        TextView date = (TextView) view.findViewById(R.id.listDate);
		String dateString = "Date: "+arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_DATE));
        date.setText(dateString);
	}
}
