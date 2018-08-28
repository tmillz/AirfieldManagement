package com.tmillz.airfieldmanagement;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener {
	
    private DrawerLayout mDrawerLayout;
    GoogleApiClient mGoogleApiClient;
    private static final String TAG = "MainActivity";
    private int mTitleRes;
    private NavigationView mNavigationView;
    FragmentManager fragmentManager;
    private static final int RC_SIGN_IN = 1;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 2;
    private ImageView profileImage;

    public BaseActivity(int titleRes){
		mTitleRes = titleRes;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean choose_theme = pref.getBoolean("choose_theme", false);
        if(choose_theme){
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_white);
            setTheme(R.style.AppTheme_Dark);
        } else {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            setTheme(R.style.AppTheme);
        }
        actionBar.setHomeButtonEnabled(true);
        setTitle(mTitleRes);
        
        Fragment fragment = new MainView();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        profileImage = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.profile_image);
        TextView userName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.username);
        TextView userEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.email);


        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        Intent a;
                        switch(item.getItemId()){
                            case android.R.id.home:
                                break;
                            case R.id.nav_regulations:
                                switchContent(new MainView());
                                setTitle("Regulations");
                                break;
                            case R.id.nav_aircraft:
                                switchContent(new AircraftList());
                                setTitle("Aircraft");
                                break;
                            case R.id.nav_calculators:
                                switchContent(new Calculators());
                                setTitle("Calculators");
                                break;
                            case R.id.nav_notams:
                                a = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.notams.faa.gov"));
                                startActivity(a);
                                break;
                            case R.id.nav_bowmonk:
                                switchContent(new Bowmonk());
                                setTitle("Bowmonk Converter");
                                break;
                            case R.id.nav_links:
                                switchContent(new QuickRefrences());
                                setTitle("Links");
                                break;
                            case R.id.nav_map:
                                switchContent(new ViewPagerFragment());
                                setTitle("Map");
                                break;
                            case R.id.nav_forms:
                                switchContent(new Forms());
                                setTitle("Forms");
                                break;
                            case R.id.nav_rate:
                                a = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.tmillz.airfieldmanagement"));
                                startActivity(a);
                                break;
                            case R.id.nav_email:
                                a = new Intent(Intent.ACTION_SEND);
                                a.setType("message/rfc822");
                                a.putExtra(Intent.EXTRA_EMAIL, new String[] {"terrymil1981@gmail.com"});
                                a.putExtra(Intent.EXTRA_SUBJECT, "Airfield Management App Android");
                                a.putExtra(Intent.EXTRA_TEXT, "Android");
                                startActivity(Intent.createChooser(a, "Send Email"));
                                break;
                            case R.id.action_settings:
                                startActivity(new Intent(getApplicationContext(), ShowSettings.class));
                                break;
                            case R.id.nav_login:
                                a = new Intent(getApplication(), LoginActivity.class);
                                startActivity(a);
                                break;
                            case R.id.nav_logout:
                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(status -> {
                                    FirebaseAuth.getInstance().signOut();
                                });
                                break;
                            case R.id.nav_login2:
                                signIn();
                                break;
                        }
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {}
            @Override
            public void onDrawerOpened(View drawerView) {
                //Checks if user is logged in with Facebook
                if(Profile.getCurrentProfile() != null && AccessToken.getCurrentAccessToken() != null){
                    //The user is logged in with facebook
                    new GraphRequest(
                            AccessToken.getCurrentAccessToken(),
                            "/{user-id}/picture",
                            null,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
                            /* handle the result */
                                    String userId = (String) Profile.getCurrentProfile().getId();
                                    Glide.with(getApplicationContext()).load("https://graph.facebook.com/" + userId + "/picture?width=200&height=150").into(profileImage);
                                    userName.setText(Profile.getCurrentProfile().getName());
                                    userEmail.setText("logged in with Facebook");
                                    mNavigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                                    mNavigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                                }
                            }
                    ).executeAsync();
                }

                //Checks if user is logged in with Google
                else if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    Glide.with(getApplicationContext()).load(currentUser.getPhotoUrl()).into(profileImage);
                    userName.setText(currentUser.getDisplayName());
                    userEmail.setText(currentUser.getEmail());
                    mNavigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
                    mNavigationView.getMenu().findItem(R.id.nav_logout).setVisible(true);
                }

                else {
                    profileImage.setImageResource(R.drawable.profile_pic);
                    userName.setText("Sign In");
                    userEmail.setText("");
                    userName.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v){
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
            @Override
            public void onDrawerClosed(View drawerView) {}
            @Override
            public void onDrawerStateChanged(int newState) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Hanlde Airport Click intent to open map
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            if (data.toString().contains("geo")) {
                switchContent(new ViewPagerFragment());
                setTitle("Map");
            }
        }
	}

    public void switchContent(Fragment fragment){
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), ShowSettings.class));
                break;
            case R.id.privacy:
                Intent a;
                a = new Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/tmillz/airfield-manager"));
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
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
	}

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case RESOLVE_CONNECTION_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    mGoogleApiClient.connect();
                }
                break;
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
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    private void signIn() {
        mGoogleApiClient.connect();
    }

}
