package com.simon.xsmdemos;

import java.util.ArrayList;

/**
 * Created by test on 2017/2/3.
 */

public class DemoSampleItemArray {
    private static String TAG = "DemoSampleItemArray";
    private static DemoSampleItemArray _instance = null;
    public  ArrayList<String> samples = new ArrayList<String>();

    private DemoSampleItemArray(){
        samples.add("hotfix____new_dex_solution");
        samples.add("contacts_demo");
    }

    public static DemoSampleItemArray getInstance(){
        if(_instance == null){
            synchronized (TAG){
                if(_instance == null){
                    _instance = new DemoSampleItemArray();
                }
            }
        }
        return _instance;
    }




}
