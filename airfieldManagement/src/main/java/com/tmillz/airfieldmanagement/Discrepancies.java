package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.tmillz.airfieldmanagement.Disc.OnMarkerSelectedListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
 
public class Discrepancies extends ActionBarActivity implements OnMarkerSelectedListener {
	 
    private ActionBar actionBar;
    private ViewPager viewPager;
    LocationsDB locationsDB;
    public ViewPagerAdapter vpa;
    public Bitmap bitmap;
    
    private static final int SELECT_PICTURE = 1;
	private static final int MAX_IMAGE_DIMENSION = 200;
    private String selectedImagePath;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean choose_theme = pref.getBoolean("choose_theme", false);
	    if(choose_theme == true){
	    	setTheme(R.style.AppTheme_Dark);
	    if(choose_theme == false){
	    	setTheme(R.style.AppTheme);
	    	}
	    }
	    
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discrepancies);
        
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setTitle("Map");

        actionBar.setDisplayHomeAsUpEnabled(true);
        
        locationsDB = new LocationsDB(getBaseContext());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(onPageChangeListener);
        vpa = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(vpa);
        addActionBarTabs();
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            // app icon in action bar clicked; go home
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            break;
        }
        return true;
    }
    
    private ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            actionBar.setSelectedNavigationItem(position);
            Maps maps = (Maps) vpa.getRegisteredFragment(0);
            if (position == 0) {
			maps.restartTheLoader();	
            }
        }
    };
 
    private void addActionBarTabs() {
        actionBar = this.getSupportActionBar();
        String[] tabs = { "Map", "Edit Marker", "Markers" };
        for (String tabTitle : tabs) {
            ActionBar.Tab tab = actionBar.newTab().setText(tabTitle).setTabListener(tabListener);
            actionBar.addTab(tab);
        }
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    }
 
    private ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            viewPager.setCurrentItem(tab.getPosition());
        }
 
        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
 
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        }
    };
    
    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

	@Override
	public void onMarkerSelected(Long id) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "Edit Marker " + id, Toast.LENGTH_SHORT).show();
		Log.v("clicked","pos: " + id);
		
		EditDisc ed = (EditDisc) vpa.getRegisteredFragment(1);
		
		ed.editMarker(id);
		//viewPager.setCurrentItem(1);
	}
	
	public void onClickEditPic(View imgBtn) {
		Log.v("SCHEMA", "imageButton click fired! in EditDiscCursor Adapter");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
	}
	
	public void onClickUpdateMarker(View Btn) {
		EditDisc edf = (EditDisc) vpa.getRegisteredFragment(1);
		Log.v("SCHEMA", "Button click fired! in Discrepancies");
		EditText editDisc = (EditText) edf.getView().findViewById(R.id.editDisc);
		EditText editLat = (EditText) edf.getView().findViewById(R.id.editLat);
		EditText editLng = (EditText) edf.getView().findViewById(R.id.editLng);
		EditText editColor = (EditText) edf.getView().findViewById(R.id.editColor);
		//ImageButton imageButton = (ImageButton) edf.getView().findViewById(R.id.imageButton);
		long id = edf.editmarker.getLong(edf.editmarker.getColumnIndex(LocationsDB.FIELD_ROW_ID));
		Log.v("SCHEMA", "int=" + id);
		String ed = editDisc.getText().toString();
		String lat = editLat.getText().toString();
		String lng = editLng.getText().toString();
		String ec = editColor.getText().toString();
		locationsDB.update_byID(id, ed, lat, lng, ec, null);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		EditDisc edf = (EditDisc) vpa.getRegisteredFragment(1);
		long id = edf.editmarker.getLong(edf.editmarker.getColumnIndex(LocationsDB.FIELD_ROW_ID));
	    if (resultCode == RESULT_OK) {
	        if (requestCode == SELECT_PICTURE) {
	            Uri selectedImageUri = data.getData();
	            Log.d("URI VAL", "selectedImageUri = " + selectedImageUri.toString());
	            selectedImagePath = getPath(selectedImageUri);
	            
	            if(selectedImagePath!=null){         
	                // IF LOCAL IMAGE, NO MATTER IF ITS DIRECTLY FROM GALLERY (EXCEPT PICASSA ALBUM),
	                // OR OI/ASTRO FILE MANAGER. EVEN DROPBOX IS SUPPORTED BY THIS BECAUSE DROPBOX DOWNLOAD THE IMAGE 
	                // IN THIS FORM - file:///storage/emulated/0/Android/data/com.dropbox.android/...
	                /*System.out.println("local image");
	                bitmap = MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(), ContentUris.parseId(selectedImageUri),
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            (BitmapFactory.Options) null );
	                Log.v("SCHEMA", "you've got this far");*/
	            	Bitmap bitmap = null;
					try {
						bitmap = scaleImage(this, selectedImageUri);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                EditDisc ed = (EditDisc) vpa.getRegisteredFragment(1);
	                ImageButton imageButton = (ImageButton) ed.getView().findViewById(R.id.imageButton);
	                imageButton.setImageBitmap(bitmap);
	                locationsDB.update_byID(id, null, null, null, null, selectedImageUri);
	                //Toast.makeText(this, selectedImagePath, Toast.LENGTH_SHORT).show();
	                
	            }
	            else{
	                System.out.println("picasa image!");
	                loadPicasaImageFromGallery(selectedImageUri);
		            locationsDB.update_byID(id, null, null, null, null, selectedImageUri);
		            Bitmap bitmap = null;
					try {
						bitmap = scaleImage(this, selectedImageUri);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                EditDisc ed = (EditDisc) vpa.getRegisteredFragment(1);
	                ImageButton imageButton = (ImageButton) ed.getView().findViewById(R.id.imageButton);
	                imageButton.setImageBitmap(bitmap);
	            }
	        }
	    }
	}
	
	// NEW METHOD FOR PICASA IMAGE LOAD
	private void loadPicasaImageFromGallery(final Uri uri) {
	    String[] projection = {  MediaColumns.DATA, MediaColumns.DISPLAY_NAME };
	    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
	    if(cursor != null) {
	        cursor.moveToFirst();

	        int columnIndex = cursor.getColumnIndex(MediaColumns.DISPLAY_NAME);
	        if (columnIndex != -1) {
	            new Thread(new Runnable() {
	                // NEW THREAD BECAUSE NETWORK REQUEST WILL BE MADE THAT WILL BE A LONG PROCESS & BLOCK UI
	                // IF CALLED IN UI THREAD 
	                public void run() {
	                    try {
	                    	EditDisc ed = (EditDisc) vpa.getRegisteredFragment(1);
	                    	ImageButton imageButton = (ImageButton) ed.getView().findViewById(R.id.imageButton);
	                        Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
	                        // THIS IS THE BITMAP IMAGE WE ARE LOOKING FOR.
	                        imageButton.setImageBitmap(bitmap);
	                    } catch (Exception ex) {
	                        ex.printStackTrace();
	                    }
	                }
	            }).start();
	        }
	    }
	    cursor.close();
	}

	public String getPath(Uri uri) {
	    String[] projection = {  MediaColumns.DATA};
	    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
	    if(cursor != null) {
	        //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
	        //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
	        cursor.moveToFirst();
	        int columnIndex = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
	        String filePath = cursor.getString(columnIndex);
	        cursor.close();
	        return filePath;
	    }
	    else 
	        return uri.getPath();               // FOR OI/ASTRO/Dropbox etc
	}

	@Override
	public void onActivityResult() {
		// TODO Auto-generated method stub
	}
	
	public static Bitmap scaleImage(Context context, Uri photoUri) throws IOException {
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);

        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }

        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);

            // Create the bitmap from file
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = (int) maxRatio;
            srcBitmap = BitmapFactory.decodeStream(is, null, options);
        } else {
            srcBitmap = BitmapFactory.decodeStream(is);
        }
        is.close();

        /*
         * if the orientation is not 0 (or -1, which means we don't know), we
         * have to do a rotation.
         */
        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);

            srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
                    srcBitmap.getHeight(), matrix, true);
        }

        String type = context.getContentResolver().getType(photoUri);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (type.equals("image/png")) {
            srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
            srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        }
        byte[] bMapArray = baos.toByteArray();
        baos.close();
        return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
    }

    public static int getOrientation(Context context, Uri photoUri) {
        /* it's on the external media. */
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }

        cursor.moveToFirst();
        return cursor.getInt(0);
    }
}