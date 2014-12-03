package com.ccc.contacts.country;

/**
 * The <code>CountryListListener</code> is an interface use for the Native Android 'ContactList' Mobile Application.
 * 
 * @author Gibran E. Castillo
 * @version 1.0, 2nd December 2014
 */
public interface CountryListListener {
	public void onSelectCountry(String name, String code);
}