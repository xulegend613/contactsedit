package com.simon.xsmdemos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by test on 2017/2/3.
 */

public class HotFixDemoActivity extends Activity {


    public static void startHotFixActivity(Context ctx){
        Intent intent = new Intent();
        intent.setClass(DemoApplication.getApplication().getApplicationContext(), HotFixDemoActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);

        setContentView(R.layout.activity_hot_fix);
    }


    @Override
    public void onResume(){
        super.onResume();


    }

    @Override
    public void onStop(){
        super.onStop();


    }

    @Override
    public void onDestroy(){
        super.onDestroy();

    }

    @Override
    public void onPause(){
        super.onPause();
    }
}
