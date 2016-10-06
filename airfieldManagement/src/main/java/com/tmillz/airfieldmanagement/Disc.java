package com.tmillz.airfieldmanagement;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class Disc extends Fragment implements LoaderCallbacks<Cursor> {
	
	public Disc(){
		super();
	}
	
	public static Disc newInstance() {
        Disc frag = new Disc();
        return frag;
    }
	
	Discrepancies activity;
	DiscrepanciesCursorAdapter adapter;
	ListView listview;
	LocationsDB locationsDB;
	OnMarkerSelectedListener mListener;
	ViewPager viewPager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		activity = (Discrepancies) getActivity();
		locationsDB = new LocationsDB(activity);
		viewPager = (ViewPager) getActivity().findViewById(R.id.pager);
		getLoaderManager().restartLoader(0, null, this);
		View view = inflater.inflate(R.layout.disc, container, false);
		listview = (ListView) view.findViewById(android.R.id.list);
		adapter = new DiscrepanciesCursorAdapter(activity, R.layout.disc, null, 0);
		View header = activity.getLayoutInflater().inflate(R.layout.disc_header, listview, false);
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
				// TODO Auto-generated method stub
				// Append the clicked item's row ID with the content provider Uri
		        ContentUris.withAppendedId(LocationsContentProvider.CONTENT_URI, id);
		        // Send the event and Uri to the host activity
		        mListener.onMarkerSelected(id);
			}
		});
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		//listview.setAdapter(adapter);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	public interface OnRefreshListener {
	    public void onRefresh();
	}
	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnMarkerSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }
	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// Uri to the content provider LocationsContentProvider
		Uri uri = LocationsContentProvider.CONTENT_URI;
	
		// Fetches all the rows from locations table
        return new CursorLoader(activity, uri, null, null, null, null);
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
		Log.v("long clicked","pos: " + id);
        locationsDB.delete(id);
        Toast.makeText(getActivity(), "Marker has been deleted", Toast.LENGTH_SHORT).show();
        getLoaderManager().restartLoader(0, null, this);
	}
	
	public interface OnMarkerSelectedListener {
        public void onMarkerSelected(Long id);
		void onActivityResult();
    }
}
