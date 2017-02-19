package com.tmillz.airfieldmanagement;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
 
public class SearchResultsActivity extends ListActivity {

    private TextView txtQuery;
    private ListView list;
    public  String latlong;
    
    private String sql = "SELECT * FROM airports WHERE LOWER(field6)LIKE ?";
 
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        
        txtQuery = (TextView) findViewById(R.id.txtQuery);
        list = (ListView) findViewById(android.R.id.list);
        
        handleIntent(getIntent());
        
        list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				String maplink = "http://maps.google.com/?q="+latlong;
	        	Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(maplink));
	        	startActivity(myIntent);	
			}
        });
    }
 
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            getAirport(query);
        }
    }
    
    private void getAirport(String query) {
    	
 		AircraftListAdapter mDbHelper = new AircraftListAdapter(getBaseContext());
        mDbHelper.open();
     	
     	Cursor testdata = mDbHelper.getAirport(sql, new String[] {query});
     	if (testdata.moveToFirst()==false){
     		txtQuery.setText("Sorry, No Results Found");
     	} else {
	     	if (testdata !=null) {
				testdata.moveToFirst();
				
				//Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(testdata));
				
				latlong = testdata.getString(testdata.getColumnIndex("field7")) + "," + testdata.getString(testdata.getColumnIndex("field8"));

	            AirportCursorAdapter adapter = new AirportCursorAdapter(
	                    this, R.layout.activity_search_results, testdata, 0 );
	
	            this.setListAdapter(adapter);
	        } 
     	}
    }
}