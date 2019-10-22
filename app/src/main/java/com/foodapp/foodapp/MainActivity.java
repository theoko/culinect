package com.foodapp.foodapp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.foodapp.foodapp.services.PlaceSearchService;
import com.foodapp.foodapp.ui.auth.login.ui.login.LoginActivity;
import com.foodapp.foodapp.ui.groups.SendFragment;
import com.foodapp.foodapp.ui.home.HomeFragment;
import com.foodapp.foodapp.ui.reviews.GalleryFragment;
import com.foodapp.foodapp.ui.settings.ShareFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private String TAG_FRAGMENT = "mainFragment";
    private AppBarConfiguration mAppBarConfiguration;
    private TextView userDisplayName;
    private TextView localityTxt;

    // Navigation links
    private TextView action_feed;
    private TextView action_reviews;
    private TextView action_groups;
    private TextView action_settings;
    private TextView action_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        userDisplayName = navigationView.findViewById(R.id.userDisplayName);
        localityTxt = navigationView.findViewById(R.id.localityTxt);

        // Links
        action_feed = navigationView.findViewById(R.id.action_feed);
        action_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Fragment feedFragment = new HomeFragment();
                fragmentTransition(feedFragment);
            }
        });

        action_reviews = navigationView.findViewById(R.id.action_reviews);
        action_reviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Fragment reviewsFragment = new GalleryFragment();
                fragmentTransition(reviewsFragment);
            }
        });

        action_groups = navigationView.findViewById(R.id.action_groups);
        action_groups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Fragment groupsFragment = new SendFragment();
                fragmentTransition(groupsFragment);
            }
        });

        action_settings = navigationView.findViewById(R.id.action_settings);
        action_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Fragment settingsFragment = new ShareFragment();
                fragmentTransition(settingsFragment);
            }
        });

        action_logout = navigationView.findViewById(R.id.action_logout);
        action_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        getSupportActionBar().setTitle("Culinect");
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // Check if user is authenticated
        checkAuth();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLocation();
    }

    private void checkAuth() {
        // Check if user has already signed in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            // Move to login screen
            Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else {
            // Update UI
            String email = user.getEmail();
            userDisplayName.setText(email);
        }
    }

    private void updateLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Geocoder gcd = new Geocoder(MainActivity.this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                String locality = addresses.get(0).getLocality();
                Log.d(getClass().getName(), "ADDRESS: " + locality);
                localityTxt.setText(locality);
            } else {
                // do your stuff
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void startPlaceSearchService() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // mark first time has ran.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.apply();

            // Schedule search for nearest places and ask user to create a review
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, PlaceSearchService.class);
            PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 5), pi);
        }
    }

    private void fragmentTransition(Fragment someFragment) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        androidx.fragment.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, someFragment); // give your fragment container id in first parameter
        fragmentTransaction.addToBackStack(null);  // if written, this transaction will be added to backstack
        fragmentTransaction.remove(fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            searchView.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        } else {
            searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public void onClick_review(View view) {
        Log.d(getClass().getName(), "Clicked on review!");
    }
}
