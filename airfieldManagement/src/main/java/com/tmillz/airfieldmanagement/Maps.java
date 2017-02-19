package com.tmillz.airfieldmanagement;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.R.attr.id;

public class Maps extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnMapReadyCallback, LocationListener {
	
	private GoogleMap mMap;
	EditText editTitle;
	private final int TAG_CODE_PERMISSION_LOCATION = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.maps, container, false);

		SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
				.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);

		editTitle = (EditText) view.findViewById(R.id.editTitle);

		restartTheLoader();

		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) ==
				PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
						PackageManager.PERMISSION_GRANTED) {
		} else {
			requestPermissions(new String[] {
							Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.ACCESS_COARSE_LOCATION },
					TAG_CODE_PERMISSION_LOCATION);
		}

		return view;
	}

	@Override
	public void onLocationChanged(Location location) {

		Log.i("called", "onLocationChanged");

		//when the location changes, update the map by zooming to the location
		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude()));
		mMap.moveCamera(center);

		CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
		mMap.animateCamera(zoom);
	}
	
	private void drawMarker(LatLng point, String disc){

    	// Creating an instance of MarkerOptions
    	MarkerOptions markerOptions = new MarkerOptions();
    		
    	// Setting latitude and longitude for the marker
    	markerOptions.position(point).title(disc).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
    		
    	// Adding marker on the Google Map
    	mMap.addMarker(markerOptions);
    }

	@Override
	public void onMapReady(GoogleMap googleMap) {

		mMap = googleMap;
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

		mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng point) {

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
				contentValues.put(LocationsDB.FIELD_COLOR, "" );
				// Creating an instance of LocationInsertTask
				LocationInsertTask insertTask = new LocationInsertTask();
				// Storing the latitude, longitude and zoom level to SQLite database
				insertTask.execute(contentValues);
				Toast.makeText(getActivity(), "Marker is added to the Map", Toast.LENGTH_SHORT).show();
				editTitle.setText("");
			}
		});

		mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
			@Override
			public void onInfoWindowClick(Marker marker) {
				EditMarker editMarker = new EditMarker();
				Bundle args = new Bundle();
				args.putLong("id", id);
				editMarker.setArguments(args);

				FragmentManager fm = getActivity().getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.container, editMarker);
				ft.addToBackStack(null);
				ft.commit();
			}
		});

		if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) ==
				PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
						PackageManager.PERMISSION_GRANTED) {
			googleMap.setMyLocationEnabled(true);
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		}
	}

	@Override
	@SuppressWarnings({"MissingPermission"})
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case TAG_CODE_PERMISSION_LOCATION: {
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					mMap.setMyLocationEnabled(true);
					mMap.getUiSettings().setMyLocationButtonEnabled(true);
				} else {
					Toast.makeText(getActivity(), "So sorry, location permission denied :(", Toast.LENGTH_LONG).show();
				}
				break;
			}
		}
	}

	private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void>{
		@Override
		protected Void doInBackground(ContentValues... contentValues) {
			// Setting up values to insert the clicked location into SQLite database
            getActivity().getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);
			return null;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// Uri to the content provider LocationsContentProvider
		Uri uri = LocationsContentProvider.CONTENT_URI;
		// Fetches all the rows from locations table
        return new CursorLoader(getActivity(), uri, null, null, null, null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		int locationCount;
		double lat;
		double lng;
		String disc;

		// Number of locations available in the SQLite database table
		locationCount = arg1.getCount();
		// Move the current record pointer to the first row of the table
		arg1.moveToFirst();
		//Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(arg1));
		for(int i=0;i<locationCount;i++){
			// Get the latitude
			lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT));
			// Get the longitude
			lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG));
			// Get the discrepancy
			disc = arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_DISC));
			// Creating an instance of LatLng to plot the location in Google Maps
			LatLng location = new LatLng(lat, lng);
			// Drawing the marker in the Google Maps
			drawMarker(location, disc);
			// Traverse the pointer to the next row
			arg1.moveToNext();
		}
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {

	}
	
	public void restartTheLoader() {
		getLoaderManager().restartLoader(0, null, this);
	}

}
	
	


