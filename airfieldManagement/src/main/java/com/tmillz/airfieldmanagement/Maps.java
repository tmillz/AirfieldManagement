package com.tmillz.airfieldmanagement;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

public class Maps extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
	
	public Maps(){
		super();
	}
	
	public Maps(ViewPager viewPager){
		super();
		this.viewPager = viewPager;
	}
	
	public static Maps newInstance() {
        Maps frag = new Maps();
        return frag;
    }
	
	View view;
	private ViewPager viewPager;
	public GoogleMap mMap;
	public Marker mMarker;
	ImageButton sendNow;
	ImageButton add;
	Discrepancies activity;
	LocationsDB locationsDB;
	EditText editTitle;
	LocationManager mLocationManager;
	LocationListener mlocationListener;
	TextView latlongLocation;
	String latlng;
	String slat;
	String slng;
	String degLat;
	String degLng;
	String newline = System.getProperty("line.separator");
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
	
		activity = (Discrepancies) getActivity();
		viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
		int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity.getBaseContext());
		
		//Prompt user to turn on GPS if GPS is off
		if (!((LocationManager) activity.getSystemService(Context.LOCATION_SERVICE))
			    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			 //prompt user to enable gps
			Intent gpsOptionsIntent = new Intent(  
				    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);  
				startActivity(gpsOptionsIntent);
			 } else {
			 //gps is enabled
			 }

		// Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
        
        	int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, activity, requestCode);
            dialog.show();
            
        // Google Play Services are available    
        }else {         	

        	if (view != null) {
    	        ViewGroup parent = (ViewGroup) view.getParent();
    	        if (parent != null)
    	            parent.removeView(view);
    	    } try {
    	        view = inflater.inflate(R.layout.maps, container, false);
    	        
    	    } catch (InflateException e) {
    	        /* map is already there, just return view as it is */
    	    }
	        
	    	mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
		    mMap.setMyLocationEnabled(true);
	        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			LoaderManager.enableDebugLogging(true);
	        Criteria cri= new Criteria();
	        
	        mLocationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
	        String bbb = mLocationManager.getBestProvider(cri, true);
	        
	        Location myLocation = mLocationManager.getLastKnownLocation(bbb);
	        
	        if(myLocation != null) {
	        	double lat= myLocation.getLatitude();
		        double lng = myLocation.getLongitude();
		        LatLng ll = new LatLng(lat, lng);
		        
		        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
		        //Map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("Look what I found!")
		        		//.draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
	        }
	        
	        mlocationListener = new LocationListener() {
	            public void onLocationChanged(Location location) {
	                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
	                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
	                //mMap.animateCamera(cameraUpdate);
	                //mMap.addMarker(new MarkerOptions().position(ll).title("Look what I found!")
			        		//.draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
	                //mLocationManager.removeUpdates(this);
	            }

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onProviderEnabled(String provider) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onProviderDisabled(String provider) {
					// TODO Auto-generated method stub
					
				}
	        };
	        
	        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, mlocationListener);

	        sendNow = (ImageButton) view.findViewById(R.id.sendnow);
	        add = (ImageButton) view.findViewById(R.id.add);
	        editTitle = (EditText) view.findViewById(R.id.editTitle);
	        latlongLocation = (TextView) view.findViewById(R.id.latlongLocation);
	        
	        sendNow.setOnClickListener(new OnClickListener() {
				@Override
	        	public void onClick(View v) {
					captureScreen();
				}
	        	
	        });
	        
	        add.setOnClickListener(new OnClickListener() {
				@Override
	        	public void onClick(View v) {
					viewPager.setCurrentItem(1);
				}
	        });
	        
	        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
	        	//@Override
				public void onMapClick(LatLng point) {

					String disc = editTitle.getText().toString();

					// Drawing marker on the map
					drawMarker(point, disc);

					// Creating an instance of ContentValues
					ContentValues contentValues = new ContentValues();

					// Setting latitude in ContentValues
		            contentValues.put(LocationsDB.FIELD_LAT, point.latitude );

		            // Setting longitude in ContentValues
		            contentValues.put(LocationsDB.FIELD_LNG, point.longitude);

		            // Setting zoom in ContentValues
		            contentValues.put(LocationsDB.FIELD_ZOOM, mMap.getCameraPosition().zoom);

		            // Setting title text
		            contentValues.put(LocationsDB.FIELD_DISC, disc);

		            // Setting color of marker
		            contentValues.put(LocationsDB.FIELD_COLOR, "blue" );

		            // Creating an instance of LocationInsertTask
					LocationInsertTask insertTask = new LocationInsertTask();

					// Storing the latitude, longitude and zoom level to SQLite database
					insertTask.execute(contentValues);

			        Toast.makeText(activity, "Marker is added to the Map", Toast.LENGTH_SHORT).show();

			        editTitle.setText("");

			        latlongLocation.setText("");
				}
			});

			/*mMap.setOnMapLongClickListener(new OnMapLongClickListener() {				
				@Override
				public void onMapLongClick(LatLng point) {
					
					// Removing all markers from the Google Map
					mMap.clear();
					
					// Creating an instance of LocationDeleteTask
					LocationDeleteTask deleteTask = new LocationDeleteTask();
					
					// Deleting all the rows from SQLite database table
					deleteTask.execute();
					
					Toast.makeText(activity, "All markers are removed", Toast.LENGTH_LONG).show();							
				}
			});*/
			
			mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					latlng = marker.getPosition().toString();
					String latlnga = (String) latlng.replaceAll("[()]","");
					slat = String.valueOf(marker.getPosition().latitude);
					slng = String.valueOf(marker.getPosition().longitude);
					degLat = Location.convert(marker.getPosition().latitude, Location.FORMAT_SECONDS);
					degLng = Location.convert(marker.getPosition().longitude, Location.FORMAT_SECONDS);
					latlongLocation.setText(marker.getTitle() + "" + latlnga + newline + degLat + "," + degLng);
					return false;
				}
			});
        }
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mLocationManager.removeUpdates(mlocationListener);
	
	}
	
	private void drawMarker(LatLng point, String disc){
    	
    	// Creating an instance of MarkerOptions
    	MarkerOptions markerOptions = new MarkerOptions();
    	
    	//BitmapDescriptorFactory bmd = BitmapDescriptorFactorycolor
    		
    	// Setting latitude and longitude for the marker
    	markerOptions.position(point).title(disc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    		
    	// Adding marker on the Google Map
    	mMap.addMarker(markerOptions);    		
    }
	
	private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void>{
		@Override
		protected Void doInBackground(ContentValues... contentValues) {
			
			/** Setting up values to insert the clicked location into SQLite database */           
            activity.getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);			
			return null;
		}		
	}
	
	/*private class LocationDeleteTask extends AsyncTask<Void, Void, Void>{
		@Override
		protected Void doInBackground(Void... params) {
			
			 //Deleting all the locations stored in SQLite database 
            activity.getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);			
			return null;
		}		
	}*/

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		
		// Uri to the content provider LocationsContentProvider
		Uri uri = LocationsContentProvider.CONTENT_URI;
	
		// Fetches all the rows from locations table
        return new CursorLoader(activity, uri, null, null, null, null);
		
        
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		int locationCount = 0;
		double lat=0;
		double lng=0;
		float zoom=0;
		String disc;
		
		// Number of locations available in the SQLite database table
		locationCount = arg1.getCount();
		
		// Move the current record pointer to the first row of the table
		arg1.moveToFirst();
		
		Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(arg1));
		
		for(int i=0;i<locationCount;i++){
			
			// Get the latitude
			lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT));
			
			// Get the longitude
			lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG));
			
			// Get the zoom level
			zoom = arg1.getFloat(arg1.getColumnIndex(LocationsDB.FIELD_ZOOM));
			
			// Get the discrepancy
			disc = arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_DISC));
			
			// Creating an instance of LatLng to plot the location in Google Maps
			LatLng location = new LatLng(lat, lng);
		
			// Drawing the marker in the Google Maps
			drawMarker(location, disc);
			
			// Traverse the pointer to the next row
			arg1.moveToNext();
		}
		
		/*if(locationCount>0){
			// Moving CameraPosition to last clicked position
	        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));
	        
	        // Setting the zoom level in the map on last position  is clicked
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
		}*/
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
	}
	
	public void restartTheLoader() {
		mMap.clear();
		getLoaderManager().restartLoader(0, null, this);
	}

	public void captureScreen() {
    	//GoogleMap mMap = ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
    	
        SnapshotReadyCallback callback = new SnapshotReadyCallback() {

            @SuppressLint("WorldReadableFiles")
			@SuppressWarnings("deprecation")
			@Override
            public void onSnapshotReady(Bitmap snapshot) {
            	
                // TODO Auto-generated method stub
                Bitmap bitmap = snapshot;

                OutputStream fout = null;

                String filePath = System.currentTimeMillis() + ".jpeg";

                try {
                    fout = activity.openFileOutput(filePath,
                            Context.MODE_WORLD_READABLE);

                    // Write the string to the file
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
                    fout.flush();
                    fout.close();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "FileNotFoundException");
                    Log.d("ImageCapture", e.getMessage());
                    filePath = "";
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.d("ImageCapture", "IOException");
                    Log.d("ImageCapture", e.getMessage());
                    filePath = "";
                }

                openShareImageDialog(filePath);
            }
        };
        
        mMap.snapshot(callback);
    }
    
    public void openShareImageDialog(String filePath) {
    File file = activity.getFileStreamPath(filePath);
    //Criteria cri= new Criteria();
    //LocationManager mLocationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
    //String bbb = mLocationManager.getBestProvider(cri, true);
    //Location myLocation = mLocationManager.getLastKnownLocation(bbb);
    //double lat= myLocation.getLatitude();
    //double lng = myLocation.getLongitude();

    if(!filePath.equals("")) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
        final Uri contentUriFile = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(android.content.Intent.EXTRA_STREAM, contentUriFile);
        intent.putExtra(Intent.EXTRA_TEXT, "http://maps.google.com/maps?q=" + slat + "," + slng + "&ll=" + slat +","+ slng + "&z=17" + newline + newline + latlng
        		+ newline + newline + degLat + " " + degLng);
        startActivity(Intent.createChooser(intent, "Share Image"));
        
	    } else {
	        //Toast!
	    } 
    }
}
	
	


