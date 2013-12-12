package cayte.check.xiami.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import cayte.check.xiami.R;

public class NotifyUtil {
	public static final int NOTIFICATION_POINTS = 0x52;// 积分获得提示
	public static final int NOTIFICATION_AUTOCHECK = 0x53;// 积分余额不足提示

	@SuppressWarnings("deprecation")
	public static void showPoints(Context con) {
		NotificationManager nm = (NotificationManager) con
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notif = getNotification(R.drawable.ic_launcher,
				con.getString(R.string.notification_ticker), 0, false, false);
		notif.setLatestEventInfo(con, con.getString(R.string.app_name),
				con.getString(R.string.notification_points), null);
		nm.notify(NOTIFICATION_POINTS, notif);
	}

	// public static void showAutoCheck(Context con) {
	// NotificationManager nm = (NotificationManager) con
	// .getSystemService(Context.NOTIFICATION_SERVICE);
	// Notification notif = getNotification(R.drawable.ic_launcher,
	// con.getString(R.string.notification_ticker), 0, true, true);
	// notif.setLatestEventInfo(con, con.getString(R.string.app_name),
	// con.getString(R.string.notification_autocheck), null);
	// nm.notify(NOTIFICATION_AUTOCHECK, notif);
	// }

	/**
	 * 初始化,在实例化之后调用
	 * 
	 * @param icon
	 *            图片资源id,R.drawable.
	 * @param ticker
	 *            滚动提示内容
	 * @param isOngoing
	 *            0可以被清除,1随程序运行,2不能被清除
	 * @param isSound
	 *            是否有提示声音
	 * @param isVibrate
	 *            是否有提示震动
	 * @return
	 */
	private static Notification getNotification(int icon, String ticker,
			int isOngoing, boolean isSound, boolean isVibrate) {
		Notification notification = new Notification();
		notification.icon = icon;
		notification.tickerText = ticker;
		notification.when = System.currentTimeMillis();

		switch (isOngoing) {
		default:
		case 0:
			notification.flags |= Notification.FLAG_AUTO_CANCEL;
			break;
		case 1:
			notification.flags |= Notification.FLAG_ONGOING_EVENT;
			break;
		case 2:
			notification.flags |= Notification.FLAG_NO_CLEAR;
			break;
		}

		if (isSound)
			notification.defaults |= Notification.DEFAULT_SOUND;
		if (isVibrate)
			notification.defaults |= Notification.DEFAULT_VIBRATE;

		return notification;
	}

}
