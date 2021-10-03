package com.tecmanic.goservices;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.tecmanic.goservices.Extra.FetchAddressTask;
import com.tecmanic.goservices.Extra.GPSTracker;
import com.tecmanic.goservices.Extra.Session_management;
import com.tecmanic.goservices.fragment.BookingFragment;
import com.tecmanic.goservices.fragment.CategoryFragment;
import com.tecmanic.goservices.fragment.HomeFragment;
import com.tecmanic.goservices.fragment.ProfileFragment;

import static com.tecmanic.goservices.Extra.Config.ADDRESS;
import static com.tecmanic.goservices.Extra.Config.CITY;
import static com.tecmanic.goservices.Extra.Config.COUNTRY;
import static com.tecmanic.goservices.Extra.Config.KEY_PINCODE;
import static com.tecmanic.goservices.Extra.Config.LAT;
import static com.tecmanic.goservices.Extra.Config.LONG;
import static com.tecmanic.goservices.Extra.Config.MyPrefreance;
import static com.tecmanic.goservices.Extra.Config.STATE;

public class HomePageActivity extends AppCompatActivity implements FetchAddressTask.OnTaskCompleted {


    FrameLayout frameLayout;

    LinearLayout linear, city_linear;
    TextView title, location,skip;
    List<Address> addresses = new ArrayList<>();
    String latitude, longitude, address, city, state, country, postalCode;
    LocationManager locationManager;
    Session_management sessionManagement;
    private static final int REQUEST_LOCATION_PERMISSION = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    String lat,lng;
    double lat1;
    double lng1;

    TextView cancel,tLogin,tSignUP;
    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet,ll_loc;

