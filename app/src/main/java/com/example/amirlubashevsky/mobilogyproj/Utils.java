package com.example.amirlubashevsky.mobilogyproj;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;

/**
 * Created by amirlubashevsky on 22/02/2018.
 */

public class Utils {

    public static void getVCF(Context context) {

        final String vfile = "Contacts.vcf";
        Cursor phones = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null, null);
        phones.moveToFirst();
        for (int i = 0; i < phones.getCount(); i++) {
            String lookupKey = phones.getString(phones
                    .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(
                    ContactsContract.Contacts.CONTENT_VCARD_URI,
                    lookupKey);
            AssetFileDescriptor fd;
            try {
                fd = context.getContentResolver().openAssetFileDescriptor(uri, "r");
                FileInputStream fis = fd.createInputStream();
                byte[] buf = new byte[(int) fd.getDeclaredLength()];
                fis.read(buf);
                String VCard = new String(buf);
                String path = Environment.getExternalStorageDirectory()
                        .toString() + File.separator + vfile;
                FileOutputStream mFileOutputStream = new FileOutputStream(path,
                        true);
                mFileOutputStream.write(VCard.toString().getBytes());
                phones.moveToNext();
                Log.d("Vcard", VCard);
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }

    public static int[] splitToComponentTimes(int biggy)
    {
        long longVal = biggy;
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;

        int[] ints = {hours , mins , secs};
        return ints;
    }

//    private void smsReading(Context context){
//        System.out.println("Kate smsReading");
//        Uri SMS_CONVERSATION= Uri.parse("content://sms/conversations/");
//
//        Cursor c = context.getContentResolver().query(SMS_CONVERSATION, null, null, null, null);
//
//        context.startManagingCursor(c);
//        String[] count = new String[c.getCount()];
//        String[] snippet = new String[c.getCount()];
//        String[] thread_id = new String[c.getCount()];
//
//        c.moveToFirst();
//        for (int i = 0; i < c.getCount(); i++) {
//            count[i] = c.getString(c.getColumnIndexOrThrow("msg_count"))
//                    .toString();
//            thread_id[i] = c.getString(c.getColumnIndexOrThrow("thread_id"))
//                    .toString();
//            snippet[i] = c.getString(c.getColumnIndexOrThrow("snippet"))
//                    .toString();
//            Log.v("count", count[i]);
//            Log.v("thread", thread_id[i]);
//            Log.v("snippet", snippet[i]);
//            c.moveToNext();
//        }
//
//
//        Uri SMS_INBOX = Uri.parse("content://sms/inbox");
//
//        for(int ad = 0; ad < thread_id.length ; ad++) {
//            // Uri uri = Uri.parse("content://sms/inbox");
//            String where = "thread_id=" + thread_id[ad];
//            Cursor mycursor = context.getContentResolver().query(SMS_INBOX, null, where, null, null);
//            context.startManagingCursor(mycursor);
//
//            String[] number = new String[mycursor.getCount()];
//
//
//            if (mycursor.moveToFirst()) {
//                for (int i = 0; i < mycursor.getCount(); i++) {
//                    number[i] = mycursor.getString(mycursor.getColumnIndexOrThrow("address")).toString();
//                    Log.v("number", number[i]);
//                    mycursor.moveToNext();
//                }
//            }
//        }
//    }
}
