package com.ccc.calculator.gratuity;

import com.ccc.calculator.gratuity.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * The <code>GratuityCalculator</code> is the main Activity class for the Native Android 'Gratuity Calculator' Mobile Application.
 * 
 * <p>Calculates grand total using 10%, 15%, 20% gratuity and a custom service percentage gratuities.</p>
 * 
 * This free gratuity calculator features fast amount entry, ideal for those eating out.  With just a few taps you can easily
 * determine the gratuity to leave your server (a male waiter or a female waitress).  Easily round your gratuities up or down, to
 * provide you with the perfect gratuity every time.
 * 
 * The 'Gratuity Calculator' will even split your bill making it very simple to figure out what each person in your party owes in
 * case your party or group decide to go dutch.
 * 
 * @author Gibran E. Castillo
 * @version 1.0, 17th December 2013
 * @see android.app.Activity
 * @see android.view.ContextThemeWrapper
 */
public class GratuityCalculator extends Activity {
	// constants used when saving/restoring state
	private static final String AMOUNT = "AMOUNT";
	private static final String CUSTOM_SERVICE_PERCENT = "CUSTOM_SERVICE_PERCENT";
	private static final String CUSTOM_SPLIT_GRATUITY_SERVICE = "CUSTOM_SPLIT_GRATUITY_SERVICE";
	
	// instance variables
	private double currentAmount; // amount entered by the user, 0.0 default value
	private double serviceGratuityAmount;
	private double serviceTotalAmount;
	
	private int currentGratuityServicePercent = 18; // gratuity % set with the SeekBar, 18% default value
	private int currentSplitGratuityService = 5; // number of people splitting gratuity set with the SeekBar, 5 people default value
	
	private EditText gratuityTenPercentEditText; // displays 10% gratuity
	private EditText totalTenPercentEditText; // displays total with 10% gratuity
	private EditText gratuityFifteenPercentEditText; // displays 15% gratuity
	private EditText totalFifteenPercentEditText; // displays total with 15% gratuity
	private EditText amountEditText; // accepts user input for amount
	private EditText gratuityTwentyPercentEditText; // displays 20% gratuity
	private EditText totalTwentyPercentEditText;  // displays total with 20% gratuity
	private TextView serviceGratuityTextView; // displays service gratuity percentage
	private EditText gratuityServiceEditText; // displays service gratuity amount
	private EditText totalServiceEditText; // displays total with service gratuity
	
	private EditText gratuityTwoIndividualsEditText;
	private EditText totalTwoIndividualsEditText;
	private EditText gratuityThreeIndividualsEditText;
	private EditText totalThreeIndividualsEditText;
	private EditText gratuityFourIndividualsEditText;
	private EditText totalFourIndividualsEditText;
	private TextView splitServiceGratuityTextView;
	private EditText splitGratuityServiceEditText;
	private EditText splitTotalServiceEditText;
	
	/**
	 * Called when the GratuityCalculator activity is first created.
	 * 
	 * @param savedInstanceState a mapping from String values to various Parcelable types.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); // call superclass's version
		setContentView(R.layout.main); // inflate the GUI
		
		// Check whether we're recreating a previously destroyed instance, that is, if application just started or is being restored from memory.
		/*if(savedInstanceState != null) {
			// app is being restored from memory, not executed from scratch, restore value of members from saved state.
			// initialize the amount to saved amount
			currentAmount = savedInstanceState.getDouble(AMOUNT); 
			
			// initialize the service gratuity to saved gratuity percent 
			currentGratuityServicePercent = savedInstanceState.getInt(CUSTOM_SERVICE_PERCENT);
			
			// initialize the split gratuity service to saved number of people splitting gratuity 
			currentSplitGratuityService = savedInstanceState.getInt(CUSTOM_SPLIT_GRATUITY_SERVICE); 
		} else {
			// the application just started running, initialize members with default values for a new instance
			currentAmount = 0.0; // initialize the amount to zero.
			currentGratuityServicePercent = 18; // initialize the service gratuity to 18%
			currentSplitGratuityService = 5; // initialize the split gratuity service to 5 people
		}*/
		
