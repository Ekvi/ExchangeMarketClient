package com.ekvilan.exchangemarket.view;

import android.test.ActivityUnitTestCase;
import android.util.Log;

public class AddAdvertisementActivityTest extends ActivityUnitTestCase<AddAdvertisementActivity> {
    AddAdvertisementActivity activity = new AddAdvertisementActivity();

    public AddAdvertisementActivityTest(Class activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

    public void testGetUserId() {
        Log.d("my", activity.getUserId());
    }
}
