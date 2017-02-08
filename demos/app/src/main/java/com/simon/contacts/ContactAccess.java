package com.simon.contacts;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.simon.util.ThreadHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by test on 2017/2/6.
 */
public class ContactAccess {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    private Activity mCtx;

    ContactInterface mContactInterface;


    public interface ContactInterface{
        public void onContactResult(ArrayList<ContactItem> list);
    }

    public ContactAccess(Activity ctx, ContactInterface contactInterface){
        mCtx = ctx;
        mContactInterface = contactInterface;
    }

    public void fetchContacts(){

        int xx = ContextCompat.checkSelfPermission(mCtx, Manifest.permission.READ_CONTACTS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && xx
                != PackageManager.PERMISSION_GRANTED) {

            //需不需要解释的dialog
            if (shouldRequest()) return;

            //请求权限
            ActivityCompat.requestPermissions(mCtx, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }else
        {
            getNumber(mCtx.getContentResolver());
        }
    }

    /**
     * Read the name of all the contacts.
     *
     * @return a list of names.
     */
    private ArrayList<String> getContactNames() {
        ArrayList<String> contacts = new ArrayList<>();
        // Get the ContentResolver
        ContentResolver cr = mCtx.getContentResolver();
        // Get the Cursor of all the contacts
        Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        // Move the cursor to first. Also check whether the cursor is empty or not.
        if (cursor.moveToFirst()) {
            // Iterate through the cursor
            do {
                // Get the contacts name
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contacts.add(name);
            } while (cursor.moveToNext());
        }
        // Close the curosor
        cursor.close();

        return contacts;
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                fetchContacts();
            } else {
                Toast.makeText(mCtx, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getNumber(ContentResolver cr) {

        final ArrayList<ContactItem> itemArray = new ArrayList<>();
        Cursor phones = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {

            int index = -1;
            index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            ContactItem item = new ContactItem();
            itemArray.add(item);
            if(index != -1){
                String name=phones.getString( index );
                item.sName = name;
            }

            index = phones.getColumnIndex( ContactsContract.Contacts._ID );
            if(index != -1){
                String id=phones.getString( index );
                item.ID = id;
            }

            int phoneCount = phones.getInt(phones.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
            getPhoneList(item, phones, cr, phoneCount);
        }

        Collections.sort(itemArray, new MapComparator());

        ThreadHelper.postOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContactInterface.onContactResult(itemArray);
            }
        });
        phones.close();// close cursor
    }


    private void getPhoneList(ContactItem item, Cursor cur, ContentResolver cr, int phoneNoCount){

        if (phoneNoCount > 0) {

            String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));

            Cursor pCur = cr.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",
                    new String[]{id}, null);

            while (pCur.moveToNext()) {
                String phoneNo = pCur.getString(pCur.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                item.appendPhoneNo(phoneNo);
            }
            pCur.close();

        }
    }

    public void deleteContacts(ArrayList<ContactItem> list){
        final ArrayList ops = new ArrayList();
        final ContentResolver cr = mCtx.getContentResolver();
        int nSize = list.size();
        String[] arr = new String[1];


        for(int i=0; i<nSize; i++){
            arr[0] = list.get(i).ID;
            delete(arr, ops);
        }



        try {
            cr.applyBatch(ContactsContract.AUTHORITY, ops);
//            background_process();
            ops.clear();
        } catch (OperationApplicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        } catch (RemoteException e) {
            // System.out.println(" length :"+i);
        }
    }


    private void delete(String[] arr, ArrayList ops) {

        ops.add(ContentProviderOperation
                .newDelete(ContactsContract.RawContacts.CONTENT_URI)
                .withSelection(
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = ?",
                        arr)
                .build());

    }


    private boolean shouldRequest() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mCtx, Manifest.permission.READ_CONTACTS)) {
            //显示一个对话框，给用户解释
            explainDialog();
            return true;
        }
        return false;
    }

    private void explainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setMessage("应用需要获取您的 通讯录 权限,是否授权？")
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //请求权限
                    ActivityCompat.requestPermissions(mCtx, new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            }).setNegativeButton("取消", null)
            .create().show();
    }

    class MapComparator implements Comparator<ContactItem> {

        public int compare(ContactItem lhs, ContactItem rhs) {
            return lhs.sName.compareTo(rhs.sName);
        }

    }


}
