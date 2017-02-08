package com.simon.xsmdemos;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by test on 2017/2/3.
 */

public class DemoSampleAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return DemoSampleItemArray.getInstance().samples.size();
    }

    @Override
    public Object getItem(int position) {
        return DemoSampleItemArray.getInstance().samples.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(DemoApplication.getApplication().getApplicationContext() ,R.layout.demo_sample_item, null);
        }

        TextView tv = (TextView)convertView;
        tv.setText(DemoSampleItemArray.getInstance().samples.get(position));
        return convertView;
    }
}
