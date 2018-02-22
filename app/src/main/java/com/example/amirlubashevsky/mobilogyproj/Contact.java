package com.example.amirlubashevsky.mobilogyproj;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by amirlubashevsky on 20/02/2018.
 */

public class Contact {

    private String _id;
    private String _name = "";
    private String _phone_number;
    private List<String> phoneNumbers = new ArrayList<>();
    private int _contact_color = 0;
    private int smsCount;
    private String imageUri;
    private int durationTime;
    // Empty constructor

    public Contact(){}
    // constructor
    public Contact(String id, String name, String _phone_number, int _contact_color){
        this._id = id;
        this._name = name;
        this._phone_number = _phone_number;
        this._contact_color = _contact_color;
    }

    // constructor
    public Contact(String id, int _contact_color){
        this._id = id;
        this._contact_color = _contact_color;
    }

    // getting ID
    public String getID(){
        return this._id;
    }

    // setting id
    public void setID(String id){
        this._id = id;
    }

    // getting name
    public String getName(){
        return this._name;
    }

    // setting name
    public void setName(String name){
        this._name = name;
    }

    // getting phone number
    public String getPhoneNumber(){
        return this._phone_number;
    }

    // setting phone number
    public void setPhoneNumber(String phone_number){
        this._phone_number = phone_number;
    }

    public int get_contact_color() {
        return _contact_color;
    }

    public int getSmsCount() {
        return smsCount;
    }

    public void setSmsCount(int smsCount) {
        this.smsCount = smsCount;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public int getDurationTime() {
        return durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    public static Comparator<Contact> COMPARE_BY_PHONE = new Comparator<Contact>() {
        public int compare(Contact one, Contact other) {
            return one._phone_number.compareTo(other._phone_number);
        }
    };

    public void setPhoneNumbersList(String phoneNumbers) {
        this.phoneNumbers.add(phoneNumbers);
    }
}
