package com.tmillz.airfieldmanagement;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {
private static String TAG = "DataBaseHelper"; // Tag just for the LogCat window

private static String DB_PATH = ""; 
private static String DB_NAME ="Database"; // Database name
private static int DB_VERSION = 2; // Database Version
private SQLiteDatabase mDataBase; 
private final Context mContext;

@SuppressLint("SdCardPath")
public DataBaseHelper(Context context) {
    super(context, DB_NAME, null, DB_VERSION); // 1? its Database Version
    if(android.os.Build.VERSION.SDK_INT >= 17){
       DB_PATH = context.getApplicationInfo().dataDir + "/databases/";         
    }
    else {
       DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }
    this.mContext = context;
}   

public void createDataBase() {

    boolean mDataBaseExist = checkDataBase();
    if(!mDataBaseExist) {
        try {
            //Copy the database from assests
            copyDataBase();
            Log.e(TAG, "createDatabase database created");
        } 
        catch (IOException mIOException) {
            throw new Error("ErrorCopyingDataBase");
        }
    }
}
    //Check that the database exists at /data/data/com.tmillz.airfieldmanagement/databases/Database
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Open the database, so we can query it
    public void open() {
        String mPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.CREATE_IF_NECESSARY);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion>oldVersion){
	        this.mContext.deleteDatabase(DB_NAME);
	        try {
	            copyDataBase();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

    public Cursor getData(String sql, String[] query) {
        try {
            Cursor mCur = mDataBase.rawQuery(sql, query);

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

    public Cursor getAllAircraft(){
        return mDataBase.query("aircraft", null, null, null,
                null, null, null);
    }
}
