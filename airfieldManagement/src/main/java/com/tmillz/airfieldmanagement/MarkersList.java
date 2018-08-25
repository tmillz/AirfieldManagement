package com.tmillz.airfieldmanagement;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class MarkersList extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

	MarkersCursorAdapter adapter;
	ListView listview;
	long longClicked;

	@Override
	public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.activity_listview, container, false);
		listview = (ListView) view.findViewById(android.R.id.list);
		adapter = new MarkersCursorAdapter(getActivity(), R.layout.markers_list, null, 0);
		listview.setAdapter(adapter);

		listview.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				longClicked = id;
				LocationDeleteTask deleteTask = new LocationDeleteTask();
				deleteTask.execute();
				return true;
        	}
        });
		
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {
				Intent intent = new Intent(getActivity(), EditMarkerActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private class LocationDeleteTask extends AsyncTask<ContentValues, Void, Void> {
		@Override
		protected Void doInBackground(ContentValues... contentValues) {
			getActivity().getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, new String[]{String.valueOf(longClicked)});
			return null;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		Uri uri = LocationsContentProvider.CONTENT_URI;
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

}
