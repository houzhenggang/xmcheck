package cayte.check.xiami.helper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import cayte.check.cryptic.Cryptic;
import cayte.check.xiami.Check;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

@SuppressLint("SimpleDateFormat")
public class SpfHelper {
	private SharedPreferences spf = null;
	private Context con = null;

	public SpfHelper(Context context) {
		// TODO Auto-generated constructor stub
		this.con = context.getApplicationContext();
		this.spf = context.getApplicationContext().getSharedPreferences(
				"cayte", 4);
	}

	/***************************************************************************************/
	/**
	 * Ç©µ½
	 */
	public String getLastCheckDateString() {
		return spf.getString("lastCheck", "");
	}

	public String getLastCheckString() {
		return DateHelper.getFriendlyDate(con, spf.getString("lastCheck", ""));
	}

	private Date getLastCheck() {
		String string = spf.getString("lastCheck", null);
		if (string == null)
			return null;
		else
			return getStringDate(string);
	}

	/**
	 * 
	 * @param last
	 * @param now
	 * @return true is need check
	 */
	@SuppressWarnings("deprecation")
	public boolean checkLastCheckDate() {
		Date last = getLastCheck();
		if (last == null)
			return true;
		Date now = new Date();
		boolean res = false;
		if (now.getYear() > last.getYear()) {
			res = true;
		} else if (now.getYear() < last.getYear()) {
			res = false;
		} else {
			if (now.getMonth() > last.getMonth()) {
				res = true;
			} else if (now.getMonth() < last.getMonth()) {
				res = false;
			} else {
				if (now.getDate() > last.getDate()) {
					res = true;
				} else if (now.getDate() < last.getDate()) {
					res = false;
				} else {
					res = false;
				}
			}
		}
		return res;
	}

	public void saveLastCheck() {
		// TODO Auto-generated method stub
		spf.edit().putString("lastCheck", getDateString()).commit();
	}

	public void removeLastCheck() {
		// TODO Auto-generated method stub
		spf.edit().remove("lastCheck").commit();
	}

	private String getDateString() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(new Date());
	}

	private Date getStringDate(String string) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.parse(string);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/***************************************************************************************/
	/**
	 * ÉèÖÃ
	 */

	public int getAppbgType() {
		return spf.getInt("appbgType", 0);
	}

	public void setAppbgType(int type) {
		spf.edit().putInt("appbgType", type).commit();
	}

	public boolean isShowDetailLog() {
		return spf.getBoolean("isShowDetailLog", true);
	}

	public void setShowDetailLog(boolean isShowDetailLog) {
		spf.edit().putBoolean("isShowDetailLog", isShowDetailLog).commit();
	}

	/***************************************************************************************/
	public boolean isRemoveAd() {
		String is = spf.getString("removeAd", null);
		if (is == null)
			return false;
		if (Cryptic.decrypt(is).equals(Check.getAccount(con) + "-" + Check.ON))
			return true;
		return false;
	}

	public void setRemoveAd(boolean isRemoveAd) {
		if (isRemoveAd)
			spf.edit()
					.putString(
							"removeAd",
							Cryptic.encrypt(Check.getAccount(con) + "-"
									+ Check.ON)).commit();
		else
			spf.edit().remove("removeAd").commit();
	}

	public boolean getCanAutoCheck(String name) {
		String can = spf.getString(name, null);
		if (can == null)
			return false;
		if (Cryptic.decrypt(can).equals(name + "-" + Check.ON))
			return true;
		return false;
	}

	public void setCanAutoCheck(String name) {
		if (name == null)
			return;
		spf.edit().putString(name, Cryptic.encrypt(name + "-" + Check.ON))
				.commit();
	}

	public void removeCanAutoCheck(String name) {
		if (name == null)
			return;
		spf.edit().remove(name).commit();
	}
}
