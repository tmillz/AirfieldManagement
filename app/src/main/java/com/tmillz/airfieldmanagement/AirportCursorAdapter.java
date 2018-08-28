package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

class AirportCursorAdapter extends ResourceCursorAdapter {


    AirportCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView txtQuery = (TextView) view.findViewById(R.id.txtQuery);
        txtQuery.setText(cursor.getString(cursor.getColumnIndex("field2")));
        
        TextView txtQuery2 = (TextView) view.findViewById(R.id.txtQuery2);
        txtQuery2.setText(cursor.getString(cursor.getColumnIndex("field3")));
        
        TextView txtQuery3 = (TextView) view.findViewById(R.id.txtQuery3);
        txtQuery3.setText(cursor.getString(cursor.getColumnIndex("field4")));
        
        TextView txtQuery4 = (TextView) view.findViewById(R.id.txtQuery4);
        txtQuery4.setText("Lat/Lng (\u00b0dd): " + cursor.getString(cursor.getColumnIndex("field7")) + " " + cursor.getString(cursor.getColumnIndex("field8")));
        
        TextView txtQuery6 = (TextView) view.findViewById(R.id.txtQuery6);
        txtQuery6.setText("Alt (ft): " + cursor.getString(cursor.getColumnIndex("field9")));
        
        TextView txtQuery7 = (TextView) view.findViewById(R.id.txtQuery7);
        txtQuery7.setText("UTC: " + cursor.getString(cursor.getColumnIndex("field10")));

        TextView txtQuery8 = (TextView) view.findViewById(R.id.txtQuery8);
        txtQuery8.setText("DST Zone: " + cursor.getString(cursor.getColumnIndex("field11")));
    }
}