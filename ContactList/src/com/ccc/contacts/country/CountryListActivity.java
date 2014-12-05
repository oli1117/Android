package com.ccc.contacts.country;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

import com.ccc.contacts.R;

/**
 * The <code>CountryListActivity</code> is the activity class that launches the country list for the Native Android 'ContactList' Mobile Application.
 * 
 * <p>Activity allows you to see the full list of Country List.</p>
 * 
 * This free contact list features ...
 * 
 * @author Gibran E. Castillo
 * @version 1.0, 2nd December 2014
 * @see android.os.Bundle
 * @see android.support.v4.app.FragmentActivity
 * @see android.support.v4.app.FragmentManager
 * @see android.support.v4.app.FragmentTransaction
 */
public class CountryListActivity extends FragmentActivity {
	// String used when logging error messages
		private static final String TAG = "CountryListActivity";
	
	/**
	 * Called when the CountryListActivity is first created.
	 * 
	 * @param savedInstanceState a mapping from String values to various Parcelable types.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// call super's onCreate.
		super.onCreate(savedInstanceState);
		
		// inflate the Country List UI.
		setContentView(R.layout.view_country_list);
		
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		
		CountryListBuilder countryListBuilder = new CountryListBuilder();
		
		/**
		 * An anonymous inner member class, flavor two (implementer of an interface), that is, an implementer of the CountryListListener interface.
		 * Called to respond to event generated when user selects a country in the country list.
		 * 
		 * Declare a type CountryListListener, then declare a new class that has no name, but that is the implementer of the CountryListListener interface.
		 * Implement the onSelectCountry() method of the interface CountryListListener.
		 */
		countryListBuilder.setListener(new CountryListListener() {
			
			/**
			 * Show a toast message on the selected country from the country list.
			 * 
			 * @param name a String value that represents the name of the country that was selected from the country list.
			 * @param code a String value that represents the code of the country that was selected from the country list.
			 */
			@Override
			public void onSelectCountry(String name, String code) {
				Toast.makeText(
						CountryListActivity.this,
						"Country Name: " + name + " - Code: " + code + " - Currency: " + CountryListBuilder.getCurrencyCode(code),
						Toast.LENGTH_SHORT).show();
			}
		});
		
		transaction.replace(R.id.home, countryListBuilder);
		transaction.commit();
	}
	
	/**
	 * Create the Country List Activity's menu from country_list_menu, a menu resource XML file.
	 * 
	 * @param menu a Menu objects that allows you to add items to it and handle clicks on the added items.
	 * 
	 * @return a true if the Menu was inflated and created successfully; otherwise, false.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the country_list_menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.country_list_menu, menu);
		MenuItem item = menu.findItem(R.string.show_dialog);
		
		/**
		 * An anonymous inner class, flavor two (implementer of an interface), that is, an implementer of the OnMenuItemClickListener static interface.
		 * An event listener object that responds to the user touching a country's name in the ListView.
		 * 
		 * Declare a type OnMenuItemClickListener, then declare a new class that has no name, but that is the implementer of the OnMenuItemClickListener static interface.
		 * Implement the onMenuItemClick() method of the static interface OnMenuItemClickListener.
		 */
		item.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			/**
			 * Selet country from country list.
			 * 
			 * @param item
			 * 
			 * @return
			 */
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				CountryListBuilder countryListBuilder = CountryListBuilder.newInstance("Select Country");
				
				/**
				 * An anonymous inner member class, flavor two (implementer of an interface), that is, an implementer of the CountryListListener interface.
				 * Called to respond to event generated when user selects a country in the country list.
				 * 
				 * Declare a type CountryListListener, then declare a new class that has no name, but that is the implementer of the CountryListListener interface.
				 * Implement the onSelectCountry() method of the interface CountryListListener.
				 */
				countryListBuilder.setListener(new CountryListListener() {
					
					/**
					 * Show a toast message on the selected country from the country list.
					 * 
					 * @param name a String value that represents the name of the country that was selected from the country list.
					 * @param code a String value that represents the code of the country that was selected from the country list.
					 */
					@Override
					public void onSelectCountry(String name, String code) {
						Toast.makeText(
								CountryListActivity.this,
								"Country Name: " + name + " - Code: " + code + " - Currency: " + CountryListBuilder.getCurrencyCode(code),
								Toast.LENGTH_SHORT).show();
					}
				});
				
				countryListBuilder.show(getSupportFragmentManager(), TAG);
				
				return false;
			}
		});
		
		return true;
	}
}