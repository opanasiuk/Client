package com.test;

import android.support.design.widget.FloatingActionButton;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;

import com.example.alexander.road.R;
import com.google.android.gms.maps.GoogleMap;
import com.road.MapsActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MapsActivity> {


    FloatingActionButton fab;
    MapsActivity activity;
    GoogleMap map;


    public ApplicationTest() {
        super("com.example.alexander", MapsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        // TODO Auto-generated method stub
        super.setUp();
    activity = getActivity();
    fab = (FloatingActionButton) activity.findViewById(R.id.fab);
    map = activity.mMap;

}

    public void testActivityNotNull() {
        assertNotNull(activity);
    }
    public void testFABNotNull() {
        assertNotNull(fab);
    }

    public void testGoogleAPINotNull() {
        assertNotNull(map);
    }

    public void testControlsVisible() {
        ViewAsserts
                .assertOnScreen(fab.getRootView(), fab);


    }
    public void testLocation() {
        assertEquals(activity.getLastBestLocation(), activity.getLastBestLocation());
    }


    public void testStartingEmpty() {
        TouchUtils.clickView(this, fab);
        assertNotNull(activity.mMap);

    }

}