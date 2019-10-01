package com.tmillz.airfieldmanagement;

import android.app.PendingIntent;
import android.content.Intent;
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
import java.util.Objects;

public class MainActivity extends BaseActivity {

	public MainActivity(){
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState){

		// Enable for debugging memory leaks related to sqlite
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
	        pInfo = getPackageManager().getPackageInfo(getPackageName(),
					PackageManager.GET_META_DATA);
	        if (pref.getLong("lastRunVersionCode", 0) < pInfo.versionCode ) {
	            // Handle first-run situation here
	        	copyAssets();
	            Editor editor = pref.edit();
	            editor.putLong("lastRunVersionCode", pInfo.versionCode);
				Log.e("tag", "Version Code is " + pInfo.versionCode);
	            editor.apply();
	        }

	    } catch (NameNotFoundException e) {
	        // Something pretty serious went wrong
	        e.printStackTrace();
	    }

		super.onCreate(savedInstanceState);

		/*if (!expansionFilesDelivered()) {
			// Build an Intent to start this activity from the Notification
			Intent notifierIntent = new Intent(this, BaseActivity.class);
			notifierIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
					notifierIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			// Start the download service (if required)
			int startResult = 0;
			try {
				startResult = DownloaderClientMarshaller.startDownloadServiceIfRequired(this,
						pendingIntent, ExpDownloaderService.class);
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}
			// If download has started, initialize this activity to show
			// download progress
			if (startResult != DownloaderClientMarshaller.NO_DOWNLOAD_REQUIRED) {
				// This is where you do set up to display the download
				// progress (next step)
				// Instantiate a member instance of IStub

				downloaderClientStub = DownloaderClientMarshaller.CreateStub(this,
						ExpDownloaderService.class);
				// Inflate layout that shows download progress
				//setContentView(R.layout.downloader_ui);

				//return;
			} // If the download wasn't necessary, fall through to start the app
		}*/
	}

	private void copyAssets() {
		AssetManager assetManager = getAssets();
	    String[] files = null;
	    try {
	        files = assetManager.list("");
	    } catch (IOException e) {
	        Log.e("tag", "Failed to get asset file list.", e);
	    }
	    for(String filename : Objects.requireNonNull(files)) {
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
}
