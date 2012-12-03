package chuk.lime.act01;

import java.util.ArrayList;
import java.util.List;

import chuk.lime.expand_animation_example.ExpandAnimation;


import android.net.Uri;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;

public class ContactsActivity extends ListActivity {

	private ContactSource datasource;
	Contact selectedContact;
	List<Contact> contactsToDisplay;
	TextView tvTitle, tvNoContact;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        
        // Initialize attributes
		datasource = new ContactSource(this);
		datasource.open();
		selectedContact = new Contact();
		contactsToDisplay = new ArrayList<Contact>();
		
        setup();
    }
    
    private void setup(){
    	// Setup views
    	tvTitle = (TextView)findViewById(R.id.tvListHeaderTitle);
    	tvNoContact = (TextView)findViewById(R.id.tvNoContacts);
    	tvTitle.setText("Contacts");
    	
    	// Retrieve contents from Contact Application
    	Cursor cursor = getContacts();
    	
    	// Update sqlite table Contacts
        while(cursor.moveToNext()){
        	String contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
        	String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Data._ID));
        	String contactNumber = getContactPhone(contactId);
        	String contactEmail = getContactEmail(contactId);
        	datasource.insertIfNewContact(new Contact(contactId, contactName, contactNumber, contactEmail));
        }
        
        // Retrieve contacts from sqlite
        displayContactsFromSQLite();
    	
    	
    	getListView().setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				selectedContact = (Contact) v.getTag();
				View toolbar = findViewById(R.id.toolbar);
                // Creating the expand animation for the item
                ExpandAnimation expandAni = new ExpandAnimation(toolbar, 0);
                // Start the animation on the toolbar
                toolbar.startAnimation(expandAni);
				
                View a= (View)findViewById(R.id.toolbar);
				a.setVisibility(View.VISIBLE);
				
				
				Button edit = (Button)findViewById(R.id.Edit);
				edit.setOnClickListener(new OnClickListener(){

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent i = new Intent();
						Bundle b = new Bundle();
						b.putSerializable("contactToEdit", selectedContact);
						i.putExtras(b);
						i.setClass(ContactsActivity.this, EditContact.class);
						startActivityForResult(i, 1);
					}
				});
				
				Button delete = (Button)findViewById(R.id.Delete);
				delete.setOnClickListener(new OnClickListener(){

					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						datasource.deleteContact(selectedContact);
						contactsToDisplay.remove(selectedContact);
						if(contactsToDisplay.size() == 0)
							tvNoContact.setVisibility(View.VISIBLE);
						getListView().setAdapter(new ContactAdapter(ContactsActivity.this, (ArrayList<Contact>)contactsToDisplay,'D'));
						Toast.makeText(ContactsActivity.this, "Contact Successfully Deleted!", Toast.LENGTH_SHORT).show();
					}
				});

			}
		});
    	
    	
    }

	private Cursor getContacts() {
		// TODO Auto-generated method stub
		// Run query
	    Uri uri = ContactsContract.Contacts.CONTENT_URI;
	    String[] projection = new String[] {ContactsContract.Contacts._ID, 
	    									ContactsContract.Contacts.DISPLAY_NAME, };
	    return getContentResolver().query(uri, projection, null, null, null);
	}
	
	private void displayContactsFromSQLite(){
        contactsToDisplay = datasource.getAllContacts();
        
    	if(contactsToDisplay.size() > 0){
    		tvNoContact.setVisibility(View.GONE);
	    	// Setup list
	    	getListView().setAdapter(new ContactAdapter(ContactsActivity.this, (ArrayList<Contact>)contactsToDisplay,'N'));
    	}
	}
	
	private String getContactPhone(String contactID) {
	    Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	    String[] projection = null;
	    String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?";
	    String[] selectionArgs = new String[] { contactID };
	    String sortOrder = null;
	    Cursor result = getContentResolver().query(uri, projection, where, selectionArgs, sortOrder);
	    if (result.moveToFirst()) {
	        String phone = result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	        if (phone != null) {
	            result.close();
	            return phone;
	        }
	    }
	    result.close();
	    return null;
	}
	
	private String getContactEmail(String contactID) {
	    Uri uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
	    String[] projection = null;
	    String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?";
	    String[] selectionArgs = new String[] { contactID };
	    String sortOrder = null;
	    Cursor result = getContentResolver().query(uri, projection, where, selectionArgs, sortOrder);
	    if (result.moveToFirst()) {
	        String phone = result.getString(result.getColumnIndex(ContactsContract.CommonDataKinds.Email.ADDRESS));
	        if (phone != null) {
	            result.close();
	            return phone;
	        }
	    }
	    result.close();
	    return null;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == RESULT_OK){
			displayContactsFromSQLite();
		}
	}
}
