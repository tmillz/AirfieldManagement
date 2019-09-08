package com.tmillz.airfieldmanagement;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.sql.SQLException;
import java.util.Objects;

// A custom Content Provider to do the database operations
public class LocationsContentProvider extends ContentProvider{

    private static final String PROVIDER_NAME = "com.tmillz.airfieldmanagement.locations";

    // A uri to do operations on locations table. A content provider is identified by its uri //
    public static final Uri CONTENT_URI = Uri.parse("content://" + PROVIDER_NAME + "/locations" );
    public static final Uri CONTENT_URI_SELECT = Uri.parse("content://" + PROVIDER_NAME +
            "/locations.select" );

    // Constant to identify the requested operation //
    private static final int LOCATIONS = 1;
    private static final int SELECT = 2;

    private static final UriMatcher uriMatcher ;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "locations", LOCATIONS);
        uriMatcher.addURI(PROVIDER_NAME, "locations.select", SELECT);
    }

    // This content provider does the database operations by this object
    private LocationsDB mLocationsDB;

    // A callback method which is invoked when the content provider is starting up
    @Override
    public boolean onCreate() {
        mLocationsDB = new LocationsDB(getContext());
        return true;
    }

    // A callback method invoked when insert operation is requested on this content provider
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        long rowID = mLocationsDB.insert(values);
        Uri _uri = null;
        if(rowID>0){
            _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
        }else {     
            try {
                throw new SQLException("Failed to insert : " + uri);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return _uri;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[]
            selectionArgs) {
        int cnt;
        cnt = mLocationsDB.update(values, selection);
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    // A callback method invoked when delete operation is requested on this content provider
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int cnt;
        cnt = mLocationsDB.deleteId(selectionArgs[0]);
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    // A callback method which is invoked by default content uri
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[]
            selectionArgs, String sortOrder) {

        Cursor cursor;

        if(uriMatcher.match(uri)==LOCATIONS){
            cursor = mLocationsDB.getAllLocations();
        } else {
            cursor = mLocationsDB.select(selection);
        }

        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}