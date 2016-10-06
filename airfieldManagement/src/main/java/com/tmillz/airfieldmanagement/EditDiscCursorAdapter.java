package com.tmillz.airfieldmanagement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class EditDiscCursorAdapter extends ResourceCursorAdapter {
	
	private static final int MAX_IMAGE_DIMENSION = 200;
	TextView editlat;
	TextView editlng;
	TextView editDisc;
	TextView editColor;
	ImageButton imageButton;
	Discrepancies activity;

	public EditDiscCursorAdapter(Context context, int layout, Cursor cursor, int flags) {
		super(context, layout, cursor, flags);
	}
	
	@Override
	public void bindView(View view, Context context, Cursor arg1) {
		// TODO Auto-generated method stub
		Uri selectedImageUri = null;
		editDisc = (TextView) view.findViewById(R.id.editDisc);
		editDisc.setText(arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_DISC)));
		
		imageButton = (ImageButton) view.findViewById(R.id.imageButton);
			String selectedImageString = arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_PIC));
			if (selectedImageString!=null) {
			selectedImageUri = Uri.parse(selectedImageString);
			/*Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), ContentUris.parseId(selectedImageUri),
	                MediaStore.Images.Thumbnails.MINI_KIND,
	                (BitmapFactory.Options) null );*/
			Bitmap bitmap = null;
			try {
				bitmap = scaleImage(context, selectedImageUri);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			imageButton.setImageBitmap(bitmap);
		}
		
		editColor = (TextView) view.findViewById(R.id.editColor);
		editColor.setText(arg1.getString(arg1.getColumnIndex(LocationsDB.FIELD_COLOR)));
		
		editlat = (TextView) view.findViewById(R.id.editLat);
        editlat.setText(Double.toString(arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT))));
        
        editlng = (TextView) view.findViewById(R.id.editLng);
        editlng.setText(Double.toString(arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG))));
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
