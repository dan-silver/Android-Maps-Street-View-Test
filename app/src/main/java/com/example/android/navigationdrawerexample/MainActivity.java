/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.navigationdrawerexample;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

public class MainActivity extends Activity {
    public static final String LOG = "SILVER_LOG";
    private final static String TAG_CLUSTER_MAP = "TAG_CLUSTER_MAP";
    static public ImageLoader il;
    public static String STREET_VIEW_IMAGE_API_KEY;
    public ArrayList<Long> newLocationIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load api keys
        STREET_VIEW_IMAGE_API_KEY = getResources().getString(R.string.streetview_image_api);

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        il = ImageLoader.getInstance();
        il.init(config);

        //new favorites that were just added to the db & haven't been rendered
        newLocationIds = new ArrayList<>();

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            switchToFragment(new ClusterFragment());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void switchToExploreWithRecord(StreetViewLocationRecord r) {
        Bundle bundle = new Bundle();
        bundle.putLong("RECORD_ID", r.getId());
        switchToStreetView(bundle);
    }

    public void switchToExploreWithPoint(LatLng point) {
        Bundle bundle = new Bundle();
        bundle.putDouble("MANUAL_LAT", point.latitude);
        bundle.putDouble("MANUAL_LONG", point.longitude);
        switchToStreetView(bundle);
    }

    private void switchToStreetView(Bundle bundle) {
        ExploreFragment f = new ExploreFragment();
        f.setArguments(bundle);
        switchToFragment(f);
    }

    private void switchToFragment(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.content_frame, fragment, TAG_CLUSTER_MAP);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.v(MainActivity.LOG, "onBackPressed()");
        ClusterFragment cf = (ClusterFragment) getFragmentManager().findFragmentById(R.id.content_frame);
        cf.onResume();
    }
}