package cayte.check.handler;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 
 * get mobile net information
 * 
 */
public class NetCheck {

	/** Check the phone network status. */
	public static boolean isConnected(Context context) {
		Context con = context.getApplicationContext();
		ConnectivityManager mConManager = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		con = null;
		if (mConManager != null) {
			NetworkInfo info = mConManager.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}

	/** Indicates whether network connectivity is possible. */
	public static boolean isNetSucces(Context context) {
		Context con = context.getApplicationContext();
		ConnectivityManager cwjManager = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		con = null;
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}
}
