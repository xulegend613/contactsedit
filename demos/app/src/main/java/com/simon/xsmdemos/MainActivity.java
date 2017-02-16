package com.simon.xsmdemos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.simon.contacts.ContactsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView lv = (ListView) this.findViewById(R.id.listview);
        DemoSampleAdapter adapter = new DemoSampleAdapter();
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    //open hot fix activity
                    HotFixDemoActivity.startHotFixActivity(MainActivity.this);
                }else if(position == 1){
                    ContactsActivity.startContactsActivity(MainActivity.this);
                }
            }
        });
//        OrionSdk.enableLog();

    }



}
