package com.simon.contacts;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.simon.xsmdemos.DemoApplication;
import com.simon.xsmdemos.DemoSampleItemArray;
import com.simon.xsmdemos.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by test on 2017/2/4.
 */
public class ContactsAdapter extends BaseAdapter {

    public ArrayList<ContactItem>  itemArrayList;

    @Override
    public int getCount() {
        if(itemArrayList != null){
            return itemArrayList.size();
        }else{
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(itemArrayList == null){
            return null;
        }else{
            return itemArrayList.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(DemoApplication.getApplication().getApplicationContext() , R.layout.contacts_item, null);
        }

        TextView tv = (TextView)convertView.findViewById(R.id.name);
        setTextForView(tv, itemArrayList.get(position).sName);

        TextView phoneNo = (TextView)convertView.findViewById(R.id.phone_no);
        setTextForView(phoneNo, itemArrayList.get(position).getDefalutPhoneNos());

        return convertView;
    }

    private void setTextForView(TextView tv, String s){
        if(tv != null && s != null){
            tv.setText(s);
        }
    }
}
