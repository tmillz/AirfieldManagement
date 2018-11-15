package com.tmillz.airfieldmanagement;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class EditMarkerActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    static String rowId;
    private ImageButton imageButton;
    private static final int SELECT_PICTURE = 1;
    private TextView editMarker;
    private TextView editLat;
    private TextView editLng;
    private TextView editDate;
    private TextView editNotes;
    private TextView editIdBy;
    private Calendar calendar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean choose_theme = pref.getBoolean("choose_theme", false);
        if(choose_theme){
            setTheme(R.style.AppTheme_Dark);
        } else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_marker);

        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        if (args != null) {
            long id = args.getLong("id");
            rowId = String.valueOf(id);
        }

        getSupportLoaderManager().initLoader(0, null, this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.edit_marker);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editDate = findViewById(R.id.date);
        calendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
        };

        editDate.setOnClickListener(v -> {
        // TODO Auto-generated method stub
        new DatePickerDialog(EditMarkerActivity.this, date, calendar
                .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        imageButton = findViewById(R.id.imageButton);

        imageButton.setOnClickListener(view -> {

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                PackageManager.PERMISSION_GRANTED) {

            Intent intent1 = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent1.setType("image/*");
            intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent1.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent1.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent1, SELECT_PICTURE);

        } else {
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 123);
        }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_marker_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {
                // If request is cancelled the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //permission granted
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                    intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivityForResult(intent, SELECT_PICTURE);

                } else {

                    // permission was denied disable the
                    // functionality that depends on this permission.
                    AlertDialog alertdialog = new AlertDialog.Builder(
                            EditMarkerActivity.this).create();
                    alertdialog.setMessage("Permission needed to choose picture");
                    alertdialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                            (dialog, which) -> dialog.dismiss());
                    alertdialog.show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {

            if (data == null) {
                Log.e("Error", "No image");
                return;
            }
            try{
                Uri selectedImageUri = data.getData();
                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);

                int takeFlags = data.getFlags();
                takeFlags &= (Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                // Check for the freshest data
                getContentResolver().takePersistableUriPermission(selectedImageUri, takeFlags);

                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageButton.setImageBitmap(selectedImage);

                String imageUri = selectedImageUri.toString();
                ContentValues contentValues = new ContentValues();
                contentValues.put(LocationsDB.FIELD_PIC, imageUri);
                LocationUpdateTask updateTask = new LocationUpdateTask();
                updateTask.execute(contentValues);
                getSupportLoaderManager().restartLoader(0, null, this);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class LocationUpdateTask extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            String selection = rowId;
            getContentResolver().update(LocationsContentProvider.CONTENT_URI, contentValues[0],
                    selection, null);

            return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        switch (item.getItemId()){
            case R.id.action_delete:
                // Do delete action
                break;
            case R.id.action_save:
                //Do save action
                // Creating an instance of ContentValues
                ContentValues contentValues = new ContentValues();
                // Setting latitude in ContentValues
                // Setting title text
                contentValues.put(LocationsDB.FIELD_DISC, editMarker.getText().toString());
                //  Setting latitude in ContentValues
                contentValues.put(LocationsDB.FIELD_LAT, editLat.getText().toString());
                // Setting longitude in ContentValues
                contentValues.put(LocationsDB.FIELD_LNG, editLng.getText().toString());
                // Setting Date in Content Values
                contentValues.put(LocationsDB.FIELD_DATE, editDate.getText().toString());
                // Setting Id'd by (used to be zoom) in ContentValues
                contentValues.put(LocationsDB.FIELD_ZOOM, editIdBy.getText().toString());
                // Setting Notees (used to be color) for marker
                contentValues.put(LocationsDB.FIELD_COLOR, editNotes.getText().toString());
                // Creating an instance of LocationInsertTask
                LocationUpdateTask updateTask = new LocationUpdateTask();
                // Storing the changes to SQLite database
                updateTask.execute(contentValues);

                AlertDialog alertdialog = new AlertDialog.Builder(
                        EditMarkerActivity.this).create();
                alertdialog.setMessage("Your marker has been updated");
                alertdialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertdialog.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = LocationsContentProvider.CONTENT_URI_SELECT;
        return new CursorLoader(this, uri, null, rowId, null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor arg1) {

        editMarker = findViewById(R.id.editTitle);
        editMarker.setText(arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_DISC)));

        editLat = findViewById(R.id.editLat);
        editLat.setText(Double.toString(arg1.getDouble(arg1.getColumnIndex(
                LocationsDB.FIELD_LAT))));

        editLng = findViewById(R.id.editLng);
        editLng.setText(Double.toString(arg1.getDouble(arg1.getColumnIndex(
                LocationsDB.FIELD_LNG))));

        editDate = findViewById(R.id.date);
        editDate.setText(arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_DATE)));

        editIdBy = findViewById(R.id.idBy);
        editIdBy.setText(arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_ZOOM)));

        editNotes = findViewById(R.id.editNotes);
        editNotes.setText(arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_COLOR)));

        imageButton = findViewById(R.id.imageButton);
        String selectedImageString = arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_PIC));

        if (selectedImageString != null) {
            Uri selectedImageUri = Uri.parse(selectedImageString);
            try {

                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageButton.setImageBitmap(selectedImage);

            } catch (IOException | SecurityException ex) {
                Log.e("TAG", "woops");
                // permission was denied disable the
                // functionality that depends on this permission.
                AlertDialog alertdialog = new AlertDialog.Builder(
                        EditMarkerActivity.this).create();
                alertdialog.setMessage("Please reselect the picture to grant permission");
                alertdialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertdialog.show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    private void updateLabel() {
        String myFormat = "dd MMM yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        editDate.setText(sdf.format(calendar.getTime()));
    }
}
