package com.ccc.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.ccc.contacts.R;
import com.ccc.contacts.model.dao.ContactListDao;

/**
 * The <code>ViewContact</code> is a sub Activity class for the Native Android 'ContactList' Mobile Application.
 * 
 * <p>Activity for viewing a single contact.</p>
 * 
 * @author Gibran E. Castillo
 * @version 1.0, 2nd December 2014
 * @see android.app.Activity
 * @see android.app.AlertDialog
 */
public class ViewContact extends Activity {
	// String used when logging error messages
	private static final String TAG = "ViewContact Activity";
	
	// instance variables
	private long rowID;
	private TextView nameTextView; 
	private TextView phoneTextView;
	private TextView emailTextView;
	private TextView streetTextView;
	private TextView cityTextView;
	private TextView stateTextView;
	private TextView zipTextView;
	
	/**
	 * called when the ViewContact activity is first created.
	 * 
	 * @param savedInstanceState a mapping from String values to various Parcelable types.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// call super's onCreate.
		super.onCreate(savedInstanceState);
		
		// inflate the ViewContact UI.
		setContentView(R.layout.view_contact);
		
		// get references to the name, phone, email, street, city, state and ZIP EditTexts.
		nameTextView = (TextView) findViewById(R.id.nameTextView);
		phoneTextView = (TextView) findViewById(R.id.phoneTextView);
		emailTextView = (TextView) findViewById(R.id.emailTextView);
		streetTextView = (TextView) findViewById(R.id.streetTextView);
		cityTextView = (TextView) findViewById(R.id.cityTextView);
		stateTextView = (TextView) findViewById(R.id.stateTextView);
		zipTextView = (TextView) findViewById(R.id.zipTextView);
		
		// get Bundle of extras.
		Bundle extras = getIntent().getExtras();
		
		// get the selected contact's row ID.
		rowID = extras.getLong(ContactList.ROW_ID);
	}
	
	/**
	 * Called when the ViewContact activity will start interacting with the user. At this point your activity is at the top of the activity stack,
	 * with user input going to it.  Always followed by onPause().  Create new LoadContactListTask and execute it.
	 */
	@Override
	protected void onResume() {
		// call super's onResume
		super.onResume();
		
		new LoadContactListTask().execute(rowID);
	}
	
	/**
	 * The <code>LoadContactListTask</code> is an inner member class that performs database query outside UI thread [asynchronous, that is, non-blocking].
	 * 
	 * @author Gibran E. Castillo
	 *
	 */
	private class LoadContactListTask extends AsyncTask<Long, Object, Cursor> {
		ContactListDao databaseConnector = new ContactListDao(ViewContact.this, TAG);
		
		/**
		 * Performs the database access and gets one contact in the list.
		 * 
		 * @param params 
		 * @return an SQLiteCursor with the results set, that is, the list of contacts.
		 */
		@Override
		protected Cursor doInBackground(Long... params) {
			databaseConnector.open();
			
			return databaseConnector.getOneContact(params[0]);
		}
		
		/**
		 * Fills TextViews with the retrieved data and closes the database access.
		 * 
		 * @param result the SQLiteCursor returned from the doInBackground method
		 */
		@Override
		protected void onPostExecute(Cursor result) {
			super.onPostExecute(result);
			
			result.moveToFirst(); 
			
			// get the column index for each data item
			int nameIndex = result.getColumnIndex("name");
			int phoneIndex = result.getColumnIndex("phone");
			int emailIndex = result.getColumnIndex("email");
			int streetIndex = result.getColumnIndex("street");
			int cityIndex = result.getColumnIndex("city");
			int stateIndex = result.getColumnIndex("state");
			int zipIndex = result.getColumnIndex("zip");
			
			// fill TextViews with the retrieved data
			nameTextView.setText(result.getString(nameIndex));
			phoneTextView.setText(result.getString(phoneIndex));
			emailTextView.setText(result.getString(emailIndex));
			streetTextView.setText(result.getString(streetIndex));
			cityTextView.setText(result.getString(cityIndex));
			stateTextView.setText(result.getString(stateIndex));
			zipTextView.setText(result.getString(zipIndex));
			
			result.close();
			databaseConnector.close();
		}
	}
	
	/**
	 * Create the ViewContact Activity's menu from view_contact_menu, a menu resource XML file.
	 * 
	 * @param menu a Menu objects that allows you to view contact items and handle clicks on the listed items.
	 * 
	 * @return a true if the Menu was inflated and created successfully; otherwise, false.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.view_contact_menu, menu);
		
		return true;
	}
	
	// handle choice from options menu
	/**
	 * Launch and start the AddEditContact activity.
	 * 
	 * @param item a MenuItem object...
	 * 
	 * @return a true if the Menu was inflated and create successfully; otherwise, false.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		
		if(itemId == R.id.editItem) {
			// edit the displayed contact.
			Intent addEditContact = new Intent(this, AddEditContact.class);
			
			// pass the selected contact's data as extras with the Intent.
			addEditContact.putExtra(ContactList.ROW_ID, rowID);
			addEditContact.putExtra("name", nameTextView.getText());
			addEditContact.putExtra("phone", phoneTextView.getText());
			addEditContact.putExtra("email", emailTextView.getText());
			addEditContact.putExtra("street", streetTextView.getText());
			addEditContact.putExtra("city", cityTextView.getText());
			addEditContact.putExtra("state", stateTextView.getText());
			addEditContact.putExtra("zip", zipTextView.getText());
			startActivity(addEditContact);
			
			return true;
		} else if(itemId == R.id.deleteItem) {
			// delete the displayed contact.
			deleteContact();
			
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Deletes the displayed contact in the ViewContact activity when the Delete Contact button is click.
	 */
	private void deleteContact() {
		AlertDialog.Builder builder = new AlertDialog.Builder(ViewContact.this);
		
		builder.setTitle(R.string.confirmTitle);
		builder.setMessage(R.string.confirmMessage);
		
		/**
		 * An anonymous inner class, flavor two (implementer of an interface) and a method-local inner class that provide an OK button that simply dismisses the dialog.
		 * 
		 * Declare a type DialogInterface.OnClickListener, then declare a new class that has no name, but that is the implementer of the DialogInterface.OnClickListener static interface.
		 * Implement the onClick() method of the static interface DialogInterface.OnClickListener.
		 */
		builder.setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
			
			/**
			 * 
			 * @param dialog
			 * @param button
			 */
			@Override
			public void onClick(DialogInterface dialog, int button) {
				final ContactListDao databaseConnector = new ContactListDao(ViewContact.this, TAG);
				
				/**
				 * An anonymous inner class, flavor one (subclass of an abstract class) and a method-local inner class that deletes the contact in another
				 * thread [Asynchronous, that is, non-blocking], then calls finish after the deletion.
				 * 
				 * Declare a type AsyncTask, then declare a new class that has no name, but that is a subclass of the AsyncTask abstract class.
				 * Override the doInBackground() and onPostExecute() methods of the abstract class AsyncTask.
				 */
				AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>() {
					
					/**
					 * Deletes the displayed contact from the SQLite database.
					 */
					@Override
					protected Object doInBackground(Long... params) {
						databaseConnector.deleteContact(params[0]);
						
						return null;
					}
					
					/**
					 * Return to the previous Activity.
					 */
					@Override
					protected void onPostExecute(Object result) {
						finish();
					}
				};
				
				deleteTask.execute(new Long[] { rowID });
			}
		});
		
		builder.setNegativeButton(R.string.button_cancel, null);
		builder.show();
	}
}