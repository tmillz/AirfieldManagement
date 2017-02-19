package com.tmillz.airfieldmanagement;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MarkersList extends DialogFragment implements LoaderCallbacks<Cursor> {

	MarkersCursorAdapter adapter;
	ListView listview;
	LocationsDB locationsDB;
	ViewPager viewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		locationsDB = new LocationsDB(getActivity());
		viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
		getLoaderManager().restartLoader(0, null, this);
		View view = inflater.inflate(R.layout.markers_list, container, false);
		listview = (ListView) view.findViewById(android.R.id.list);
		adapter = new MarkersCursorAdapter(getActivity(), R.layout.markers_list, null, 0);
		View header = getActivity().getLayoutInflater().inflate(R.layout.markers_list_header, listview, false);
		listview.addHeaderView(header);
		listview.setAdapter(adapter);
		
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
        		deletemarker(id);
				return true;
        	}
        	
        });
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {

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
		return view;
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
		adapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}
	
	public void deletemarker(long id) {
		// Log.v("long clicked","pos: " + id);
        locationsDB.delete(id);
        Toast.makeText(getActivity(), "Marker has been deleted", Toast.LENGTH_SHORT).show();
        getLoaderManager().restartLoader(0, null, this);
	}
}
