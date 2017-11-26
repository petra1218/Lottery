package com.petra.lottery;

import android.app.Application;

import com.tencent.bugly.Bugly;

/**
 * Created by qiang on 2017/11/4.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "0433432f85", true);
    }
}
