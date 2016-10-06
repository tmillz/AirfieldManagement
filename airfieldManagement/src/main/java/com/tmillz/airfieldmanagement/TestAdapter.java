package com.tmillz.airfieldmanagement;

import java.io.IOException;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TestAdapter extends ListActivity{
	
    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private DataBaseHelper mDbHelper;
   
    public TestAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public TestAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } 
        catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public TestAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } 
        catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor getTestData() {
         try {
             String sql ="SELECT * FROM airports";

             Cursor mCur = mDb.rawQuery(sql, null);
             if (mCur!=null) {
                mCur.moveToNext();
             }
             return mCur;
         }
         catch (SQLException mSQLException) {
             Log.e(TAG, "getTestData >>"+ mSQLException.toString());
             throw mSQLException;
         }
     }
    public Cursor getAirport(String sql, String[] query) {
        try {
            Cursor mCur = mDb.rawQuery(sql, query);
            if (mCur!=null) {
               mCur.moveToNext();
            } else {
            	//To Do
            }
            return mCur;
        }
        catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
    
    public Cursor getAircraft(String sql, String[] acftquery) {
        try {
            Cursor mCur = mDb.rawQuery(sql, acftquery);
            if (mCur!=null) {
               mCur.moveToNext();
            } else {
            	//To Do
            }
            return mCur;
        }
        catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}
