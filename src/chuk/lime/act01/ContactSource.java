package chuk.lime.act01;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ContactSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper databaseHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_APPID, 
			MySQLiteHelper.COLUMN_NAME, MySQLiteHelper.COLUMN_NUMBER, MySQLiteHelper.COLUMN_EMAIL};
	
	public ContactSource(Context context){
		databaseHelper = new MySQLiteHelper(context);
	}
	public void open() throws SQLException{
		database = databaseHelper.getWritableDatabase();
	}
	
	public void close(){
		databaseHelper.close();
	}
	
	public void insertIfNewContact(Contact contact){
		// Check if present
		Cursor cursor = database.rawQuery("SELECT * FROM contact WHERE app_id = "+contact.appContactsId, null);
		if(cursor.getCount() < 1){
			// Insert Contact
			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.COLUMN_APPID, contact.appContactsId);
			values.put(MySQLiteHelper.COLUMN_NAME, contact.name);
			values.put(MySQLiteHelper.COLUMN_NUMBER, contact.number);
			values.put(MySQLiteHelper.COLUMN_EMAIL, contact.email);
			long insertid = database.insert(MySQLiteHelper.TABLE_CONTACT, null, values);
			Log.d("INSERTED ID:", ""+insertid);
		}
	}
	
	public List<Contact> getAllContacts(){
		List<Contact> contacts = new ArrayList<Contact>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_CONTACT, allColumns, null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			Contact contact = cursorToContact(cursor);
			Log.d("RETRIEVED ID", ""+contact.id);
			contacts.add(contact);
			cursor.moveToNext();
		}
		// Close the cursor
		cursor.close();
		return contacts;
	}
	
	public void deleteContact(Contact contact){
		long id = contact.id;
		database.delete(MySQLiteHelper.TABLE_CONTACT, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	}
	
	public void editContact(long id, String name, String number, String email){
		// Edit contact
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_NAME, name);
		values.put(MySQLiteHelper.COLUMN_NUMBER, number);
		values.put(MySQLiteHelper.COLUMN_EMAIL, email);
		database.update(MySQLiteHelper.TABLE_CONTACT, values, MySQLiteHelper.COLUMN_ID+"="+id , null);
	}
	
	private Contact cursorToContact(Cursor cursor){
		Contact contact = new Contact();
		contact.setId(cursor.getLong(0));
		contact.setAppId(cursor.getString(1));
		contact.setName(cursor.getString(2));
		contact.setNumber(cursor.getString(3));
		contact.setEmail(cursor.getString(4));
		return contact;
	}
}
