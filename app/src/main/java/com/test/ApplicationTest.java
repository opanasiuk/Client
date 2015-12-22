package com.test;

import android.support.design.widget.FloatingActionButton;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.ViewAsserts;
import android.widget.EditText;

import com.example.alexander.road.R;
import com.google.android.gms.maps.GoogleMap;
import com.road.GoogleDirection;
import com.road.MapsActivity;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MapsActivity> {


    FloatingActionButton fab;
    MapsActivity activity;
    GoogleMap map;
    String apikey;

    GoogleDirection gd;

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
        apikey = activity.ApiKey;
        gd = activity.gd;
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



    public void testStartingEmpty() {
        TouchUtils.clickView(this, fab);
        assertNotNull(activity.mMap);

    }

    public void testAPI () {
	assertNotNull(apikey);
    }

    public void testAPIKey() {
        assertEquals("AIzaSyCYpA4RVDcQApm204s1YykQmUXcttEqj1A", apikey);
    }

    public void testNullGoogleDirection() {
        assertNotNull(gd);
    }

    public void testLocation() {
        assertNotNull(activity.getLastBestLocation());
    }
    
    
	

}