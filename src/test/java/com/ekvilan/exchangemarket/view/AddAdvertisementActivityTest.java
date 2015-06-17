package com.ekvilan.exchangemarket.view;



import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.ekvilan.exchangemarket.view.activities.AddAdvertisementActivity;

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


}
