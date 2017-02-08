package com.simon.xsmdemos;

import android.app.Application;
import android.os.Bundle;
import android.support.multidex.MultiDexApplication;

/**
 * Created by test on 2017/2/3.
 */

public class DemoApplication extends MultiDexApplication {

    private static DemoApplication _instance = null;

    public static DemoApplication getApplication(){
        return _instance;
    }

    @Override
    public void onCreate(){
        super.onCreate();
        _instance = this;
    }
}
