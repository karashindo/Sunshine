package com.example.asaf.sunshine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MyActivity extends ActionBarActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        if (findViewById(R.id.weather_detail_container) != null) {
            // The weather detail view exists, so it means our layout is a tablet UI (sw600dp)
            mTwoPane = true;

            // In two-pane mode, we need to create the detail fragment but only if there is no
            // previously saved state. If there is a saved state, we let the system framework
            // reconstruct the fragment by not doing anything.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.weather_detail_container, new DetailFragment())
                        .commit();
            }
        } else {
            // Single pane mode
            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_show_location) {
            // Read location from SharePreferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            String locationPref = sharedPref.getString(getString(R.string.pref_location_key),
                    getString(R.string.pref_location_default));
            showMap(locationPref);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showMap(String location) {
        final String MAP_GEO_BASE_URI = "geo:0,0?";
        Uri geoLocationUri = Uri.parse(MAP_GEO_BASE_URI).buildUpon()
            .appendQueryParameter("q", "us zip code " + location).build();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocationUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.error_map_intent), Toast.LENGTH_SHORT).show();
        }
    }

}
