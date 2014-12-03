package com.ccc.contacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ccc.contacts.R;
import com.ccc.contacts.model.dao.ContactListDao;

/**
 * The <code>AddEditContact</code> is a sub Activity class for the Native Android 'ContactList' Mobile Application.
 * 
 * <p>Activity for adding a new entry to or editing an existing entry in the Contact List.</p>
 * 
 * This free contact list features ...
 * 
 * @author Gibran E. Castillo
 * @version 1.0, 2nd December 2014
 * @see android.app.Activity
 * @see ndroid.app.AlertDialog
 */
public class AddEditContact extends Activity {
	// String used when logging error messages
	private static final String TAG = "AddEditContact Activity";
	
	// contact id
	private long rowID;
	
	// EditTexts instance variables for contact information
	private EditText nameEditText;
	private EditText phoneEditText;
	private EditText emailEditText;
	private EditText streetEditText;
	private EditText cityEditText;
	private EditText stateEditText;
	private EditText zipEditText;
	
	/**
	 * Called when the AddEditContact Activity is first created.
	 * 
	 * @param savedInstanceState a mapping from String values to various Parcelable types.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// call super's onCreate.
		super.onCreate(savedInstanceState);
		
		// inflate the AddEditContact UI.
		setContentView(R.layout.add_contact);
		
		// get references to the name, phone, email, street, city, state and zip EditTexts.
		nameEditText = (EditText) findViewById(R.id.nameEditText);
		phoneEditText = (EditText) findViewById(R.id.phoneEditText);
		emailEditText = (EditText) findViewById(R.id.emailEditText);
		streetEditText = (EditText) findViewById(R.id.streetEditText);
		cityEditText = (EditText) findViewById(R.id.cityEditText);
		stateEditText = (EditText) findViewById(R.id.stateEditText);
		zipEditText = (EditText) findViewById(R.id.zipEditText);
		
		// get Bundle of extras
		Bundle extras = getIntent().getExtras();
		
		// if there are extras, use them to populate the EditTexts.
		if(extras != null) {
			rowID = extras.getLong("row_id"); // id of contact
			nameEditText.setText(extras.getString("name"));
			phoneEditText.setText(extras.getString("phone"));
			emailEditText.setText(extras.getString("email"));
			streetEditText.setText(extras.getString("street"));
			cityEditText.setText(extras.getString("city"));
			stateEditText.setText(extras.getString("state"));
			zipEditText.setText(extras.getString("zip"));
		}
		
		// set event listener for the Save Contact Button.
		Button saveContactButton = (Button) findViewById(R.id.saveContactButton);
		saveContactButton.setOnClickListener(saveContactButtonClicked);
	}
	
	/**
	 * An anonymous inner member class, flavor one (subclass of an class), that is, a subclass of the OnClickListener class.
	 * Called to respond to event generated when user clicks the Done Button.
	 * 
	 * Declare a type OnClickListener, then declare a new class that has no name, but that is a subclass of the OnClickListener class.
	 * Override the onClick() method of the class OnClickListener.
	 */
	OnClickListener saveContactButtonClicked = new OnClickListener() {
		
		/**
		 * 
		 */
		@Override
		public void onClick(View v) {
			if(nameEditText.getText().length() != 0) {
				
				/**
				 * An anonymous inner class, flavor one (subclass of an abstract class) and a method-local inner class that saves the contact to the
				 * SQLite database using a separate thread [Asynchronous, that is, non-blocking], then calls finish after the saving.
				 * 
				 * Declare a type AsyncTask, then declare a new class that has no name, but that is a subclass of the AsyncTask abstract class.
				 * Override the doInBackground() and onPostExecute() methods of the abstract class AsyncTask.
				 */
				AsyncTask<Object, Object, Object> saveContactTask = new AsyncTask<Object, Object, Object>() {
					
					/**
					 * Saves contact to the database.  Insert new contact or update existing contact.
					 */
					@Override
					protected Object doInBackground(Object... params) {
						saveContact();
						
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
				
				saveContactTask.execute((Object[]) null);
			} else {
				AlertDialog.Builder builder = new AlertDialog.Builder(AddEditContact.this);
				
				// set dialog title & message, and provide Button to dismiss
				builder.setTitle(R.string.errorTitle);
				builder.setMessage(R.string.errorMessage);
				builder.setPositiveButton(R.string.errorButton, null);
				
				// display the Dialog.
				builder.show();
			}
		}
	};
	
	/**
	 * Saves contact information to the SQLite database.  Insert new contact or update existing contact.
	 */
	private void saveContact() {
		ContactListDao databaseConnector = new ContactListDao(this, TAG);
		
		if(getIntent().getExtras() == null) {
			databaseConnector.insertContact(
					nameEditText.getText().toString(),
					phoneEditText.getText().toString(),
					emailEditText.getText().toString(),
					streetEditText.getText().toString(),
					cityEditText.getText().toString(),
					stateEditText.getText().toString(),
					zipEditText.getText().toString());
		} else {
			databaseConnector.updateContact(rowID,
					nameEditText.getText().toString(),
					phoneEditText.getText().toString(),
					emailEditText.getText().toString(), 
					streetEditText.getText().toString(),
					cityEditText.getText().toString(),
					stateEditText.getText().toString(),
					zipEditText.getText().toString());
		}
	}
}