		// get references to the 10%, 15% and 20% gratuity and total EditTexts
		gratuityTenPercentEditText = (EditText) findViewById(R.id.gratuityTenPercentEditText);
		totalTenPercentEditText = (EditText) findViewById(R.id.totalTenPercentEditText);
		gratuityFifteenPercentEditText = (EditText) findViewById(R.id.gratuityFifteenPercentEditText);
		totalFifteenPercentEditText = (EditText) findViewById(R.id.totalFifteenPercentEditText);
		gratuityTwentyPercentEditText = (EditText) findViewById(R.id.gratuityTwentyPercentEditText);
		totalTwentyPercentEditText = (EditText) findViewById(R.id.totalTwentyPercentEditText);
		
		// get references to the going dutch or split gratuity and total EditTexts between 2, 3, and 4 individuals
		gratuityTwoIndividualsEditText = (EditText) findViewById(R.id.gratuityTwoIndividualsEditText);
		totalTwoIndividualsEditText = (EditText) findViewById(R.id.totalTwoIndividualsEditText);
		gratuityThreeIndividualsEditText = (EditText) findViewById(R.id.gratuityThreeIndividualsEditText);
		totalThreeIndividualsEditText = (EditText) findViewById(R.id.totalThreeIndividualsEditText);
		gratuityFourIndividualsEditText = (EditText) findViewById(R.id.gratuityFourIndividualsEditText);
		totalFourIndividualsEditText = (EditText) findViewById(R.id.totalFourIndividualsEditText);
		
		// get the TextView displaying the service gratuity percentage
		serviceGratuityTextView = (TextView) findViewById(R.id.serviceGratuityTextView);
		
		// get the TextView displaying the split service gratuity
		splitServiceGratuityTextView = (TextView) findViewById(R.id.splitServiceGratuityTextView);
		
		// get the service gratuity and total EditTexts 
		gratuityServiceEditText = (EditText) findViewById(R.id.gratuityServiceEditText);
		totalServiceEditText = (EditText) findViewById(R.id.totalServiceEditText);
		
		// get the split service gratuity and split total EditTexts 
		splitGratuityServiceEditText = (EditText) findViewById(R.id.splitGratuityServiceEditText);
		splitTotalServiceEditText = (EditText) findViewById(R.id.splitTotalServiceEditText);
		
		// get the amountEditText 
		amountEditText = (EditText) findViewById(R.id.amountEditText);
		
		// amountEditTextWatcher handles amountEditText's onTextChanged event
		amountEditText.addTextChangedListener(amountTextWatcher);
		
		// get the SeekBar used to set the service gratuity amount
		SeekBar serviceSeekBar = (SeekBar) findViewById(R.id.serviceSeekBar);
		serviceSeekBar.setOnSeekBarChangeListener(serviceSeekBarListener);
		
