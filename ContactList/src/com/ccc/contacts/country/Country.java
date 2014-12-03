package com.ccc.contacts.country;

/**
 * The <code>Country</code> is a lightweight class use by the Native Android 'ContactList' Mobile Application.
 * 
 * <p>Contains a Country's name and code.</p>
 * 
 * @author Gibran E. Castillo
 * @version 1.0, 2nd December 2014
 */
public class Country {
	// instance variables.
	private String countryCode;
	private String countryName;
	
	/**
	 * Returns the country's code.
	 * @return the country's code.
	 */
	public String getCountryCode() {
		return countryCode;
	}
	
	/**
	 * Sets the country's code.
	 * @param code a String value that represents the Country's code.
	 */
	public void setCountryCode(String code) {
		this.countryCode = code;
	}
	
	/**
	 * Returns the country's name.
	 * @return the country's name.
	 */
	public String getCountryName() {
		return countryName;
	}
	
	/**
	 * Sets the country's name.
	 * @param name a String value that represents the Country's name. 
	 */
	public void setCountryName(String name) {
		this.countryName = name;
	}
}