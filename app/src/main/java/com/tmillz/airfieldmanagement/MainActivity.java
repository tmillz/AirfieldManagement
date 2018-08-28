package com.tmillz.airfieldmanagement;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.maps.MapView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends BaseActivity {

	public MainActivity(){
		super(R.string.app_name);
	}
	private static final String TAG = "MainActivity";

	@Override
	public void onCreate(Bundle savedInstanceState){

		/*StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
				.detectLeakedSqlLiteObjects()
				.detectLeakedClosableObjects()
				.penaltyLog()
				.penaltyDeath()
				.build());*/
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean choose_theme = pref.getBoolean("choose_theme", false);
	    if(choose_theme){
	    	setTheme(R.style.AppTheme_Dark);
	    } else setTheme(R.style.AppTheme);

		// Check versionCode
		PackageInfo pInfo;
	    try {
	        pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
	        if (pref.getLong("lastRunVersionCode", 0) < pInfo.versionCode ) {
	            // TODO: Handle your first-run situation here
	        	copyAssets();
	        	//copyFilesToSdCard();
	            Editor editor = pref.edit();
	            editor.putLong("lastRunVersionCode", pInfo.versionCode);
				Log.e("tag", "Version Code is " + pInfo.versionCode);
	            editor.apply();
	        }

	    } catch (NameNotFoundException e) {
	        // TODO Something pretty serious went wrong if you got here...
	        e.printStackTrace();
	    }

		// Fixing Later Map loading Delay
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					MapView mv = new MapView(getApplicationContext());
					mv.onCreate(null);
					mv.onPause();
					mv.onDestroy();
				}catch (Exception ignored){

				}
			}
		}).start();

		super.onCreate(savedInstanceState);
	}

	private void copyAssets() {
		AssetManager assetManager = getAssets();
	    String[] files = null;
	    try {
	        files = assetManager.list("");
	    } catch (IOException e) {
	        Log.e("tag", "Failed to get asset file list.", e);
	    }
	    for(String filename : files) {
	        InputStream in;
	        OutputStream out;
	        try {
	          in = assetManager.open(filename);
	          File outFile = new File(getExternalFilesDir(null), filename);
	          out = new FileOutputStream(outFile);
	          copyFile(in, out);
	          in.close();
	          out.flush();
	          out.close();

	          DataBaseHelper mDbHelper = new DataBaseHelper(getBaseContext());
	          mDbHelper.createDataBase();
	          //mDbHelper.open();
	          //mDbHelper.close();
	          
	        } catch(IOException e) {
	            Log.e("tag", "Failed to copy asset file: " + filename, e);
	        }       
	    }
	}
	
	private void copyFile(InputStream in, OutputStream out) throws IOException {
	    byte[] buffer = new byte[1024];
	    int read;
	    while((read = in.read(buffer)) != -1){
	      out.write(buffer, 0, read);
	    }
	}
	
	//private void copyFilesToSdCard() {
		// copy all files in assets folder in project
		//copyFileOrDir("");
	//}

	private void copyFileOrDir(String path) {
	    AssetManager assetManager = this.getAssets();
	    String assets[];
	    try {
	        Log.i("tag", "copyFileOrDir() "+path);
	        assets = assetManager.list(path);
	        if (assets.length == 0) {
	            copyFile(path);
	        } else {
	            for (int i = 0; i < assets.length; ++i) {
	                String p;
	                if (path.equals(""))
	                    p = "";
	                else 
	                    p = path + "/";

	                if (!path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
	                    copyFileOrDir( p + assets[i]);
	            }
	        }
	    } catch (IOException ex) {
	        Log.e("tag", "I/O Exception", ex);
	    }
	}

	private void copyFile(String filename) {
	    AssetManager assetManager = this.getAssets();

	    InputStream in = null;
	    OutputStream out = null;
	    String newFileName = null;
	    try {
	    	if (filename.endsWith(".xml")) {
	    		Log.i("tag", "copyFile() "+filename);
		        in = assetManager.open(filename);
		        out = new FileOutputStream(newFileName);

		        byte[] buffer = new byte[1024];
		        int read;
		        while ((read = in.read(buffer)) != -1) {
		            out.write(buffer, 0, read);
		        }
		        in.close();
		        out.flush();
		        out.close();
		    }
	    } catch (Exception e) {
	        Log.e("tag", "Exception in copyFile() of "+newFileName);
	        Log.e("tag", "Exception in copyFile() "+e.toString());
	    }
	}
}
