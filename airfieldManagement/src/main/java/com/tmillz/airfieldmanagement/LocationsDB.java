package com.tmillz.airfieldmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;


public class LocationsDB extends SQLiteOpenHelper {

    /** Database name */
    private static String DBNAME = "Locations";

    /** Version number of the database */
    private static int VERSION = 3;

    /** Field 1 of the table locations, which is the primary key */
    public static final String FIELD_ROW_ID = "_id";

    /** Field 2 of the table locations, stores the latitude */
    public static final String FIELD_LAT = "lat";

    /** Field 3 of the table locations, stores the longitude*/
    public static final String FIELD_LNG = "lng";

    /** Field 4 of the table locations, stores the zoom level of map*/
    public static final String FIELD_ZOOM = "zoom";
    
    /** Field 5 of the table locations, stores the discription*/
    public static final String FIELD_DISC = "disc";
    
    /** Field 6 of the table locations, stores color*/
    public static final String FIELD_COLOR = "color";
    
    /** Field 7 of the table locations, stores color*/
    public static final String FIELD_PIC = "pic";

    /** A constant, stores the the table name */
    private static final String DATABASE_TABLE = "locations";

    /** An instance variable for SQLiteDatabase */
    private SQLiteDatabase mDB;  
    

    /** Constructor */
    public LocationsDB(Context context) {
        super(context, DBNAME, null, VERSION);  
        this.mDB = getWritableDatabase();
    }


    /** This is a callback method, invoked when the method getReadableDatabase() / getWritableDatabase() is called 
      * provided the database does not exists 
    * */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql =    "create table " + DATABASE_TABLE + " ( " +
                            FIELD_ROW_ID + " integer primary key autoincrement , " +
                            FIELD_LNG + " double , " +
                            FIELD_LAT + " double , " +
                            FIELD_ZOOM + " text , " +
                            FIELD_DISC + " text , " +
                            FIELD_COLOR + " real , " +
                            FIELD_PIC + " text " +
                        " ) ";

        db.execSQL(sql);
        
        /*Cursor cursor = mDB.rawQuery("SELECT * FROM DATABASE_TABLE", null); // grab cursor for all data
        int deleteStateColumnIndex = cursor.getColumnIndex("FIELD_PIC");  // see if the column is there
        if (deleteStateColumnIndex < 0) { 
            // missing_column not there - add it
            mDB.execSQL("ALTER TABLE DATABASE_TABLE ADD COLUMN FIELD_PIC int null;");
        }*/
    }

    /** Inserts a new location to the table locations */
    public long insert(ContentValues contentValues){
        long rowID = mDB.insert(DATABASE_TABLE, null, contentValues);
        return rowID;

    }   

    /** Deletes all locations from the table */
    public int del(){
        int cnt = mDB.delete(DATABASE_TABLE, null , null);      
        return cnt;
    }
    
    /** Delete record based on row _id */
    public long delete(long pos) {
    	long rowID = mDB.delete(DATABASE_TABLE, FIELD_ROW_ID + "=" + pos, null);
    	return rowID;
    }
    
    /** Select record based on row _id **/
    public Cursor select(long id) {
    	Cursor getrow = mDB.query(DATABASE_TABLE, null, FIELD_ROW_ID + "=" + id, null, null, null, null);
    	return getrow;
    }
    
    /** Update record based on row _id**/
    public void update_byID(Long id, String ed, String lat, String lng, String ec, Uri selectedImageUri){
    	  ContentValues values = new ContentValues();
    	  if(ed!=null) {
    		  values.put(LocationsDB.FIELD_DISC, ed);
    	  }
    	  if(lat!=null) {
    		  values.put(LocationsDB.FIELD_LAT, lat);
    	  }
    	  if(lng!=null) {
    		  values.put(LocationsDB.FIELD_LNG, lng);
    	  }
    	  if(ec!=null) {
    		  values.put(LocationsDB.FIELD_COLOR, ec);
    	  }
    	  if(selectedImageUri!=null){
    		  values.put(LocationsDB.FIELD_PIC, selectedImageUri.toString());
    	  }
    	  mDB.update(LocationsDB.DATABASE_TABLE, values, FIELD_ROW_ID+"="+id, null);
    	 }
      
    /** Returns all the locations from the table */
    public Cursor getAllLocations(){
        return mDB.query(DATABASE_TABLE, new String[] { FIELD_ROW_ID,  FIELD_LAT, FIELD_LNG, FIELD_ZOOM, FIELD_DISC, FIELD_COLOR, FIELD_PIC } , null, null, null, null, null);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase mDB, int oldVersion, int newVersion) {
    	if (newVersion > oldVersion) {
    		
    		try {
                mDB.execSQL("ALTER TABLE locations" + " ADD COLUMN pic STRING DEFAULT null");
            } catch (SQLException e) {
                Log.i("ADD COLUMN pic", "pic already exists");
            }
    	}
    }
}
