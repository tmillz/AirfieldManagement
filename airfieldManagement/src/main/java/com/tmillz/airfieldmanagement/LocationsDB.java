package com.tmillz.airfieldmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class LocationsDB extends SQLiteOpenHelper {

    // Database name
    static String DBNAME = "Locations";

    // Version number of the database
    private static final int VERSION = 4;  //used to be 3

    // Field 1 of the table locations, which is the primary key
    public static final String FIELD_ROW_ID = "_id";

    // Field 2 of the table locations, stores the latitude
    public static final String FIELD_LAT = "lat";

    // Field 3 of the table locations, stores the longitude
    public static final String FIELD_LNG = "lng";

    // Field 4 of the table locations, stores the Id'd By
    public static final String FIELD_ZOOM = "zoom";
    
    // Field 5 of the table locations, stores the Title
    public static final String FIELD_DISC = "disc";
    
    // Field 6 of the table locations, stores Notes
    public static final String FIELD_COLOR = "color";
    
    // Field 7 of the table locations, stores pic Uri
    public static final String FIELD_PIC = "pic";

    // Field 8 of the table locations, stores color
    public static final String FIELD_DATE = "date";

    // A constant, stores the the table name
    private static final String DATABASE_TABLE = "locations";

    // An instance variable for SQLiteDatabase
    private SQLiteDatabase mDB;  
    

    // Constructor
    public LocationsDB(Context context) {
        super(context, DBNAME, null, VERSION);  
        this.mDB = getWritableDatabase();
    }


    // This is a callback method, invoked when the method getReadableDatabase() / getWritableDatabase() is called
    // provided the database does not exists

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =    "create table " + DATABASE_TABLE + " ( " +
                            FIELD_ROW_ID + " integer primary key autoincrement , " +
                            FIELD_LNG + " double , " +
                            FIELD_LAT + " double , " +
                            FIELD_ZOOM + " text , " +  // AKA ID'd By
                            FIELD_DISC + " text , " +  // AKA Title
                            FIELD_COLOR + " real , " + // AKA Notes
                            FIELD_PIC + " text , " +
                            FIELD_DATE + " text " +
                        " ) ";

        db.execSQL(sql);

    }

    //Inserts a new location to the table locations
    public long insert(ContentValues values){
        long rowID = mDB.insert(DATABASE_TABLE, null, values);
        return rowID;
    }   

    //Deletes all locations from the table
    public int del(){
        int cnt = mDB.delete(DATABASE_TABLE, null , null);
        return cnt;
    }
    
    //Delete record based on row _id
    public int deleteId(String rowId) {
        String whereClause = FIELD_ROW_ID + "=?";
        String[] whereArgs = new String[] { rowId };
    	int cnt = mDB.delete(DATABASE_TABLE, whereClause, whereArgs);
        return cnt;
    }
    
    //Select record based on row _id
    public Cursor select(String rowId) {
    	Cursor getrow = mDB.query(DATABASE_TABLE, null, FIELD_ROW_ID + "=" + rowId, null, null, null, null);
        getrow.moveToFirst();
    	return getrow;
    }
    
    //Update record based on row _id
    public int update(ContentValues values, String selection){
        String whereClause = FIELD_ROW_ID + "=?";
        String[] whereArgs = new String[] { selection };
        int cnt = mDB.update(DATABASE_TABLE, values, whereClause, whereArgs);
        return cnt;
    }
      
    // Returns all the locations from the table
    public Cursor getAllLocations(){
        return mDB.query(DATABASE_TABLE, new String[] { FIELD_ROW_ID,  FIELD_LAT, FIELD_LNG, FIELD_ZOOM, FIELD_DISC, FIELD_COLOR, FIELD_PIC, FIELD_DATE } , null, null, null, null, null, null);
    }


    // Adding fields to the existing database
    @Override
    public void onUpgrade(SQLiteDatabase mDB, int oldVersion, int newVersion) {
    	if (newVersion > oldVersion) {
    		
    		try {
                mDB.execSQL("ALTER TABLE locations" + " ADD COLUMN pic STRING DEFAULT null"); // VERSION 4 MOD
            } catch (SQLException e) {
                Log.i("ADD COLUMN pic", "pic already exists");
            }

            try {
                mDB.execSQL("ALTER TABLE locations" + " ADD COLUMN date STRING DEFAULT null"); // VERSION 5 MOD
            } catch (SQLException e) {
                Log.i("ADD COLUMN date", "date already exists");
            }
    	}
    }
}
