package cayte.check.xiami;

import android.content.Context;
import android.telephony.TelephonyManager;
import cayte.check.ad.PointManager;
import cayte.check.handler.AccountInfo;
import cayte.check.xiami.helper.DBHelper;
import cayte.check.xiami.helper.SpfHelper;

import com.umeng.analytics.MobclickAgent;

public class Check {

	public static final String REMOVEAD_POINT = "sNmt57Dik0mM8tzT7oVLnI9V6k1z7C8xJudngzxM7MyyQ+nbHEpH+g==";
	public static final String AUTOCHECK_POINT = "yjEIsXoq0MjYUBL0n8cd5WXBp/A+5PUC0sD1B1MPpliyQ+nbHEpH+g==";

	public static final String ON = "on";
	public static final String OFF = "off";

	public static boolean isNull(String str) {
		if (str == null)
			return true;
		if (str.trim().equals(""))
			return true;
		return false;
	}

	public static String getAccount(Context con) {
		// TODO Auto-generated method stub
		TelephonyManager tm = (TelephonyManager) con
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();// String
		if (imei != null && !imei.equals("")) {
			return imei + "@cayte.check";
		}
		return null;
	}

	public static boolean checkPunish(Context con) {
		// TODO Auto-generated method stub
		String cur = getAccount(con);
		try {
			String punish_accounts = MobclickAgent.getConfigParams(con,
					"punish_imeis");
			punish_accounts.replaceAll("\n", "");
			String[] accs = punish_accounts.split(",");
			for (String a : accs) {
				if (a.trim().equals(cur)) {
					punish(con);
					return true;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}

	private static void punish(Context con) {
		int points = PointManager.instance(con).queryPoints();
		if (points > 0)
			PointManager.instance(con).spendPoints(points);
		SpfHelper spf = new SpfHelper(con);
		DBHelper db = new DBHelper(con);
		for (AccountInfo acc : db.query()) {
			spf.removeCanAutoCheck(acc.name);
			db.punish(acc.name);
		}
	}

}
