package com.simon.contacts;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.simon.xsmdemos.DemoApplication;
import com.simon.xsmdemos.R;

import java.util.ArrayList;


/**
 * Created by test on 2017/2/4.
 */
public class ContactsActivity extends AppCompatActivity {
    private final static String  Tag = "ContactsActivity";
    private ContactsAdapter  adapter = new ContactsAdapter();
    private ListView lv = null;
    private ContactAccess   contactAccess = null;
    private Button          mBtn;

    private final int   SEARCH_STATUS = 100;
    private final int   Searching_STATUS = 102;
    private final int   Deleteing_STATUS = 103;
    private final int   CLEAN_STATUS = 101;

    private int status      = SEARCH_STATUS;



    public static void startContactsActivity(Context ctx){
        Intent intent = new Intent();
        intent.setClass(DemoApplication.getApplication().getApplicationContext(), ContactsActivity.class);
        ctx.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle bundle){

        super.onCreate(bundle);

        setContentView(R.layout.activity_contacts);
        lv =  (ListView)findViewById(R.id.listview);
        lv.setAdapter(adapter);
        contactAccess = new ContactAccess(this, new ContactAccess.ContactInterface() {
            @Override
            public void onContactResult(ArrayList<ContactItem> list) {
                adapter.itemArrayList = list;
                adapter.notifyDataSetChanged();
                status = SEARCH_STATUS;
            }
        });
        contactAccess.fetchContacts();
        mBtn = (Button)findViewById(R.id.btn_done);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status == SEARCH_STATUS){
                    status = Searching_STATUS;
                    adapter.itemArrayList = findUselessContacts();
                    adapter.notifyDataSetChanged();
                    mBtn.setText("clean");

                    status = CLEAN_STATUS;

                }else if(status == CLEAN_STATUS){

                    contactAccess.deleteContacts(adapter.itemArrayList);
                }
            }
        });
    }

    private ArrayList<ContactItem> findUselessContacts(){

        ArrayList<ContactItem> list = (ArrayList<ContactItem>)adapter.itemArrayList.clone();
        ArrayList<ContactItem> tmpList = new ArrayList<>(100);
        int size = list.size();
        for (int i=0; i<size; i++) {
            for(int j=(i+1); j<size;j++){
                if(list.get(i).getPhoneNoSize() == 0){
                    tmpList.add(list.get(i));
                    break;
                }else if(list.get(i).isEqual(list.get(j))){
                    tmpList.add(list.get(j));
                }else{
                    break;
                }
            }
        }
        return tmpList;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        contactAccess.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
