package com.ccc.contacts.country;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.ccc.contacts.R;

/**
 * The <code>CountryListBuilder</code> is a lightweight class that builds the country list menu with their respective flags for the Native Android 'ContactList' Mobile Application.
 * 
 * @author Gibran E. Castillo
 * @version 1.0, 2nd December 2014
 * @see android.annotation.SuppressLint
 * @see android.content.Context
 * @see android.os.Bundle
 * @see android.support.v4.app.DialogFragment
 */
public class CountryListBuilder extends DialogFragment implements Comparator<Country> {
	// String used when logging error messages
	private static final String TAG = "CountryListBuilder";
	
	// instance variables
	private EditText searchEditText;
	private ListView countryListView;
	private CountryListAdapter countryListAdapter;
	private List<Country> countryList;
	private List<Country> selectedCountriesList;
	private CountryListListener countryListListener;
	
	/**
	 * Set listener to which country user selected
	 * 
	 * @param listener
	 */
	public void setListener(CountryListListener listener) {
		this.countryListListener = listener;
	}
	
	/**
	 * 
	 * @return
	 */
	public EditText getSearchEditText() {
		return searchEditText;
	}
	
	/**
	 * 
	 * @return
	 */
	public ListView getCountryListView() {
		return countryListView;
	}
	
	/**
	 * Convenient function to get currency code from country code currency code is in English locale
	 * 
	 * @param countryCode a String value that represents the country's code.
	 * @return a currency object corresponding to an ISO 4217 currency code such as "EUR" or "USD".
	 */
	public static Currency getCurrencyCode(String countryCode) {
		try {
			return Currency.getInstance(new Locale("en", countryCode));
		} catch(Exception e) {
			// do nothing!
		}
		
		return null;
	}
	
	/**
	 * Get country list with code and name from JSON.
	 * 
	 * @return a list of Country objects, sorted by country name.
	 */
	private List<Country> getCountryList() {
		if(countryList == null) {
			try {
				countryList = new ArrayList<Country>();
				
				// Read from local file
				String allCountriesString = readFileAsString(getActivity());
				
				Log.d(TAG, "country: " + allCountriesString);
				
				JSONObject jsonObject = new JSONObject(allCountriesString);
				Iterator<?> keys = jsonObject.keys();
				
				// Add the data to all countries list
				while(keys.hasNext()) {
					String key = (String) keys.next();
					Country country = new Country();
					country.setCountryCode(key);
					country.setCountryName(jsonObject.getString(key));
					countryList.add(country);
				}
				
				// Sort the all countries list based on country name
				Collections.sort(countryList, this);
				
				// Initialize selected countries with all countries.  Hold countries that matched user query.
				selectedCountriesList = new ArrayList<Country>();
				selectedCountriesList.addAll(countryList);
				
				return countryList;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * R.string.countries is a JSON string which is Base64 encoded to avoid special characters in XML.  It's Base64 decoded here to get original JSON.
	 * 
	 * @param context allows access to application-specific resources and classes, as well as up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc.
	 * @return
	 * @throws java.io.IOException
	 */
	private static String readFileAsString(Context context) throws java.io.IOException {
		/*String base64 = context.getResources().getString(R.string.countries);
		byte[] data = Base64.decode(base64, Base64.DEFAULT);
		
		return new String(data, "UTF-8");*/
		return "";
	}
	
	/**
	 * To support show as dialog
	 * 
	 * @param dialogTitle a String value that represents the dialog title.
	 * @return a build Country List.
	 */
	public static CountryListBuilder newInstance(String dialogTitle) {
		CountryListBuilder countryListBuilder = new CountryListBuilder();
		Bundle bundle = new Bundle();
		bundle.putString("dialogTitle", dialogTitle);
		countryListBuilder.setArguments(bundle);
		
		return countryListBuilder;
	}
	
	/**
	 * Create view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// inflate CountryListBuilder
		View view = inflater.inflate(R.layout.country_list_item, null);
		
		// Get country list from the JSON.
		getCountryList();
		
		// Set dialog title if show as dialog
		Bundle args = getArguments();
		
		if(args != null) {
			String dialogTitle = args.getString("dialogTitle");
			getDialog().setTitle(dialogTitle);
			
			int width = getResources().getDimensionPixelSize(R.dimen.cp_dialog_width);
			int height = getResources().getDimensionPixelSize(R.dimen.cp_dialog_height);
			getDialog().getWindow().setLayout(width, height);
		}
		
		// Get view components
		searchEditText = (EditText) view.findViewById(R.id.country_list_search);
		countryListView = (ListView) view.findViewById(R.id.country_list_listview);
		
		// Set adapter for the country ListView.
		countryListAdapter = new CountryListAdapter(getActivity(), selectedCountriesList);
		countryListView.setAdapter(countryListAdapter);
		
		/**
		 * An anonymous inner class, flavor two (implementer of an interface) and method-local inner class, that is, an implementer of the OnItemClickListener static interface.
		 * An event listener object that responds to the user touching a country's name in the ListView; in other words, inform CountryListListener.
		 * 
		 * Declare a type OnItemClickListener, then declare a new class that has no name, but that is the implementer of the OnItemClickListener static interface.
		 * Implement the onItemClick() method of the static interface OnItemClickListener.
		 */
		countryListView.setOnItemClickListener(new OnItemClickListener() {
			
			/**
			 * Launch and start the View Country.
			 * 
			 * @param parent
			 * @param view 
			 * @param position
			 * @param id
			 */
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(countryListListener != null) {
					Country country = selectedCountriesList.get(position);
					countryListListener.onSelectCountry(country.getCountryName(), country.getCountryCode());
				}
			}
		});
		
		// 
		/**
		 * An anonymous inner class, flavor two (implementer of an interface) and method-local inner class, that is, an implementer of the TextWatcher static interface.
		 * An event listener object that Searches for which countries matched user query.
		 * 
		 * Declare a type TextWatcher, then declare a new class that has no name, but that is the implementer of the TextWatcher static interface.
		 * Implement the onItemClick() method of the static interface TextWatcher.
		 */
		searchEditText.addTextChangedListener(new TextWatcher() {
			
			/**
			 * 
			 */
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// do nothing!
			}
			
			/**
			 * 
			 */
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// do nothing!
			}
			
			/**
			 * 
			 * @param s
			 */
			@Override
			public void afterTextChanged(Editable s) {
				search(s.toString());
			}
		});
		
		return view;
	}
	
	/**
	 * Searches the country list; contains text and put result into selectedCountriesList.
	 * 
	 * @param text
	 */
	@SuppressLint("DefaultLocale")
	private void search(String text) {
		selectedCountriesList.clear();
		
		for(Country country : countryList) {
			if(country.getCountryName().toLowerCase(Locale.ENGLISH).contains(text.toLowerCase())) {
				selectedCountriesList.add(country);
			}
		}
		
		countryListAdapter.notifyDataSetChanged();
	}
	
	/**
	 * Support sorting the countries list
	 */
	@Override
	public int compare(Country lhs, Country rhs) {
		return lhs.getCountryName().compareTo(rhs.getCountryName());
	}
}