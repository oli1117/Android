package com.ccc.contacts;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.ccc.contacts.R;
import com.ccc.contacts.model.dao.ContactListDao;
import com.ccc.utility.EndUserLicenseAgreement;

/**
 * The <code>ContactList</code> is the main Activity class for the Native Android 'ContactList' Mobile Application.
 * 
 * <p>Activity allows you to see the full list of Contact List.</p>
 * 
 * This free contact list features ...
 * 
 * @author Gibran E. Castillo
 * @version 1.0, 2nd December 2014
 * @see android.app.ListActivity
 * @see android.content.Intent
 */
public class ContactList extends ListActivity {
	// String used when logging error messages
	private static final String TAG = "ContactList Activity";
	
	// Intent extra key
	public static final String ROW_ID = "row_id";
	
	// instance variables
	private ListView contactListView;
	private CursorAdapter contactAdapter;
	
	/**
	 * Called when the ContactList activity is first created.
	 * 
	 * @param savedInstanceState a mapping from String values to various Parcelable types.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// call super's onCreate.
		super.onCreate(savedInstanceState);
		
		new EndUserLicenseAgreement(this).show();
		
		// Get the Android built-in ListView.
		contactListView = getListView();
		contactListView.setOnItemClickListener(viewContactListener);
		
		// map each contact's name to a TextView in the ListView layout
		String[] from = new String[] { "name" };
		int[] to = new int[] { R.id.contactTextView };
		
		// inflate the ContactList UI.
		contactAdapter = new SimpleCursorAdapter(ContactList.this, R.layout.contact_list_item, null, from, to);
		
		// set contactView's adapter
		setListAdapter(contactAdapter);
	}
	
	/**
	 * Called when the ContactList activity will start interacting with the user. At this point your activity is at the top of the activity stack,
	 * with user input going to it.  Always followed by onPause().  Create new ContactListTask and execute it
	 */
	@Override
	protected void onResume() {
		// call super's onResume
		super.onResume();
		
		new GetContactListTask().execute((Object[]) null);
	}
	
	/**
	 * Called when the ContactList activity is no longer visible to the user, because another activity has been resumed and is covering this one.
	 * Deactivate the current cursor
	 */
	@Override
	protected void onStop() {
		Cursor cursor = contactAdapter.getCursor(); // get current Cursor
		
		if(cursor != null) {
			cursor.deactivate(); // deactivate it
		}
		
		contactAdapter.changeCursor(null); // adapted now has no Cursor
		super.onStop();
	}
	
	/**
	 * The <code>GetContactListTask</code> is an inner member class that performs database query outside UI thread [asynchronous, that is, non-blocking].
	 * 
	 * @author Gibran E. Castillo
	 *
	 */
	private class GetContactListTask extends AsyncTask<Object, Object, Cursor> {
		ContactListDao contactListDao = new ContactListDao(ContactList.this, TAG);
		
		/**
		 * Performs the database access and gets all contacts in the list.
		 * 
		 * @param params 
		 * @return an SQLiteCursor with the results set, that is, the list of contacts.
		 */
		@Override
		protected Cursor doInBackground(Object... params) {
			contactListDao.open();
			
			return contactListDao.getAllContacts();
		}
		
		/**
		 * Sets the adapter's Cursor and closes the database access.
		 * 
		 * @param result the SQLiteCursor returned from the doInBackground method
		 */
		@Override
		protected void onPostExecute(Cursor result) {
			contactAdapter.changeCursor(result);
			contactListDao.close();
		}
	}
	
	/**
	 * Create the ContactList Activity's menu from contact_list_menu, a menu resource XML file.
	 * 
	 * @param menu a Menu objects that allows you to add items to it and handle clicks on the added items.
	 * 
	 * @return a true if the Menu was inflated and created successfully; otherwise, false.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contact_list_menu, menu);
		
		return true;
	}
	
	/**
	 * Launch and start the AddEditContact activity.
	 * 
	 * @param item a MenuItem object...
	 * 
	 * @return a true if the Menu was inflated and create successfully; otherwise, false.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent addNewContact = new Intent(ContactList.this, AddEditContact.class);
		startActivity(addNewContact);
		
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * An anonymous inner class, flavor two (implementer of an interface), that is, an implementer of the OnItemClickListener static interface.
	 * An event listener object that responds to the user touching a contact's name in the ListView.
	 * 
	 * Declare a type OnItemClickListener, then declare a new class that has no name, but that is the implementer of the OnItemClickListener static interface.
	 * Implement the onItemClick() method of the static interface OnItemClickListener.
	 */
	OnItemClickListener viewContactListener = new OnItemClickListener() {
		
		/**
		 * Launch and start the View Contact activity.
		 * 
		 * @param parent
		 * @param view 
		 * @param position
		 * @param id
		 */
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent viewContact = new Intent(ContactList.this, ViewContact.class);
			
			// pass the selected contact's row ID as an extra with the Intent
			viewContact.putExtra(ROW_ID, id);
			startActivity(viewContact);
		}
	};
}