package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.database.Cursor;
//import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

import androidx.cursoradapter.widget.ResourceCursorAdapter;

class AircraftListCursorAdapter extends ResourceCursorAdapter {

    AircraftListCursorAdapter(Context context, Cursor cursor) {
        super(context, R.layout.aircraft_list, cursor, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor arg1) {

        TextView title = view.findViewById(R.id.aircraft_title);
        String description = arg1.getString(arg1.getColumnIndex("aircraft"));
        title.setText(description);

        TextView wingSpan = view.findViewById(R.id.wing_span);
        String wing_span = "wingspan = " + arg1.getString(arg1.getColumnIndex(
                "wing_span")) + " ft";
        wingSpan.setText(wing_span);

        TextView length = view.findViewById(R.id.length);
        String acft_length = "length = " + arg1.getString(arg1.getColumnIndex(
                "length")) + " ft";
        length.setText(acft_length);
    }
}


