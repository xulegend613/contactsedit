package com.simon.contacts;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by test on 2017/2/4.
 */
public class ContactItem {

    public String  ID;
    public String  sEmail;
    public String  sNickName;
    private ArrayList<String> phoneNos = new ArrayList<>();
    public String  sName;
    public String  sAddress;
    public String  sOrgnization;


    public ContactItem(){

    }

    public ContactItem(String name){
        sName = name;
    }

    public void appendPhoneNo(String sNo){
        phoneNos.add(sNo);
    }

    public String getDefalutPhoneNos(){
        if(phoneNos.size() > 0){
            return phoneNos.get(0);
        }else{
            return "";
        }
    }

    public int getPhoneNoSize(){
        return phoneNos.size();
    }

    public boolean isEqual(ContactItem item){

        if(item == null ){
            return false;
        }
        if(!TextUtils.equals(sName, item.sName))
            return false;

        if(phoneNos.size() != item.phoneNos.size())
            return false;

        int size = phoneNos.size();
        for(int i=0; i<size; i++){
            if(!TextUtils.equals(item.phoneNos.get(i), phoneNos.get(i))){
                return false;
            }
        }

        return true;
    }

}


