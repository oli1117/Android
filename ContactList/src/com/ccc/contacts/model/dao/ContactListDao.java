package com.ccc.contacts.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

/**
 * The <code>ContactListDao</code> is a lightweight class that executes SQLite queries for the Native Android 'ContactList' Mobile Application.
 * 
 * SQLite is a software library that implements a self-contained, serverless, zero-configuration, transactional SQL database engine.
 * 
 * <p>Provides easy connection and creation of ContactsDb.</p>
 * 
 * This free contact list features ...
 * 
 * @author Gibran E. Castillo
 * @version 1.0, 2nd December 2014
 * @see android.content.ContentValues
 * @see android.content.Context
 */
public class ContactListDao {
	// String used when logging error messages
	private String TAG;
	
	// SQLite database name
	private static final String DATABASE_NAME = "ContactsDb";
	
	// instance variables
	private SQLiteDatabase sqLiteDatabase;
	private SQLiteDatabaseOpenHelper sqliteDatabaseOpenHelper;
	
	/**
	 * Constructs a DatabaseConnector
	 * 
	 * @param context allows access to application-specific resources and classes, as well as up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc.
	 * @param tag a String value that represents the TAG value use by the calling class used when logging error messages.
	 */
	public ContactListDao(Context context, String tag) {
		sqliteDatabaseOpenHelper = new SQLiteDatabaseOpenHelper(context, DATABASE_NAME, null, 1);
		TAG = tag;
	}
	
	/**
	 * Create a SQLite database or Open a SQLite database connection for reading and writing.
	 */
	public void open() {
		try {
			sqLiteDatabase = sqliteDatabaseOpenHelper.getWritableDatabase();
		} catch(SQLException  sqle) {
			sqle.printStackTrace();
			Log.e(TAG, "Error creating or opening a SQLite database for read/write", sqle);
		}
	}
	
	/**
	 * Close the SQLite database connection
	 */
	public void close() {
		if(sqLiteDatabase != null) {
			sqLiteDatabase.close();
		}
	}
	
	/**
	 * Inserts a new contact in the SQLite database.
	 * 
	 * @param id a long value that represents the contact id.
	 * @param name a String value that represents the contact name. 
	 * @param email a String value that represents the contact email.
	 * @param phone a String value that represents the contact phone.
	 * @param street a String value that represents the contact street.
	 * @param city a String value that represents the contact city.
	 * @param state a String value that represents the contact state.
	 * @param zip a String value that represents the contact ZIP.
	 */
	public void insertContact(String name, String email, String phone, String street, String city, String state, String zip) {
		ContentValues newContact = new ContentValues();
		newContact.put("name", name);
		newContact.put("email", email);
		newContact.put("phone", phone);
		newContact.put("street", street);
		newContact.put("city", city);
		newContact.put("state", state);
		newContact.put("zip", zip);
		
		open();
		
		sqLiteDatabase.insert("contacts_table", null, newContact);
		
		close();
	}
	
	/**
	 * Updates an existing contact's information in the SQLite database.
	 * 
	 * @param id a long value that represents the contact id.
	 * @param name a String value that represents the contact name. 
	 * @param email a String value that represents the contact email.
	 * @param phone a String value that represents the contact phone.
	 * @param street a String value that represents the contact street.
	 * @param city a String value that represents the contact city.
	 * @param state a String value that represents the contact state.
	 * @param zip a String value that represents the contact ZIP.
	 */
	public void updateContact(long id, String name, String email, String phone, String street, String city, String state, String zip) {
		ContentValues editContact = new ContentValues();
		editContact.put("name", name);
		editContact.put("email", email);
		editContact.put("phone", phone);
		editContact.put("street", street);
		editContact.put("city", city);
		editContact.put("state", state);
		editContact.put("zip", zip);
		
		open();
		
		sqLiteDatabase.update("contacts_table", editContact, "_id=" + id, null);
		
		close();
	}
	
	/**
	 * Return a Cursor with all contact information in the SQLite database.
	 * 
	 * @return a SQLiteCursor with all contact information in the SQLite database.
	 */
	public Cursor getAllContacts() {
		return sqLiteDatabase.query("contacts_table", new String[] {"_id", "name"}, null, null, null, null, "name");
	}
	
	/**
	 * Get a Cursor containing all information about the contact specified by the given id.
	 * 
	 * @param id a long value that represents the contact id.
	 * 
	 * @return a SQLiteCursor with the id or one contact information in the SQLite database.
	 */
	public Cursor getOneContact(long id) {
		return sqLiteDatabase.query("contacts_table", null, "_id=" + id, null, null, null, null);
	}
	
	/**
	 * Delete the contact specified by the given id.
	 * 
	 * @param id a long value that represents the contact id.
	 */
	public void deleteContact(long id) {
		open();
		
		sqLiteDatabase.delete("contacts_table", "_id=" + id, null);
		
		close();
	}
	
	/**
	 * The <code>SQLiteDatabaseOpenHelper</code> is an inner member class that manages the SQLite database.
	 * 
	 * @author Gibran E. Castillo
	 *
	 */
	private class SQLiteDatabaseOpenHelper extends SQLiteOpenHelper {
		
		/**
		 * Constructs a SQLiteDatabaseOpenHelper
		 * 
		 * @param context to use to open or create the database.
		 * @param name of the database file, or null for an in-memory database.
		 * @param factory to use for creating cursor objects, or null for the default.
		 * @param version an int value that represents the number of the database (starting at 1)
		 */
		public SQLiteDatabaseOpenHelper(Context context, String name, CursorFactory factory, int version) {
			super(context, name, factory, version);
		}
		
		/**
		 * Creates the contacts_table when the SQLite database is created.
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			/*String createQuery = "CREATE TABLE contacts" +
					"(_id integer primary key autoincrement," +
					"name TEXT, email TEXT, phone TEXT," +
					"street TEXT, city TEXT);";*/
			String createQuery = "CREATE TABLE contacts_table(_id integer primary key autoincrement, name TEXT, email TEXT, phone TEXT, street TEXT, city TEXT, state TEXT, zip TEXT);";
			
			db.execSQL(createQuery);
		}
		
		/**
		 * If the SQLite database is an older version, this method will be use to upgrade the SQLite database.
		 * 
		 * @param db
		 * @param oldVersion
		 * @param newVersion
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// do nothing
		}
	}
}