package com.tmillz.airfieldmanagement;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

public abstract class RecyclerViewCursorAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    private Cursor mCursor;
    private boolean mDataValid;
    private int mRowIDColumn;

    public abstract void onBindViewHolder(VH holder, Cursor cursor);

    public RecyclerViewCursorAdapter(Cursor cursor) {
        setHasStableIds(true);
        swapCursor(cursor);
    }

    @Override
    public void onBindViewHolder(VH holder, int position){
        if(!mDataValid) {
            throw new IllegalStateException(
                    "Cannot look up item id when cursor is in invalid state");
        }
        if (!mCursor.moveToPosition(position)){
            throw new IllegalStateException(
                    "Could not move cursor to position " +  position + "when getting ID");
        }
        onBindViewHolder(holder, mCursor);
    }

    @Override
    public int getItemCount() {
        if(mDataValid){
            return mCursor.getCount();
        }
        else{
            return 0;
        }
    }

    @Override
    public long getItemId(int position) {
        if(!mDataValid){
            throw new IllegalStateException(
                    "Cannot lookup item id when cursor is in invalid state.");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("Could not move cursor to position "
                    + position + " when trying to get an item id");
        }

        return mCursor.getLong(mRowIDColumn);
    }

    public void swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return;
        }

        if (newCursor != null) {
            mCursor = newCursor;
            mRowIDColumn = mCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            // notify the observers about the new cursor
            notifyDataSetChanged();
        } else {
            notifyItemRangeRemoved(0, getItemCount());
            mCursor = null;
            mRowIDColumn = -1;
            mDataValid = false;
        }
    }
}
