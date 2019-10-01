package com.tmillz.airfieldmanagement;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.widget.NavigationView;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.Helpers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener {

    private DrawerLayout mDrawerLayout;
    private final int mTitleRes;
    private FragmentManager fragmentManager;
    private static final int RC_SIGN_IN = 1;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 2;
    private ImageView profileImage;
    private TextView userName;
    private TextView userEmail;

    public BaseActivity(int titleRes){
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean choose_theme = pref.getBoolean("choose_theme", false);
        if(choose_theme){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            setTheme(R.style.AppTheme_Dark);
        } else {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            setTheme(R.style.AppTheme);
        }

        actionBar.setHomeButtonEnabled(true);
        setTitle(mTitleRes);
        
        Fragment fragment = new Regulations();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        NavigationView mNavigationView = findViewById(R.id.nav_view);
        profileImage = mNavigationView.getHeaderView(
                0).findViewById(R.id.profile_image);
        userName = mNavigationView.getHeaderView(
                0).findViewById(R.id.username);
        userEmail = mNavigationView.getHeaderView(
                0).findViewById(R.id.email);

        // highlights the first item in the Navigation Menu when app is opened
        mNavigationView.getMenu().getItem(0).setChecked(true);

        mNavigationView.setNavigationItemSelectedListener(
                item -> {
                    Intent a;
                    switch(item.getItemId()){
                        case android.R.id.home:
                            break;
                        case R.id.nav_regulations:
                            switchContent(new Regulations());
                            setTitle(R.string.regulations);
                            break;
                        case R.id.nav_aircraft:
                            switchContent(new AircraftList());
                            setTitle(R.string.aircraft);
                            break;
                        case R.id.nav_calculators:
                            switchContent(new Calculators());
                            setTitle(R.string.calculators);
                            break;
                        case R.id.nav_notams:
                            a = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                    "https://www.notams.faa.gov"));
                            startActivity(a);
                            break;
                        case R.id.nav_bowmonk:
                            switchContent(new Bowmonk());
                            setTitle(R.string.bowmonk_converter);
                            break;
                        case R.id.nav_links:
                            switchContent(new QuickRefrences());
                            setTitle("Links");
                            break;
                        case R.id.nav_map:
                            switchContent(new ViewPagerFragment());
                            setTitle(R.string.map);
                            break;
                        case R.id.nav_forms:
                            switchContent(new Forms());
                            setTitle(R.string.forms);
                            break;
                        case R.id.nav_rate:
                            a = new Intent(Intent.ACTION_VIEW, Uri.parse(
                                    "http://play.google.com/store/apps/details?id=" +
                                            "com.tmillz.airfieldmanagement"));
                            startActivity(a);
                            break;
                        case R.id.nav_email:
                            a = new Intent(Intent.ACTION_SEND);
                            a.setType("message/rfc822");
                            a.putExtra(Intent.EXTRA_EMAIL, new String[]
                                    {"terrymil1981@gmail.com"});
                            a.putExtra(Intent.EXTRA_SUBJECT,
                                    "Airfield Management App Android");
                            a.putExtra(Intent.EXTRA_TEXT, "Android");
                            startActivity(Intent.createChooser(a, "Send Email"));
                            break;
                    }
                    mDrawerLayout.closeDrawers();
                    return true;
                });

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(@NonNull View drawerView) {}
            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}
            @Override
            public void onDrawerStateChanged(int newState) {}
        });

        /*GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();*/

        // Hanlde Airport Click intent to open map
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            if (data.toString().contains("geo")) {
                switchContent(new ViewPagerFragment());
                setTitle(R.string.map);
            }
        }

        userName.setOnClickListener(v -> {
            Intent intent1 = new Intent(getApplicationContext(),
                    LoginActivity.class);
            startActivityForResult(intent1, RC_SIGN_IN);
        });

        this.refreshNavHeader();

        // Set back button
        fragmentManager.addOnBackStackChangedListener(() -> {
            if (fragmentManager.getBackStackEntryCount() > 0) {
                if(choose_theme){
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
                } else {
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
                }

            } else {
                actionBar.setDisplayHomeAsUpEnabled(true);
                // actionBar.setTitle("Regulations");
                if(choose_theme){
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
                } else {
                    actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
                }

            }
        });

        // Check if expansion files are available before going any further
        /*if (!expansionFilesDelivered()) {
            // Build an Intent to start this activity from the Notification
            Intent notifierIntent = new Intent(this, this.getClass());
            notifierIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notifierIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // Start the download service (if required)
            int startResult =
                    0;
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
                return;
            } // If the download wasn't necessary, fall through to start the app
        }
        //startApp(); // Expansion files are available, start the app
        */

	}

    private void switchContent(Fragment fragment){
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                if (fragmentManager.getBackStackEntryCount() > 0) {
                    fragmentManager.popBackStack();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), ShowSettings.class));
                break;
            case R.id.privacy:
                Intent a;
                a = new Intent(Intent.ACTION_VIEW, Uri.parse(
                        "https://sites.google.com/view/tmillz/airfield-manager"));
                startActivity(a);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main, menu);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager)
                .getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
	}

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode,
                                    final Intent data) {

        if (requestCode == 1 ) {
            if(resultCode == Activity.RESULT_OK){
                this.refreshNavHeader();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Sign in Canceled",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(getApplicationContext(), "Success!!!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,
                        RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            apiAvailability.getErrorDialog(this, RC_SIGN_IN,
                    RESOLVE_CONNECTION_REQUEST_CODE).show();
        }
    }

    private void refreshNavHeader(){
        //Checks if user is logged in with Facebook
        if(Profile.getCurrentProfile() != null && AccessToken.getCurrentAccessToken()
                != null){
            //The user is logged in with facebook
             new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    "/{user-id}/picture",
                    null,
                    HttpMethod.GET,
                     response -> {
                         /* handle the result */
                         String userId = Profile.getCurrentProfile().getId();
                         Glide.with(getApplicationContext()).load(
                                 "https://graph.facebook.com/" + userId
                                         + "/picture?width=200&height=150")
                                 .into(profileImage);
                         userName.setText(Profile.getCurrentProfile().getName());
                         userEmail.setText(R.string.fb_logged_in);
                     }
             ).executeAsync();
        }

        //Checks if user is logged in with Google
        else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (Objects.requireNonNull(currentUser).getPhotoUrl() != null) {
                Glide.with(getApplicationContext()).load(currentUser.getPhotoUrl())
                        .into(profileImage);
            }
            userName.setText(currentUser.getDisplayName());
            userEmail.setText(R.string.gg_signed_in);
        }

        else {
            profileImage.setImageResource(R.drawable.ic_account_circle_black_24dp);
            userName.setText(R.string.sign_in);
            userEmail.setText("");
        }
    }

    boolean expansionFilesDelivered() {
        for (XAPKFile xf : xAPKS) {
            String fileName = Helpers.getExpansionAPKFileName(this, xf.mIsMain,
                    xf.mFileVersion);
            if (!Helpers.doesFileExist(this, fileName, xf.mFileSize, false))
                return false;
        }
        return true;
    }

    private static class XAPKFile {
        public final boolean mIsMain;
        public final int mFileVersion;
        public final long mFileSize;

        XAPKFile(boolean isMain, int fileVersion, long fileSize) {
            mIsMain = isMain;
            mFileVersion = fileVersion;
            mFileSize = fileSize;
        }
    }

    private static final XAPKFile[] xAPKS = {
            new XAPKFile(
                    true, // true signifies a main file
                    23, // the version of the APK that the file was uploade against
                    687801613L // the length of the file in bytes
            ),
            new XAPKFile(
                    false, // false signifies a patch file
                    23, // the version of the APK that the patch file was uploaded against
                    512860L // the length of the patch file in bytes
            )
    };

    /*@Override
    public void onBackPressed(){
        if (fragmentManager.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            //fragmentManager.popBackStack();
            Fragment fragment = fragmentManager.findFragmentByTag("Regulations");
            if (fragment instanceof RegsSub) {
                    setTitle("Regulations");
            }
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }

        Log.d("Test", "Back button pressed");
    }*/

}
