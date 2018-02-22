package com.example.amirlubashevsky.mobilogyproj;

/**
 * Created by amirlubashevsky on 18/02/2018.
 */

        import android.database.Cursor;
        import android.os.Bundle;
        import android.os.Handler;
        import android.provider.ContactsContract;
        import android.support.v4.app.FragmentActivity;
        import android.support.v7.widget.DefaultItemAnimator;
        import android.support.v7.widget.DividerItemDecoration;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.Button;
        import java.util.ArrayList;
        import java.util.List;

public class MainActivityDB extends FragmentActivity {
    private RecyclerView lstContact;
    private CustomContactAdapter adapter;
    private DatabaseHandler databaseHadler;
    private Handler handler = new Handler();
    private Button save_contacts;
    private ArrayList<Contact> lstContactsSMS = new ArrayList<>();
    private List<Contact> lstContacts = new ArrayList<>();
    private ArrayList<Contact> lstContactsDuration = new ArrayList<>();
    private int counter = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHadler = new DatabaseHandler(MainActivityDB.this);
        initializeListView();

        save_contacts = (Button)findViewById(R.id.save_contacts);
        save_contacts.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                save_contacts.setEnabled(false);
                saveToFile();
            }
        });

        lstContactsSMS = UtilsCursor.readSMSMessages(getBaseContext());
        createCallDurationList();

        lstContacts = getContactsFromPhone();
        adapter = new CustomContactAdapter(this,  databaseHadler, lstContacts);
        lstContact.setAdapter(adapter);

        workOnLists();
    }

    private void workOnLists(){
        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                // concatinating between list of users and sms list, not all received sms from contacts
                ArraysCompareUtils.joinArrays(lstContactsSMS, lstContacts);
                // concatinatin between list of contacts and duration time
                ArraysCompareUtils.joinArrays(lstContactsDuration, lstContacts);

                handler.post(new Runnable()  //If you want to update the UI, queue the code on the UI thread
                {
                    public void run()
                    {
                        adapter.notifyDataSetChanged();

                    }
                });
            }
        };

        Thread t = new Thread(r);
        t.start();
    }


    private void createCallDurationList(){
        ArrayList<Contact> contactsDuration = UtilsCursor.getCallDetails(MainActivityDB.this);
        counter = 1;
        ArrayList<Contact> durationList = new ArrayList<>();
        for(Contact contact : contactsDuration){
            lstContactsDuration = createListOfContactsDuration(durationList, contact, contact.getPhoneNumber()) ;
        }
    }


    private void initializeListView(){
        lstContact = (RecyclerView) findViewById(R.id.lstContacts);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.getBaseContext());
        lstContact.setLayoutManager(mLayoutManager);
        lstContact.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(lstContact.getContext(),
                mLayoutManager.getOrientation());
        lstContact.addItemDecoration(dividerItemDecoration);
    }


    /**
     * Receiving contacts from phone
     * @return
     */
    private List<Contact> getContactsFromPhone(){
        List<Contact> list = new ArrayList<>();
        String sortOrder =  ContactsContract.Contacts.SORT_KEY_PRIMARY + " ASC";
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, sortOrder);
        while (phones.moveToNext())
        {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String phoneURL = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
            String phoneID = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID));
            Contact contact =new Contact();
            contact.setImageUri(phoneURL);

            contact.setName(name);
            contact.setID(phoneID);


            phoneNumber = phoneNumber.replace("-", "");
            phoneNumber = phoneNumber.replace(" ", "");
            contact.setPhoneNumber(phoneNumber);
            contact.setPhoneNumbersList(phoneNumber);


            //if numbers are the same, can be if some saved on card and same saved on device
            // don't repeat numbers
            if(list.isEmpty()) {
                list.add(contact);
            }else{
                int size = list.size();
                if(list.get(size-1).getPhoneNumber().equals(contact.getPhoneNumber())){
                    list.get(size-1).setPhoneNumbersList(phoneNumber);
                }else{
                    list.add(contact);
                }
            }
        }

        phones.close();
        return list;
    }


    private ArrayList<Contact> createListOfContactsDuration(ArrayList<Contact> contactsTemp, Contact contact, String phoneNumber){
        if(phoneNumber != null) {
            if (contactsTemp.isEmpty()) {
                contactsTemp.add(contact);
            } else {
                Contact contactTemp = contactsTemp.get(contactsTemp.size() - 1);
                if (contactTemp.getPhoneNumber().equals(phoneNumber)) {
                    int durationTime = contactTemp.getDurationTime() + contact.getDurationTime();
                    contactsTemp.get(contactsTemp.size() - 1).setDurationTime(durationTime);
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

    private void saveToFile(){
        Runnable r = new Runnable()
        {
            @Override
            public void run()
            {
                Utils.getVCF(getBaseContext());
                handler.post(new Runnable()  //If you want to update the UI, queue the code on the UI thread
                {
                    public void run()
                    {
                        save_contacts.setEnabled(true);
                    }
                });
            }
        };

        Thread t = new Thread(r);
        t.start();
    }

}
