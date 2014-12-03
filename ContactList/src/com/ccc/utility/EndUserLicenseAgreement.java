package com.ccc.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.ccc.contacts.R;

/**
 * The <code>EndUserLicenseAgreement</code> class provides an End User License Agreement.  It shows a simple dialog with the license text, and two buttons.
 * If user clicks on 'cancel' button, mobile application closes and user will not be granted access to the mobile application.
 * If user clicks on 'accept' button, mobile application access is allowed and this choice is saved in preferences; so next time this will not show, until next upgrade.
 * 
 * @author Gibran E. Castillo
 * @version 1.0, 2nd December 2014
 * @see android.app.Activity
 * @see android.app.AlertDialog
 * @see android.app.Dialog
 */
public class EndUserLicenseAgreement {
	private String EULA_PREFIX = "eula_";
	private Activity mActivity;
	
	/**
	 * Constructs an EndUserLicenseAgreement object.
	 * 
	 * @param activityContext the activity object.
	 */
	public EndUserLicenseAgreement(Activity activityContext) {
		mActivity = activityContext;
	}
	
	/**
	 * Gets the Activity's package information
	 * 
	 * @return the Overall information about the contents of a package.  This corresponds to all of the information collected from AndroidManifest.xml. 
	 */
	private PackageInfo getPackageInfo() {
		PackageInfo packageInfo = null;
		
		try {
			packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_ACTIVITIES);
		} catch(PackageManager.NameNotFoundException nnfe) {
			nnfe.printStackTrace();
		}
		
		return packageInfo;
	}
	
	/**
	 * This method shows a simple dialog with the license text, and two buttons.  If user clicks on 'cancel' button, mobile application closes and user will not be
	 * granted access to the mobile application.  If user clicks on 'accept' button, mobile application access is allowed and this choice is saved in preferences; so
	 * next time this will not show, until next upgrade.
	 */
	public void show() {
		PackageInfo versionInfo = getPackageInfo();
		
		// The eulaKey changes every time you increment the version number in the AndroidManifest.xml
		final String eulaKey = EULA_PREFIX + versionInfo.versionCode;
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
		boolean hasBeenShown = prefs.getBoolean(eulaKey, false);
		
		if(hasBeenShown == false) {
			// EULA title
			String title = mActivity.getString(R.string.app_name) + " v" + versionInfo.versionName;
			
			// Updates and EULA text
			String message = mActivity.getString(R.string.updates) + "\n\n" + mActivity.getString(R.string.eula);
			
			// Disable orientation changes, to prevent parent activity re-initialization
            //mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity)
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton(R.string.accept, new Dialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					// Mark this version as read.
					SharedPreferences.Editor editor = prefs.edit();
					editor.putBoolean(eulaKey, true);
					editor.commit();
					dialogInterface.dismiss();  // Close dialog
					// Enable orientation changes based on device's sensor
                    //mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				}
			})
			.setNegativeButton(R.string.decline, new Dialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Close the activity or mobile application as they have declined the EULA
					mActivity.finish();
					//mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
				}
			});
			
			builder.create().show();
		}
	}
}