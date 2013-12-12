package cayte.check.handler;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 
 * DEBUG LOGCAT
 * 
 */
public class D {
	public static final String TAG = "cayte";

	/**
	 * 4 m
	 */
	public static final boolean OWNER = false;

	/**
	 * log debug
	 * 
	 * @param content
	 */
	public static void d(Object content) {
		Log.d(TAG, content.toString());
	}

	/**
	 * log debug
	 * 
	 * @param tag
	 * @param content
	 */
	public static void d(String tag, Object content) {
		Log.d(tag, content.toString());
	}

	/**
	 * log info
	 * 
	 * @param content
	 */
	public static void i(Object content) {
		Log.i(TAG, content.toString());
	}

	/**
	 * log info
	 * 
	 * @param tag
	 * @param content
	 */
	public static void i(String tag, Object content) {
		Log.i(tag, content.toString());
	}

	/**
	 * 
	 * log error
	 * 
	 * @param content
	 */
	public static void e(Object content) {
		Log.e(TAG, content.toString());
	}

	/**
	 * log error
	 * 
	 * @param tag
	 * @param content
	 */
	public static void e(String tag, Object content) {
		Log.e(tag, content.toString());
	}

	public static void show(Context con, Object text) {
		Toast.makeText(con.getApplicationContext(), text.toString(),
				Toast.LENGTH_SHORT).show();
	}

	public static void show(Context con, int text) {
		Toast.makeText(con.getApplicationContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * sleep thread
	 * 
	 * @param time
	 *            time
	 */
	public static void sleep(long time) {
		if (time <= 0)
			return;
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
	}

	public static boolean isNull(CharSequence cs) {
		// TODO Auto-generated method stub
		if (cs == null || cs.toString().trim().equals(""))
			return true;
		return false;
	}

}
