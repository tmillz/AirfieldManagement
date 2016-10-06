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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends BaseActivity{
	
	final static String TARGET_BASE_PATH = "/sdcard/odk/forms/";
	
	public MainActivity(){
		super(R.string.app_name);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		boolean choose_theme = pref.getBoolean("choose_theme", false);
	    if(choose_theme == true){
	    	setTheme(R.style.AppTheme_Dark);
	    if(choose_theme == false){
	    	setTheme(R.style.AppTheme);
	    	}
	    }
		super.onCreate(savedInstanceState);

		//Experimental
		PackageInfo pInfo;
	    try {
	        pInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
	        if (pref.getLong("lastRunVersionCode", 0) < pInfo.versionCode ) {
	            // TODO: Handle your first-run situation here
	        	copyAssets();
	        	copyFilesToSdCard();
	            Editor editor = pref.edit();
	            editor.putLong("lastRunVersionCode", pInfo.versionCode);
				Log.e("tag", "Version Code is" + pInfo.versionCode);
	            editor.commit();
	        }
			/*if (pref.getLong("lastRunVersionCode", 0) > pInfo.versionCode ) {
				copyAssets();
				copyFilesToSdCard();
				Editor editor = pref.edit();
				editor.putLong("LastRunVersionCode", pInfo.versionCode);
				Log.e("tag", "Version Code is" + pInfo.versionCode);
				editor.commit();
			}*/
	    } catch (NameNotFoundException e) {
	        // TODO Something pretty serious went wrong if you got here...
	        e.printStackTrace();
	    }
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		//getSupportFragmentManager().putFragment(outState, "mContent", mContent);
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
	        InputStream in = null;
	        OutputStream out = null;
	        try {
	          in = assetManager.open(filename);
	          File outFile = new File(getExternalFilesDir(null), filename);
	          out = new FileOutputStream(outFile);
	          copyFile(in, out);
	          in.close();
	          in = null;
	          out.flush();
	          out.close();
	          out = null;
	          
	          TestAdapter mDbHelper = new TestAdapter(getBaseContext());        
	          mDbHelper.createDatabase();      
	          mDbHelper.open();
	          mDbHelper.close();
	          
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
	
	private void copyFilesToSdCard() {
	    copyFileOrDir(""); // copy all files in assets folder in my project
	}

	private void copyFileOrDir(String path) {
	    AssetManager assetManager = this.getAssets();
	    String assets[] = null;
	    try {
	        Log.i("tag", "copyFileOrDir() "+path);
	        assets = assetManager.list(path);
	        if (assets.length == 0) {
	            copyFile(path);
	        } else {
	            String fullPath =  TARGET_BASE_PATH + path;
	            Log.i("tag", "path="+fullPath);
	            File dir = new File(fullPath);
	            if (!dir.exists() && !path.startsWith("images") && !path.startsWith("sounds") && !path.startsWith("webkit"))
	                if (!dir.mkdirs())
	                    Log.i("tag", "could not create dir "+fullPath);
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
		        /*if (filename.endsWith(".jpg")) // extension was added to avoid compression on APK file
		            newFileName = TARGET_BASE_PATH + filename.substring(0, filename.length()-4);
		        else*/
		            newFileName = TARGET_BASE_PATH + filename;
		        out = new FileOutputStream(newFileName);

		        byte[] buffer = new byte[1024];
		        int read;
		        while ((read = in.read(buffer)) != -1) {
		            out.write(buffer, 0, read);
		        }
		        in.close();
		        in = null;
		        out.flush();
		        out.close();
		        out = null;
		    }
	    }
	         catch (Exception e) {
	        Log.e("tag", "Exception in copyFile() of "+newFileName);
	        Log.e("tag", "Exception in copyFile() "+e.toString());
	    }
	}
}
