package com.tmillz.airfieldmanagement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

public class AircraftSpecsActivity extends AppCompatActivity {

    long id;
    DataBaseHelper mDbHelper;
    Cursor aircraftData;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean choose_theme = pref.getBoolean("choose_theme", false);
        if(choose_theme){
            setTheme(R.style.AppTheme_Dark);
        } else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        mDbHelper = new DataBaseHelper(this);
        mDbHelper.open();
        setContentView(R.layout.activity_list_toolbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.airport_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Aircraft Specs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(android.R.id.list);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getLong("id");
        String rowId = String.valueOf(id);
        String sql = "SELECT * FROM aircraft WHERE _id=?";
        aircraftData = mDbHelper.getData(sql, new String[] {rowId});
        aircraftData.moveToFirst();
        AircraftCursorAdapter adapter = new AircraftCursorAdapter(
                this, R.layout.aircraft_specs, aircraftData, 0 );
                listView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
