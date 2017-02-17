package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

public class ViewPagerFragment extends Fragment {

    LocationsDB locationsDB;
    public ViewPagerAdapter vpa;

    private static final int SELECT_PICTURE = 1;
	private static final int MAX_IMAGE_DIMENSION = 200;
    private String selectedImagePath;
	EditMarker edf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.view_pager, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);
        viewPager.setAdapter(buildAdapter());
        viewPager.setOnPageChangeListener(onPageChangeListener);
		EditMarker edf = (EditMarker) vpa.getRegisteredFragment(1);

		ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("Map");

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        locationsDB = new LocationsDB(getActivity().getBaseContext());

        return view;
    }

    private PagerAdapter buildAdapter() {
		vpa = new ViewPagerAdapter(getActivity(), getChildFragmentManager());
		EditMarker edf = (EditMarker) vpa.getRegisteredFragment(1);
		return vpa;
    }

    private ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            Maps maps = (Maps) vpa.getRegisteredFragment(0);
            if (position == 0) {
			maps.restartTheLoader();
            }
        }
    };

	public void onClickEditPic(View imgBtn) {
		Log.v("SCHEMA", "imageButton click fired! in EditMarkerCursor Adapter");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
	}
	
	public void onClickUpdateMarker(View Btn) {
		EditMarker edf = (EditMarker) vpa.getRegisteredFragment(1);
		Log.v("SCHEMA", "Button click fired! in ViewPagerFragment");
		EditText editMarker = (EditText) edf.getView().findViewById(R.id.editMarker);
		EditText editLat = (EditText) edf.getView().findViewById(R.id.editLat);
		EditText editLng = (EditText) edf.getView().findViewById(R.id.editLng);
		EditText editColor = (EditText) edf.getView().findViewById(R.id.editColor);
		long id = edf.editmarker.getLong(edf.editmarker.getColumnIndex(LocationsDB.FIELD_ROW_ID));
		Log.v("SCHEMA", "int=" + id);
		String ed = editMarker.getText().toString();
		String lat = editLat.getText().toString();
		String lng = editLng.getText().toString();
		String ec = editColor.getText().toString();
		locationsDB.update_byID(id, ed, lat, lng, ec, null);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        edf = (EditMarker) getActivity().getSupportFragmentManager().findFragmentById(R.id.editMarker);
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
						bitmap = scaleImage(getContext(), selectedImageUri);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                EditMarker ed = (EditMarker) vpa.getRegisteredFragment(1);
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
						bitmap = scaleImage(getContext(), selectedImageUri);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	                EditMarker ed = (EditMarker) vpa.getRegisteredFragment(1);
	                ImageButton imageButton = (ImageButton) ed.getView().findViewById(R.id.imageButton);
	                imageButton.setImageBitmap(bitmap);
	            }
	        }
	    }
	}
	
	// NEW METHOD FOR PICASA IMAGE LOAD
	private void loadPicasaImageFromGallery(final Uri uri) {
	    String[] projection = {  MediaColumns.DATA, MediaColumns.DISPLAY_NAME };
	    Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
	    if(cursor != null) {
	        cursor.moveToFirst();

	        int columnIndex = cursor.getColumnIndex(MediaColumns.DISPLAY_NAME);
	        if (columnIndex != -1) {
	            new Thread(new Runnable() {
	                // NEW THREAD BECAUSE NETWORK REQUEST WILL BE MADE THAT WILL BE A LONG PROCESS & BLOCK UI
	                // IF CALLED IN UI THREAD 
	                public void run() {
	                    try {
	                    	EditMarker ed = (EditMarker) vpa.getRegisteredFragment(1);
	                    	ImageButton imageButton = (ImageButton) ed.getView().findViewById(R.id.imageButton);
	                        Bitmap bitmap = android.provider.MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
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
	    Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
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