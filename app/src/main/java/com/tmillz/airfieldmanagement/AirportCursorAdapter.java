package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

class AirportCursorAdapter extends ResourceCursorAdapter {


    AirportCursorAdapter(Context context, Cursor c) {
        super(context, R.layout.activity_search_results, c, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView txtQuery = view.findViewById(R.id.txtQuery);
        txtQuery.setText(cursor.getString(cursor.getColumnIndex("field2")));
        
        TextView txtQuery2 = view.findViewById(R.id.txtQuery2);
        txtQuery2.setText(cursor.getString(cursor.getColumnIndex("field3")));
        
        TextView txtQuery3 = view.findViewById(R.id.txtQuery3);
        txtQuery3.setText(cursor.getString(cursor.getColumnIndex("field4")));
        
        TextView txtQuery4 = view.findViewById(R.id.txtQuery4);
        txtQuery4.setText(context.getString(R.string.lat_lng,
               cursor.getString(cursor.getColumnIndex("field7")),
                cursor.getString(cursor.getColumnIndex("field8"))));
        
        TextView txtQuery6 = view.findViewById(R.id.txtQuery6);
        txtQuery6.setText(context.getString(R.string.alt,
                cursor.getString(cursor.getColumnIndex("field9"))));
        
        TextView txtQuery7 = view.findViewById(R.id.txtQuery7);
        txtQuery7.setText(context.getString(R.string.utc,
                cursor.getString(cursor.getColumnIndex("field10"))));

        TextView txtQuery8 = view.findViewById(R.id.txtQuery8);
        txtQuery8.setText(context.getString(R.string.dst,
                cursor.getString(cursor.getColumnIndex("field11"))));
    }
}