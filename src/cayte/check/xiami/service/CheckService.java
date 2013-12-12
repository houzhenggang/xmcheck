package cayte.check.xiami.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;
import cayte.check.handler.AccountInfo;
import cayte.check.handler.CheckinHandler;
import cayte.check.handler.D;
import cayte.check.handler.NetCheck;
import cayte.check.xiami.Check;
import cayte.check.xiami.helper.DBHelper;
import cayte.check.xiami.helper.SpfHelper;
import cayte.check.xiami.ui.CheckActivity;

import com.umeng.analytics.MobclickAgent;

public class CheckService extends Service implements Runnable {
	private static final String TAG = CheckService.class.getSimpleName();

	private static final long SLEEP_TIME = 5 * 60 * 1000;
	private boolean isRun = false;

	private DBHelper dataBase = null;
	private CheckinHandler checkin = null;

	private final CheckServiceAIDL.Stub mBinder = new CheckServiceAIDL.Stub() {

		@Override
		public boolean isRun() throws RemoteException {
			// TODO Auto-generated method stub
			return isRun;
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		isRun = true;
		D.d(">>>onCreate<<<");

		dataBase = new DBHelper(this);
		checkin = new CheckinHandler(this);

		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRun) {
			if (D.OWNER ? checkTime4Owner() : checkTime()) {
				updateOnlintConfig();
				if (dataBase.isHasAutoCheck()) {
					if (NetCheck.isConnected(this)) {
						if (!Check.checkPunish(this)) {
							doCheckin();
						}
					}
				}
			}
			D.sleep(SLEEP_TIME);
		}
	}

	private static final String KEY_SPF_FILE = "cayte_service";
	private static final String HOUR = "hour";
	private static final String MINUTES = "minutes";

	@SuppressWarnings("deprecation")
	private boolean checkTime() {
		// TODO Auto-generated method stub.
		SharedPreferences spf = getSharedPreferences(KEY_SPF_FILE, 0);
		int hour = spf.getInt(HOUR, 2);
		int minutes = spf.getInt(MINUTES, 10);
		Date date = new Date();
		int h = date.getHours();
		int m = date.getMinutes();
		if (h == 0 && (m == 8 || m == 9)) {
			Random random = new Random();
			hour = random.nextInt(2);
			minutes = random.nextInt(60);
			if (hour == 0 && minutes < 20)
				minutes += 20;
			spf.edit().putInt(HOUR, hour).commit();
			spf.edit().putInt(MINUTES, minutes).commit();
		}
		if (h < hour)
			return false;
		if (h == hour && m < minutes)
			return false;
		return true;
	}

	@SuppressWarnings("deprecation")
	private boolean checkTime4Owner() {
		// TODO Auto-generated method stub
		Date date = new Date();
		int h = date.getHours();
		int m = date.getMinutes();
		if (h == 0 && m < 10)
			return false;
		if (h == 23 && m > 50)
			return false;
		return true;
	}

	private int lastHour = -3;

	@SuppressWarnings("deprecation")
	private void updateOnlintConfig() {
		// TODO Auto-generated method stub
		int curHour = new Date().getHours();
		if (curHour - lastHour >= 3) {
			MobclickAgent.updateOnlineConfig(this);
			lastHour = curHour;
		}
	}

	private void doCheckin() {
		// TODO Auto-generated method stub
		D.e(TAG + " : Checkin Start");

		SpfHelper spf = new SpfHelper(this);

		List<AccountInfo> arr = dataBase.query();
		if (arr == null || arr.isEmpty())
			return;
		int allSize = arr.size();

		List<AccountInfo> accs = new ArrayList<AccountInfo>();
		AccountInfo a = null;
		for (AccountInfo acc : arr) {
			if (!acc.isAuto)
				continue;
			if (!spf.getCanAutoCheck(acc.name)) {
				dataBase.punish(acc.name);
				spf.removeCanAutoCheck(acc.name);
				continue;
			}
			if (!isNeedCheck(acc.lastCheck))
				continue;
			if (!checkISCheckInNet(acc)) {
				dataBase.punish(acc.name);
				spf.removeCanAutoCheck(acc.name);
				continue;
			}
			a = checkin.check(acc);
			accs.add(a);
		}
		if (accs == null || accs.isEmpty())
			return;
		int checkSize = 0;

		for (AccountInfo acc : accs) {
			if (acc.state == CheckinHandler.SUCCESS) {
				checkSize++;
				dataBase.updataOne(acc);
			} else if (acc.state == CheckinHandler.IS_CHECKED) {
				checkSize++;
				dataBase.updataOne(acc);
			} else {
			}
		}

		if (checkSize == allSize) {
			spf.saveLastCheck();
		}
		sendTipBroadcast();

		D.e(TAG + " : Checkin End");
	}

	private boolean isNeedCheck(long time) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR, 0);
		c.set(Calendar.MINUTE, 10);
		c.set(Calendar.SECOND, 0);
		return time < c.getTimeInMillis();
	}

	private boolean checkISCheckInNet(AccountInfo acc) {
		return true;
	}

	private void sendTipBroadcast() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction(CheckActivity.ACTION_TIP_RECEIVER);
		intent.putExtra("success", true);
		sendBroadcast(intent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		D.d(TAG, "-----onDestroy-----");
		isRun = false;
		super.onDestroy();
	}

}