    SharedPreferences placePref;
    ChipNavigationBar bottomNav;
   /* protected BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {

                case R.id.navigation_shop:

                    city_linear.setVisibility(View.VISIBLE);
                    title.setText("");
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_gifts:

                    city_linear.setVisibility(View.GONE);
                    title.setText("Category");
                    fragment = new CategoryFragment();
                    loadFragment(fragment);

                    return true;
                case R.id.navigation_cart:

                    if (sessionManagement.isLoggedIn()) {
                        city_linear.setVisibility(View.GONE);
                        title.setText("MyBooking");
                        fragment = new BookingFragment();
                        loadFragment(fragment);

                    } else {
                        Intent i = new Intent(HomePageActivity.this, LoginActivity.class);
                        startActivity(i);
                    }

                    return true;
                case R.id.navigation_profile:
                    if (sessionManagement.isLoggedIn()) {
                        city_linear.setVisibility(View.GONE);
                        title.setText("Profile");
                        fragment = new ProfileFragment();
                        loadFragment(fragment);

                    } else {
                        Intent i = new Intent(HomePageActivity.this, LoginActivity.class);
                        startActivity(i);
                    }


                    return true;
            }

            return false;
        }
    };
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        sessionManagement=new Session_management(this);

        cancel=findViewById(R.id.cancel);
        tLogin=findViewById(R.id.Login);
        tSignUP=findViewById(R.id.SignUP);

        linear = findViewById(R.id.linear);
        city_linear = findViewById(R.id.city_linear);
        title = findViewById(R.id.title);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        bottom_sheet.setVisibility(View.VISIBLE);

        placePref = getSharedPreferences("getlatlng",MODE_PRIVATE);
        lat=placePref.getString("getlat","");
        lng=placePref.getString("getlng","");


        bottomNav = findViewById(R.id.menu_bottom);
        location = findViewById(R.id.location);

        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setPeekHeight(300);

            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        } else {
            sheetBehavior.setPeekHeight(0);

            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        sheetBehavior.setPeekHeight(0);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        sheetBehavior.setPeekHeight(300);

                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        sheetBehavior.setPeekHeight(0);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:

                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        if(sessionManagement.isLoggedIn()){
            bottom_sheet.setVisibility(View.GONE);
        }
        else {
            bottom_sheet.setVisibility(View.VISIBLE);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sheetBehavior.setPeekHeight(0);
                // sheetBehavior.isHideable();
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                // bottom_sheet.setVisibility(View.GONE);
            }
        });
        tLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(in);
            }
        });
        tSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(getApplicationContext(),SignUpActivity.class);
                startActivity(in);
            }
        });

        if (savedInstanceState == null) {
            bottomNav.setItemSelected(R.id.navigation_shop, true);
            city_linear.setVisibility(View.VISIBLE);
            title.setText("");
            Fragment fragment = new HomeFragment();
            loadFragment(fragment);
        }
        bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment fragment;
                switch (id) {

                    case R.id.navigation_shop:

                        city_linear.setVisibility(View.VISIBLE);
                        title.setText("");
                        fragment = new HomeFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.navigation_gifts:

                        city_linear.setVisibility(View.GONE);
                        title.setText("Category");
                        fragment = new CategoryFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.navigation_cart:

                        if (sessionManagement.isLoggedIn()) {
                            city_linear.setVisibility(View.GONE);
                            title.setText("MyBooking");
                            fragment = new BookingFragment();
                            loadFragment(fragment);
                            break;
                        } else {
                            Intent i = new Intent(HomePageActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                    case R.id.navigation_profile:
                        if (sessionManagement.isLoggedIn()) {
                            city_linear.setVisibility(View.GONE);
                            title.setText("Profile");
                            fragment = new ProfileFragment();
                            loadFragment(fragment);
                            break;
                        } else {
                            Intent i = new Intent(HomePageActivity.this, LoginActivity.class);
                            startActivity(i);
                        }
                }
            }
        });

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, LocationActivity.class);
                startActivityForResult(intent,345);
            }
        });


       // sessionManagement.LatLng(lat,lng);
        GPSTracker mGPS = new GPSTracker(getApplicationContext());
        if (mGPS.canGetLocation) {
            mGPS.getLocation();

            latitude = String.valueOf(mGPS.getLatitude());
            longitude = String.valueOf(mGPS.getLongitude());

         //   Log.d("lat", latitude);
         //   Log.d("long", longitude);
            sessionManagement.LatLng(latitude,longitude);

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(mGPS.getLatitude(), mGPS.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                String locality = addresses.get(0).getAddressLine(0);
                location.setText(locality);
            }
            if (address != null) {
                address = addresses.get(0).getAddressLine(0); //0 to obtain first possible address
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();                // commented on 27 sept 19 bcoz of screen crash
                // create your custom title
                String title = address + "-" + city + "-" + state;
                Log.d("addresss", title + country + "-" + postalCode);

                SharedPreferences.Editor editor = getSharedPreferences(MyPrefreance, MODE_PRIVATE).edit();
                editor.putString(ADDRESS, address);
                editor.putString(CITY, city);
                editor.putString(STATE, state);
                editor.putString(KEY_PINCODE, postalCode);
                editor.putString(COUNTRY, country);
                editor.putString(LAT, latitude);
                editor.putString(LONG, longitude);
                editor.apply();
                editor.commit();
            }
        }

        //location.setText(address+","+city+","+state);

        getLocation();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
//                Log.e(TAG, "location create"+location.getLatitude()+" , "+location.getLongitude() );
                if (location != null) {
                    new FetchAddressTask(HomePageActivity.this, HomePageActivity.this).execute(location);
                }
            }
        });
        if (latitude.isEmpty()){
          //  location.setText("Set Location");

            if(!lat.isEmpty()){
                sessionManagement.LatLng(lat,lng);
                Geocoder gcd = new Geocoder(HomePageActivity.this, Locale.getDefault());

                List<Address> addresses = null;
                try {
                    addresses = gcd.getFromLocation(Double.parseDouble(lat),Double.parseDouble(lng) , 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null && addresses.size() > 0) {
                    String locality = addresses.get(0).getAdminArea();
                    location.setText(locality);
                }
            }
            else{
                location.setText("Set Location");

            }
        }
        else {
            Geocoder gcd = new Geocoder(HomePageActivity.this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(Double.parseDouble(latitude),Double.parseDouble(longitude) , 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                String locality = addresses.get(0).getAddressLine(0);
                location.setText(locality);
            }
            bottomNav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int id) {
                    Fragment fragment;
                    switch (id) {

                        case R.id.navigation_shop:

                            city_linear.setVisibility(View.VISIBLE);
                            title.setText("");
                            fragment = new HomeFragment();
                            loadFragment(fragment);
                            break;
                        case R.id.navigation_gifts:

                            city_linear.setVisibility(View.GONE);
                            title.setText("Category");
                            fragment = new CategoryFragment();
                            loadFragment(fragment);
                            break;
                        case R.id.navigation_cart:

                            if (sessionManagement.isLoggedIn()) {
                                city_linear.setVisibility(View.GONE);
                                title.setText("MyBooking");
                                fragment = new BookingFragment();
                                loadFragment(fragment);
                                break;
                            } else {
                                Intent i = new Intent(HomePageActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        case R.id.navigation_profile:
                            if (sessionManagement.isLoggedIn()) {
                                city_linear.setVisibility(View.GONE);
                                title.setText("Profile");
                                fragment = new ProfileFragment();
                                loadFragment(fragment);
                                break;
                            } else {
                                Intent i = new Intent(HomePageActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                    }
                }
            });

            if (savedInstanceState == null) {
                bottomNav.setItemSelected(R.id.navigation_shop, true);
                city_linear.setVisibility(View.VISIBLE);
                title.setText("");
                Fragment fragment = new HomeFragment();
                loadFragment(fragment);
            }
        }




        loadFragment(new HomeFragment());

      /*  BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, displayMetrics);
            iconView.setLayoutParams(layoutParams);


        }
*/

    }


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framelayout, fragment);
        transaction.commit();
    }

    @Override
    public void onTaskCompleted(String result) {
      //  Log.e("TAG", "onTaskCompleted: " + result);

      //  ((TextView) findViewById(R.id.location)).setText(result);
    }


    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    //  item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("BNVHelper", "Unable to get shift mode field", e);
            } catch (IllegalAccessException e) {
                Log.e("BNVHelper", "Unable to change value of shift mode", e);
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {// If the permission is granted, get the location,
            // otherwise, show a Toast
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    getLocation();
                Log.e("TAG", "Granted");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
//                                Log.e(TAG, "location create" + location.getLatitude() + " , " + location.getLongitude());
                            new FetchAddressTask(HomePageActivity.this, HomePageActivity.this)
                                    .execute(location);
                        }
                    }
                });


            } else {
//                    Log.e(TAG, "permission denied" );

                Toast.makeText(HomePageActivity.this, "Location permission is necessary", Toast.LENGTH_SHORT).show();
                finish();

            }
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            Log.d("TAG", "getLocation: permissions granted");
        }

            bottomNav.setItemSelected(R.id.navigation_shop, true);
            city_linear.setVisibility(View.VISIBLE);
            title.setText("");
            Fragment fragment = new HomeFragment();
            loadFragment(fragment);

    }


   public void onResume() {
        super.onResume();
       placePref = getSharedPreferences("getlatlng",MODE_PRIVATE);

       lat=placePref.getString("getlat","");
       lng=placePref.getString("getlng","");
       sessionManagement.LatLng(lat, lng);
        if (!lat.isEmpty()) {
            sessionManagement.LatLng(lat, lng);
            Geocoder gcd = new Geocoder(HomePageActivity.this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                String locality = addresses.get(0).getAddressLine(0);
                location.setText(locality);
            }
            loadFragment(new HomeFragment());

        }
    }
    public void onPause() {
        super.onPause();
        placePref = getSharedPreferences("getlatlng",MODE_PRIVATE);

        lat=placePref.getString("getlat","");
        lng=placePref.getString("getlng","");
        sessionManagement.LatLng(lat, lng);
        if (!lat.isEmpty()) {
            sessionManagement.LatLng(lat, lng);
            Geocoder gcd = new Geocoder(HomePageActivity.this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                String locality = addresses.get(0).getAddressLine(0);
               location.setText(locality);
            }
            loadFragment(new HomeFragment());

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 345) {
            //lat = String.valueOf(data.getDoubleExtra("getlat", 0));
           // lng = String.valueOf(data.getDoubleExtra("getlng", 0));
          //  lat1=getIntent().getDoubleExtra("getlat");
          //  lng1=getIntent().getStringExtra("getlng");
            placePref = getSharedPreferences("getlatlng",MODE_PRIVATE);

            lat=placePref.getString("getlat","");
            lng=placePref.getString("getlng","");
            sessionManagement.LatLng(lat, lng);
            Geocoder gcd = new Geocoder(HomePageActivity.this, Locale.getDefault());

            List<Address> addresses = null;
            try {
                addresses = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses != null && addresses.size() > 0) {
                String locality = addresses.get(0).getAddressLine(0);
               location.setText(locality);
            }
            loadFragment(new HomeFragment());

            return;


        }


        //value = true;
    }
}
