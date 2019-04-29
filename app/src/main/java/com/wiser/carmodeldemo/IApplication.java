package com.wiser.carmodeldemo;

import android.app.Application;

import com.wiser.library.helper.WISERHelper;

public class IApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        WISERHelper.newBind().Inject(this, BuildConfig.DEBUG);
    }
}
