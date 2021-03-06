package com.example.amirlubashevsky.mobilogyproj;

import android.database.Cursor;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amirlubashevsky on 22/02/2018.
 */

public class ArraysCompareUtils {

    /**
     * Util for Array manipulation
     * @param contact1
     * @param contact
     */
    protected static void setSMSNumbers(Contact contact1, Contact contact){
        if(contact1.getSmsCount() != 0 ){
            contact.setSmsCount(contact1.getSmsCount());
        }else if(contact.getSmsCount() != 0){
            contact1.setSmsCount(contact.getSmsCount());
        }
    }
    protected static void setDuration(Contact contact1, Contact contact){
        if(contact1.getDurationTime() != 0 ){
            contact.setDurationTime(contact1.getDurationTime());
        }else if(contact.getDurationTime() != 0){
            contact1.setDurationTime(contact.getDurationTime());
        }
    }

    protected static void joinArrays(List<Contact> contacts, List<Contact> smsNumbers)
    {
        if(contacts.size() > smsNumbers.size()){
            joinToMainArray(contacts, smsNumbers);
        }else{
            joinToMainArray(smsNumbers, contacts);
        }
    }

    private static void joinToMainArray(List<Contact>  first, List<Contact>  second)
    {
        for(Contact contact: first){
            for(Contact contact1 : second){
                if(contact1.getPhoneNumber().equals(contact.getPhoneNumber())){

                    ArraysCompareUtils.setSMSNumbers( contact1,  contact);
                    ArraysCompareUtils.setDuration( contact1, contact);
                }
            }
        }
    }

}
