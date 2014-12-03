package com.ccc.contacts.country;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccc.contacts.R;
import com.ccc.contacts.R.drawable;

/**
 * The <code>CountryListAdapter</code> is a lightweight class that draws a list of countries with their respective flags for the Native Android 'ContactList' Mobile Application.
 * 
 * @author Gibran E. Castillo
 * @version 1.0, 2nd December 2014
 * @see android.content.Context
 * @see android.util.Log
 * @see android.view.LayoutInflater
 */
public class CountryListAdapter extends BaseAdapter {
	// String used when logging error messages
	private static final String TAG = "CountryListAdapter";
	
	// instance variables.
	private Context context;
	List<Country> countries;
	LayoutInflater layoutInflater;
	
	/**
	 * The drawable image name has the format "country_flag_$countryCode". It loads the drawable dynamically from country code.
	 * 
	 * @param drawableName a String value that represents the drawable name.
	 * 
	 * @return an integer value that represents the drawable id.
	 */
	private int getResId(String drawableName) {
		try {
			Class<drawable> res = R.drawable.class;
			Field field = res.getField(drawableName);
			int drawableId = field.getInt(null);
			
			return drawableId;
		} catch(Exception e) {
			Log.e(TAG, "Failure to get drawable id.", e);
		}
		
		return -1;
	}
	
	/**
	 * Constructs a CountryListAdapter.
	 * 
	 * @param context allows access to application-specific resources and classes, as well as up-calls for application-level operations such as launching activities, broadcasting and receiving intents, etc.
	 * @param countries a list of Country objects.
	 */
	public CountryListAdapter(Context context, List<Country> countries) {
		super();
		this.context = context;
		this.countries = countries;
		layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return countries.size();
	}
	
	@Override
	public Object getItem(int arg0) {
		return null;
	}
	
	@Override
	public long getItemId(int arg0) {
		return 0;
	}
	
	/**
	 * Return row for each country
	 * 
	 * @param position an interger value that represents the index for the country in the country list.
	 * @param convertView a widget or a View object that occupies a rectangular area on the screen and is responsible for drawing and event handling.
	 * @param parent a special view that can contain other views (called children).  The view group is the base class for layouts and views containers.
	 * 
	 * @return a View row object.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Cell cell;
		View cellView = convertView;
		Country country = countries.get(position);
		
		if(convertView == null) {
			cell = new Cell();
			cellView = layoutInflater.inflate(R.layout.row, null);
			cell.textView = (TextView) cellView.findViewById(R.id.row_title);
			cell.imageView = (ImageView) cellView.findViewById(R.id.row_icon);
			cellView.setTag(cell);
		} else {
			cell = (Cell) cellView.getTag();
		}
		
		cell.textView.setText(country.getCountryName());
		
		// Load drawable dynamically from country code
		String drawableName = "country_flag_"+ country.getCountryCode().toLowerCase(Locale.ENGLISH);
		cell.imageView.setImageResource(getResId(drawableName));
		
		return cellView;
	}
	
	/**
	 * The <code>Cell</code> is an inner member static class, a holder for the cell.
	 * 
	 * @author Gibran E. Castillo
	 *
	 */
	static class Cell {
		public TextView textView;
		public ImageView imageView;
	}
}