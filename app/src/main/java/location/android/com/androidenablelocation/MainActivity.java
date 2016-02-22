package location.android.com.androidenablelocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;





public class MainActivity extends AppCompatActivity {
    TextView loc_text;
    private LocationManager locationMangaer = null;
    private LocationListener locationListener = null;
    private Boolean flag = false;
    public static final String MyPREFERENCES = "UserData";
    //    public static final String Name = "nameKey";
//    public static final String Phone = "phoneKey";
//    public static final String Email = "emailKey";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enable_location);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
// Created shared prefences----
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedpreferences.edit();

        loc_text = (TextView) findViewById(R.id.textView13);
        Button E_gps = (Button) findViewById(R.id.enable_gps);
        Button pick_loc = (Button) findViewById(R.id.pick_location);
        locationMangaer = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        E_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = displayGpsStatus();
                if (flag) {

                    Log.v("on click", "onClick");

//                    loc_text.setText("Please!! move your device to" +
//                            " see the changes in coordinates." + "\nWait..");


                    locationListener = new MyLocationListener();

                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationMangaer.requestLocationUpdates(LocationManager
                            .GPS_PROVIDER, 5000, 10, locationListener);
                    finish();
                    Intent i1 = new Intent(MainActivity.this, MainActivity.class);
                    i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i1);

                } else {
                    alertbox("Gps Status!!", "Your GPS is: OFF");
                }


            }
        });
        pick_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i1 = new Intent(MainActivity.this, PickLocation.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

            }
        });

    }

    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    /*----------Method to create an AlertBox ------------- */
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
//                                Intent myIntent = new Intent(
//                                        Settings.ACTION_SECURITY_SETTINGS);
//                                startActivity(myIntent);
//                                Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
//                                intent.putExtra("enabled", true);
//                                getBaseContext().sendBroadcast(intent);
                                Intent location = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(location);
                                dialog.cancel();
//                                finish();
//                                Intent i1 = new Intent(EnableLocation.this, MainActivity.class);
//                                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(i1);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
//                                finish();
//                                Intent i1 = new Intent(EnableLocation.this, MainActivity.class);
//                                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(i1);
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*----------Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {

            // loc_text.setText("");
            // pb.setVisibility(View.INVISIBLE);
//            Toast.makeText(getBaseContext(), "Location changed : Lat: " +
//                            loc.getLatitude() + " Lng: " + loc.getLongitude(),
//                    Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();
            Log.v("longitude", longitude);
            String latitude = "Latitude: " + loc.getLatitude();
            Log.v("latitude", latitude);

    /*----------to get City-Name from coordinates ------------- */
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = longitude + "\n" + latitude +
                    "\n\nMy Currrent City is: " + cityName;
            // loc_text.setText(s);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }


    // Try this also for enable GPS
//
//    public void startGPS ()
//    {
//        //---use the LocationManager class to obtain GPS locations---
//        if (lm == null)
//        {
//            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//            locationListener = new MyLocationListener();
//
//            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
//            {
//                Log.i("message", "starting up GPS location provider...");
//                lm.requestLocationUpdates(
//                        LocationManager.GPS_PROVIDER,
//                        LOCATION_UPDATE_TIME, LOCATION_UPDATE_DISTANCE, locationListener);
//
//                currentLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//                updateLocation (currentLocation);
//            }
//
//            if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
//            {
//
//            }
//        }
//    }
}
