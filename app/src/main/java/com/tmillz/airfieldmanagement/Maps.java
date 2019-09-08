package com.tmillz.airfieldmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.Objects;

import static android.content.ContentValues.TAG;

public class Maps extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,
		OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener, LocationListener {

	private GoogleMap mMap;
	private EditText editTitle;
	private final int TAG_CODE_PERMISSION_LOCATION = 1;
	private long markerId = 0;
	private GoogleApiClient googleApiClient;
	private Location location;
	// DatabaseReference myRef;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState){

		View view = inflater.inflate(R.layout.maps, container, false);

		SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
				.findFragmentById(R.id.map);
		Objects.requireNonNull(mapFragment).getMapAsync(this);

		editTitle = view.findViewById(R.id.editTitle);

		// Firebase Setup
		// FirebaseAuth mAuth = FirebaseAuth.getInstance();
		// FirebaseUser currentUser = mAuth.getCurrentUser();
		// FirebaseDatabase database = FirebaseDatabase.getInstance();
		//database.setPersistenceEnabled(true);  //causes app to crash

		// firebase
		/* if (FirebaseAuth.getInstance().getCurrentUser() != null) {

			myRef = database.getReference("locations").child(currentUser.getUid());

			myRef.addChildEventListener(new ChildEventListener() {
				@Override
				public void onChildAdded(DataSnapshot dataSnapshot, String s) {
					LocationsObject locationsObject = dataSnapshot.getValue(LocationsObject.class);
					Log.d("Tag", dataSnapshot.getValue().toString());
				}

				@Override
				public void onChildChanged(DataSnapshot dataSnapshot, String s) {

				}

				@Override
				public void onChildRemoved(DataSnapshot dataSnapshot) {

				}

				@Override
				public void onChildMoved(DataSnapshot dataSnapshot, String s) {

				}

				@Override
				public void onCancelled(DatabaseError databaseError) {

				}
			});
		}*/

		return view;
	}

	private void drawMarker(LatLng point, String disc, long markerId){

    	// Creating an instance of MarkerOptions
    	MarkerOptions markerOptions = new MarkerOptions();

    	// Setting latitude and longitude for the marker
    	markerOptions.position(point).title(disc).icon(
    			BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

		// Adding marker on the Google Map
    	Marker marker = mMap.addMarker(markerOptions);
		marker.setTag(markerId);
    }

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);

	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

		mMap = googleMap;
		mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

		mMap.setOnMapLongClickListener(point -> {

            markerId = markerId + 1;

            String disc = editTitle.getText().toString();
            // Drawing marker on the map
            drawMarker(point, disc, markerId);
            // Creating an instance of ContentValues
            ContentValues contentValues = new ContentValues();
            // Setting latitude in ContentValues
            contentValues.put(LocationsDB.FIELD_LAT, point.latitude );
            // Setting longitude in ContentValues
            contentValues.put(LocationsDB.FIELD_LNG, point.longitude);
            // Setting Date in ContentValues
            contentValues.put(LocationsDB.FIELD_DATE, "");
            // Setting ID'd By in ContentValues
            contentValues.put(LocationsDB.FIELD_ZOOM, "");
            // Setting title text
            contentValues.put(LocationsDB.FIELD_DISC, disc);
            // Setting Notes of marker
            contentValues.put(LocationsDB.FIELD_COLOR, "" );
            // Creating an instance of LocationInsertTask
            LocationInsertTask insertTask = new LocationInsertTask(this);
            // Storing the latitude, longitude and zoom level to SQLite database
            insertTask.execute(contentValues);
            //Log.v("TAG", contentValues.toString());
            Toast.makeText(getActivity(), "Marker is added to the Map",
                    Toast.LENGTH_SHORT).show();
            editTitle.setText("");

            // firebase
            //Map<String,Object> values = new HashMap<>();
            //values.put("title", disc);
            //values.put("lat", point.latitude);
            //values.put("lng", point.longitude);
            //values.put("date", "");
            //values.put("id_by", "");
            //values.put("notes", "");
            //values.put ("dateClosed","");
            //values.put ("img", "");
            //values.put ("type", "fod.png");
            //myRef.push().setValue(values);
        });

		mMap.setOnInfoWindowClickListener(marker -> {
            long id = (long) marker.getTag();
            Intent intent = new Intent(getActivity(), EditMarkerActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });

		// Check location permissions, if needed request from user
		if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
				android.Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(getActivity(),
						android.Manifest.permission.ACCESS_COARSE_LOCATION) !=
						PackageManager.PERMISSION_GRANTED) {

			requestPermissions(new String[] {
							android.Manifest.permission.ACCESS_FINE_LOCATION,
							android.Manifest.permission.ACCESS_COARSE_LOCATION },
					TAG_CODE_PERMISSION_LOCATION);
		} else {
			getLocationServices();
		}

		// firebase
		/*if (FirebaseAuth.getInstance().getCurrentUser() != null) {

			myRef.addChildEventListener(new ChildEventListener() {
				@Override
				public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {

					Double lat = (Double) dataSnapshot.child("lat").getValue();
					Double lng = (Double) dataSnapshot.child("lng").getValue();
					String title = (String) dataSnapshot.child("title").getValue();

					LatLng newLocation = new LatLng(
							lat,
							lng
					);
					mMap.addMarker(new MarkerOptions()
							.position(newLocation)
							.title(title));
				}

				@Override
				public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
				}

				@Override
				public void onChildRemoved(DataSnapshot dataSnapshot) {
				}

				@Override
				public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
				}

				@Override
				public void onCancelled(DatabaseError databaseError) {
				}
			});
		}*/
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
										   @NonNull int[]  grantResults) {
		switch (requestCode) {
			case TAG_CODE_PERMISSION_LOCATION: {
				getLocationServices();
			}
		}
	}

	@Override
	@SuppressWarnings({"MissingPermission"})
	public void onConnected(Bundle bundle) {

		LocationRequest locationRequest = new LocationRequest();

		Intent intent = Objects.requireNonNull(getActivity()).getIntent();
		Uri data = intent.getData();

		if (data != null) {
			if (data.toString().contains("geo")) {
				String sLocation = data.toString();
				String sLL = sLocation.substring(sLocation.lastIndexOf(":")+1);
				String[] str = sLL.split(",");
				double lat = Double.parseDouble(str[0]);
				double lng = Double.parseDouble(str[1]);
				LatLng ll = new LatLng(lat,lng);
				Log.d("Tag", sLocation);
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 13));
			}
		} else if (location == null) {
			LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
					locationRequest, this);
		} else {
			double lat = location.getLatitude();
			double lng = location.getLongitude();
			LatLng ll = new LatLng(lat,lng);
			mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
		}

	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
		Log.i(TAG,"onConnectionFailed:"+connectionResult.getErrorCode()+","
				+connectionResult.getErrorMessage());
	}

	@Override
	public void onLocationChanged(Location location) {
		double lat = location.getLatitude();
		double lng = location.getLongitude();
		LatLng ll = new LatLng(lat, lng);
		mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
	}

	private static class LocationInsertTask extends AsyncTask<ContentValues, Void, Void>{

		private final WeakReference<Maps> activityReference;

		LocationInsertTask(Maps context) {
			activityReference = new WeakReference<>(context);
		}

		@Override
		protected Void doInBackground(ContentValues... contentValues) {
			// Setting up values to insert locations into SQLite database
            Objects.requireNonNull(activityReference.get().getContext()).getContentResolver()
					.insert(LocationsContentProvider.CONTENT_URI,
					contentValues[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
		}
	}

	@NonNull
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// Uri to the content provider LocationsContentProvider
		Uri uri = LocationsContentProvider.CONTENT_URI;
		// Fetches all the rows from locations table
        return new CursorLoader(Objects.requireNonNull(getActivity()), uri, null,
                null, null,null);
	}

	@Override
	public void onLoadFinished(@NonNull Loader<Cursor> arg0, Cursor arg1) {
		int locationCount;
		double lat;
		double lng;
		String disc;

		// clear the map and redraw the markers
		mMap.clear();
		// Number of locations available in the SQLite database table
		locationCount = arg1.getCount();
		// Move the current record pointer to the first row of the table
		arg1.moveToFirst();
		//Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(arg1));
		for(int i=0;i<locationCount;i++){
			// Get the ID
			markerId = arg1.getInt(arg1.getColumnIndex(LocationsDB.FIELD_ROW_ID));
			// Get the latitude
			lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT));
			// Get the longitude
			lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG));
			// Get the discrepancy
			disc = arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_DISC));
			// Creating an instance of LatLng to plot the location in Google Maps
			LatLng location = new LatLng(lat, lng);
			// Drawing the marker in the Google Maps
			drawMarker(location, disc, markerId);
			// Traverse the pointer to the next row
			arg1.moveToNext();
		}
	}

	@Override
	public void onLoaderReset(@NonNull Loader<Cursor> arg0) {

	}

	private void getLocationServices() {

		if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
				android.Manifest.permission.ACCESS_FINE_LOCATION) ==
				PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(getActivity(),
						android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
						PackageManager.PERMISSION_GRANTED) {
			mMap.setMyLocationEnabled(true);
			mMap.getUiSettings().setMyLocationButtonEnabled(true);

			GoogleApiAvailability api = GoogleApiAvailability.getInstance();
			int status = api.isGooglePlayServicesAvailable(getActivity());

			// Prompt user to turn on GPS if GPS is off
			if (!((LocationManager) Objects.requireNonNull(getActivity()
                    .getSystemService(Context.LOCATION_SERVICE)))
					.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				// prompt user to enable gps
				Intent gpsOptionsIntent = new Intent(
						android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(gpsOptionsIntent);
			}

			// Showing status
			if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available
				int requestCode = 10;
				GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
				apiAvailability.getErrorDialog(getActivity(), status, requestCode).show();
				// Google Play Services are available
			}

			googleApiClient = new GoogleApiClient.Builder(getActivity())
					.addApi(LocationServices.API)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.build();
			googleApiClient.connect();

		} else {
			Toast.makeText(getActivity(), "Sorry, location permission denied :(",
					Toast.LENGTH_LONG).show();
		}
	}
}
