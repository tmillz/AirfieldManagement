package com.tmillz.airfieldmanagement;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.Objects;

public class SearchResultsActivity extends AppCompatActivity {

    // private TextView txtQuery;
    private ListView listView;
    public  String latlong;
    DataBaseHelper mDbHelper;
    Cursor testdata;

    @Override
	public void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean choose_theme = pref.getBoolean("choose_theme", false);
        if(choose_theme){
            setTheme(R.style.AppTheme_Dark);
        } else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_toolbar);

        listView = findViewById(android.R.id.list);

        Toolbar toolbar = findViewById(R.id.airport_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.airports);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listView.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            Uri location = Uri.parse("geo:"+ latlong);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
            startActivity(mapIntent);
        });

        mDbHelper = new DataBaseHelper(getBaseContext());
        mDbHelper.open();

        handleIntent(getIntent());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
 
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean choose_theme = pref.getBoolean("choose_theme", false);
        if(choose_theme){
            getApplicationContext().setTheme(R.style.AppTheme_Dark);
        } else getApplicationContext().setTheme(R.style.AppTheme);

        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            String sql = "SELECT * FROM airports WHERE LOWER(field6)LIKE ?";
            testdata = mDbHelper.getData(sql, new String[] {query});
            startManagingCursor(testdata);

            if (!testdata.moveToFirst()){
                Log.v("TAG", "test data null");
                //txtQuery.setText("Sorry, No Results Found");
            } else {
                testdata.moveToFirst();
                latlong = testdata.getString(testdata.getColumnIndex("field7")) + ","
                        + testdata.getString(testdata.getColumnIndex("field8"));
                listView = findViewById(android.R.id.list);
                AirportCursorAdapter adapter = new AirportCursorAdapter(
                        this, R.layout.activity_search_results, testdata, 0);
                listView.setAdapter(adapter);
            }
        }
    }
}