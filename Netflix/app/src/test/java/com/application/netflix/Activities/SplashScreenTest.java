package com.application.netflix.Activities;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;
import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.application.netflix.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)

public class SplashScreenTest {
    @Rule
    public ActivityTestRule<SplashScreen> activityTestRule = new ActivityTestRule<>(SplashScreen.class);
    private  SplashScreen screen=null;
    Instrumentation.ActivityMonitor monitor =getInstrumentation().addMonitor(SignIn.class.getName(),null,false);
    @Before
    public void setUp() throws Exception {
        screen = activityTestRule.getActivity();

    }
    @Test
    public void test(){
        View v = screen.findViewById(R.id.forgotpsswrdtxtv);
        Activity signin= getInstrumentation().waitForMonitorWithTimeout(monitor,3000);
        assertNotNull(signin);
    }

    @After
    public void tearDown() throws Exception {
        screen =null;

    }
}