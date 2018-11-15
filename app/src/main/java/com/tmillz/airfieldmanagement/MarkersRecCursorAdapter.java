package com.tmillz.airfieldmanagement;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MarkersRecCursorAdapter extends
        RecyclerViewCursorAdapter<MarkersRecCursorAdapter.ItemHolder> {

    public MarkersRecCursorAdapter() {
        super(null);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.markers_list,
                parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, Cursor cursor) {
        holder.bindCursor(cursor);
    }

    @Override
    public void swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView idBy;
        public TextView date;
        public RelativeLayout viewBackground, viewForeground;

        public ItemHolder(View itemView) {
            super (itemView);
            title = itemView.findViewById(R.id.title);
            idBy = itemView.findViewById(R.id.idBy);
            date = itemView.findViewById(R.id.listDate);
            itemView.setTag(this);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);

            // working click for marker item
            itemView.setOnClickListener(view -> {
                RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder)view.getTag();
                long id = viewHolder.getItemId();
                Log.e("Yay!", String.valueOf(id));
                Intent intent = new Intent(view.getContext(), EditMarkerActivity.class);
                intent.putExtra("id", id);
                view.getContext().startActivity(intent);
            });
        }

        private void bindCursor(Cursor cursor) {
            title.setText(cursor.getString(cursor.getColumnIndexOrThrow(
                    LocationsDB.FIELD_DISC
            )));
            idBy.setText(cursor.getString(cursor.getColumnIndexOrThrow(
                    LocationsDB.FIELD_ZOOM
            )));
            date.setText(cursor.getString(cursor.getColumnIndexOrThrow(
                    LocationsDB.FIELD_DATE
            )));
        }
    }
}