		// get the SeekBar used to set the service gratuity amount
		SeekBar splitServiceSeekBar = (SeekBar) findViewById(R.id.splitServiceSeekBar);
		splitServiceSeekBar.setOnSeekBarChangeListener(splitServiceSeekBarListener);
	}
	
	/**
	 * restore values of currentAmount, custom currentGratuityServicePercent, and currentSplitGratuityService
	 * 
	 * @param savedInstanceState a mapping from String values to various Parcelable types.
	 */
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can restore the view hierarchy
	    super.onRestoreInstanceState(savedInstanceState);
	    
	    // initialize the amount to saved amount
	 	currentAmount = savedInstanceState.getDouble(AMOUNT); 
	 	
	 	// initialize the service gratuity to saved gratuity percent 
	 	currentGratuityServicePercent = savedInstanceState.getInt(CUSTOM_SERVICE_PERCENT);
	 	
	 	// initialize the split gratuity service to saved number of people splitting gratuity 
	 	currentSplitGratuityService = savedInstanceState.getInt(CUSTOM_SPLIT_GRATUITY_SERVICE); 
	}
	
	/**
	 * save values of currentAmount, custom currentGratuityServicePercent, and currentSplitGratuityService
	 * 
	 * @param savedInstanceState a mapping from String values to various Parcelable types.
	 */
	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		// Always call the superclass so it can save the view hierarchy state
		super.onSaveInstanceState(savedInstanceState);
		
		savedInstanceState.putDouble(AMOUNT, currentAmount);
		savedInstanceState.putInt(CUSTOM_SERVICE_PERCENT, currentGratuityServicePercent);
		savedInstanceState.putInt(CUSTOM_SPLIT_GRATUITY_SERVICE, currentSplitGratuityService);
	}
	
	/**
	 * An anonymous inner class, flavor two, that is, an implementer of the TextWatcher [abstract] interface.  An event-handling object that responds to amountEditText's events
	 */
	private TextWatcher amountTextWatcher = new TextWatcher() {
		
		/**
		 * Called when the user enters an amount.
		 * 
		 * @param currentAmountCharSeq an interface that represents an ordered set of characters and defines the methods to probe them, this case the amount. 
		 * @param start an interger value that represents the start.
		 * @param before an interger value that represents the before.
		 * @param count an interger value that represents the count.
		 */
		@Override
		public void onTextChanged(CharSequence currentAmountCharSeq, int start, int before, int count) {
			// convert amountEditText's text to a double
			try {
				currentAmount = Double.parseDouble(currentAmountCharSeq.toString());
			} catch(NumberFormatException e) {
				currentAmount = 0.0; // default if an exception occurs
			}
			
			// update the standard gratuity & total and the custom service gratuity EditTexts
			updateStandardGratuityAndTotal(); // update the 10%, 15% and 20% EditTexts
			updateCustomGratuityAndTotal(); // update the custom service gratuity EditTexts
			
			// update the standard split gratuity & split total and the custom split service gratuity EditTexts
			updateStandardSplitGratuityAndTotal(); // update the split gratuity and split total 2, 3, and 4 EditTexts
			updateCustomSplitGratuityAndTotal(); // update EditTexts for split custom service gratuity and total.
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// do nothing
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// do nothing
		}
	};
	
	/**
	 * An anonymous inner class, flavor one, that is, a subclass of the OnSeekBarChangeListener class.  Called when the user changes the position of the gratuity percentage SeekBar.
	 */
	private OnSeekBarChangeListener serviceSeekBarListener = new OnSeekBarChangeListener() {
		
		/**
		 * update currentServicePercent, then call updateCustomGratuityAndTotal
		 */
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			// sets currentServicePercent to position of the SeekBar's thumb
			currentGratuityServicePercent = seekBar.getProgress();
			updateCustomGratuityAndTotal(); // update EditTexts for service gratuity and total
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// do nothing
		}
		
		/**
		 * update the standard split gratuity & split total and the custom split service gratuity, when the custom service gratuity percent changes
		 */
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			updateStandardSplitGratuityAndTotal();  // update the split gratuity and split total 2, 3, and 4 EditTexts
			updateCustomSplitGratuityAndTotal(); // update EditTexts for split service gratuity and total.
		}
	};
	
	/**
	 * An anonymous inner class, flavor one, that is, a subclass of the OnSeekBarChangeListener class.  Called when the user changes the position of the number of individuals SeekBar.
	 */
	private OnSeekBarChangeListener splitServiceSeekBarListener = new OnSeekBarChangeListener() {
		
		/**
		 * update currentGratuitySplitService, then call updateCustomSplitGratuityAndTotal
		 */
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			if(progress == 0) {
				seekBar.setProgress(1);
			}
			
			// sets currentGratuitySplitService to position of the SeekBar's thumb
			currentSplitGratuityService = seekBar.getProgress();
			updateCustomSplitGratuityAndTotal(); // update EditTexts for split service gratuity and total.
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// do nothing
		}
		
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// do nothing
		}
	};
	
	/**
	 * updates 10, 15 and 20 percent gratuity and total EditTexts
	 */
	private void updateStandardGratuityAndTotal() {
		// calculate total with a ten percent gratuity
		double tenPercentGratuity = currentAmount * .1;
		double tenPercentTotal = currentAmount + tenPercentGratuity;
		
		// set gratuityTenPercentEditText's text to tenPercentGratuity
		gratuityTenPercentEditText.setText(String.format("%.02f", tenPercentGratuity));
		
		// set totalTenPercentEditText's text to tenPercentTotal
		totalTenPercentEditText.setText(String.format("%.02f", tenPercentTotal));
		
		// calculate total with a fifteen percent gratuity
		double fifteenPercentGratuity = currentAmount * .15;
		double fifteenPercentTotal = currentAmount + fifteenPercentGratuity;
		
		// set gratuityFifteenEditText's text to fifteenPercentGratuity
		gratuityFifteenPercentEditText.setText(String.format("%.02f", fifteenPercentGratuity));
		
		// set totalFifteenEditText's text to fifteenPercentTotal
		totalFifteenPercentEditText.setText(String.format("%.02f", fifteenPercentTotal));
		
		// calculate total with a twenty percent gratuity
		double twentyPercentGratuity = currentAmount * .20;
		double twentyPercentTotal = currentAmount + twentyPercentGratuity;
		
		// set gratuityTwentyEditText's text to twentyPercentGratuity
		gratuityTwentyPercentEditText.setText(String.format("%.02f", twentyPercentGratuity));
		
		// set totalTwentyEditText's text to twentyPercentTotal
		totalTwentyPercentEditText.setText(String.format("%.02f", twentyPercentTotal));
	}
	
	/**
	 * updates the service gratuity and total EditTexts
	 */
	private void updateCustomGratuityAndTotal() {
		// set serviceGratuityTextView's text to match the position of the SeekBar
		serviceGratuityTextView.setText(currentGratuityServicePercent + "%");
		
		// calculate the service gratuity amount
		serviceGratuityAmount = currentAmount * currentGratuityServicePercent * .01;
		
		// calculate the grand total, including the service gratuity
		serviceTotalAmount = currentAmount + serviceGratuityAmount;
		
		// display the gratuity and total amounts
		gratuityServiceEditText.setText(String.format("%.02f", serviceGratuityAmount));
		totalServiceEditText.setText(String.format("%.02f", serviceTotalAmount));
	}
	
	/**
	 * updates 2, 3 and 4 people split gratuity and split total EditTexts
	 */
	private void updateStandardSplitGratuityAndTotal() {
		// calculate split gratuity and split total between two individuals
		double twoIndividualsGratuity = serviceGratuityAmount / 2;
		double twoIndividualstTotal = serviceTotalAmount / 2;
		
		// set gratuityTwoIndividualsEditText's text to twoIndividualsGratuity
		gratuityTwoIndividualsEditText.setText(String.format("%.02f", twoIndividualsGratuity));
		
		// set totalTwoIndividualsEditText's text to twoIndividualstTotal
		totalTwoIndividualsEditText.setText(String.format("%.02f", twoIndividualstTotal));
		
		// calculate split gratuity and split total between three individuals
		double threeIndividualsGratuity = serviceGratuityAmount / 3;
		double threeIndividualstTotal = serviceTotalAmount / 3;
		
		// set gratuityThreeIndividualsEditText's text to threeIndividualsGratuity
		gratuityThreeIndividualsEditText.setText(String.format("%.02f", threeIndividualsGratuity));
		
		// set totalThreeIndividualsEditText's text to threeIndividualstTotal
		totalThreeIndividualsEditText.setText(String.format("%.02f", threeIndividualstTotal));
		
		// calculate split gratuity and split total between four individuals
		double fourIndividualsGratuity = serviceGratuityAmount / 4;
		double fourIndividualstTotal = serviceTotalAmount / 4;
		
		// set gratuityFourIndividualsEditText's text to fourIndividualsGratuity
		gratuityFourIndividualsEditText.setText(String.format("%.02f", fourIndividualsGratuity));
		
		// set totalFourIndividualsEditText's text to fourIndividualstTotal
		totalFourIndividualsEditText.setText(String.format("%.02f", fourIndividualstTotal));
	}
	
	/**
	 * updates the custom service split gratuity and split total EditTexts
	 */
	private void updateCustomSplitGratuityAndTotal() {
		// set splitServiceGratuityTextView's text to match the position of the SeekBar
		if(currentSplitGratuityService > 1) {
			splitServiceGratuityTextView.setText(currentSplitGratuityService + " People");
		} else {
			splitServiceGratuityTextView.setText(currentSplitGratuityService + " Person");
		}
		
		// calculate the custom split service gratuity amount
		double splitServiceGratuityAmount = serviceGratuityAmount / currentSplitGratuityService;
		
		// calculate the custom grand split total, including the split service gratuity
		double splitServiceTotalAmount = serviceTotalAmount / currentSplitGratuityService;
		
		// display the custom split gratuity and total amounts
		splitGratuityServiceEditText.setText(String.format("%.02f", splitServiceGratuityAmount));
		splitTotalServiceEditText.setText(String.format("%.02f", splitServiceTotalAmount));
	}
}