package com.example.amirlubashevsky.mobilogyproj;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by amirlubashevsky on 22/02/2018.
 */

public class UtilsCursor {

    protected static  ArrayList<Contact> getCallDetails(Activity activity) {
        ArrayList<Contact> contacts = new ArrayList<>();
        Cursor managedCursor = activity.managedQuery(CallLog.Calls.CONTENT_URI, null,
                null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        int callNameCache = managedCursor
                .getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME);

        while (managedCursor.moveToNext()) {
            String callName = managedCursor.getString(callNameCache);
            String phNumber = managedCursor.getString(number);
            int callDuration = Integer.parseInt( managedCursor.getString(duration));

            Contact contact = new Contact();
            contact.setPhoneNumber(phNumber);
            contact.setDurationTime(callDuration);
            contact.setName(callName);
            contacts.add(contact);

        }

        // Sort later by phone.
        Collections.sort(contacts, Contact.COMPARE_BY_PHONE);

        return contacts;
    }


    protected static ArrayList<Contact> readSMSMessages(Context context){
        ArrayList<Contact> lstContactsSMS = new ArrayList<>();
        Uri message = Uri.parse("content://sms/");
        Cursor cursor = context.getContentResolver().query(
                message,
                new String[] { "_id", "thread_id", "address", "person", "date", "body" },
                null,
                null,
                null);


        // run for all sms
        // add only first number
        // till next number count number of msg
        // add next number to array

        ArrayList<Contact> contactsTempSMS = new ArrayList<>();
        int counter = 1;
        int totalSMS = cursor.getCount();

        if (cursor.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
                String phoneNumber =   cursor.getString(cursor
                        .getColumnIndexOrThrow("address"));
                String phonePersonName =   cursor.getString(cursor
                        .getColumnIndexOrThrow("person"));
                String phoneID =   cursor.getString(cursor
                        .getColumnIndexOrThrow("_id"));

                Contact contact = new Contact(phoneID, phonePersonName, phoneNumber, 0);
                lstContactsSMS = createListOfContacts(contactsTempSMS, contact, phoneNumber, counter);

                cursor.moveToNext();
            }
        }
        return lstContactsSMS;
    }

    private static ArrayList<Contact> createListOfContacts(ArrayList<Contact> contactsTemp, Contact contact, String phoneNumber, int counter){
        if(phoneNumber != null) {
            if (contactsTemp.isEmpty()) {
                contactsTemp.add(contact);
            } else {
                if (contactsTemp.get(contactsTemp.size() - 1).getPhoneNumber().equals(phoneNumber)) {
                    counter++;
                } else {
                    contactsTemp.get(contactsTemp.size() - 1).setSmsCount(counter);
                    counter = 1;
                    contactsTemp.add(contact);
                }
            }
        }
        return contactsTemp;
    }



}
