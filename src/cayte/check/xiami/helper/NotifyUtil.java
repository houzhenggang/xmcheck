package cayte.check.xiami.helper;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import cayte.check.xiami.R;

public class NotifyUtil {
	public static final int NOTIFICATION_POINTS = 0x52;// ���ֻ����ʾ
	public static final int NOTIFICATION_AUTOCHECK = 0x53;// ����������ʾ

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
	 * ��ʼ��,��ʵ����֮�����
	 * 
	 * @param icon
	 *            ͼƬ��Դid,R.drawable.
	 * @param ticker
	 *            ������ʾ����
	 * @param isOngoing
	 *            0���Ա����,1���������,2���ܱ����
	 * @param isSound
	 *            �Ƿ�����ʾ����
	 * @param isVibrate
	 *            �Ƿ�����ʾ��
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
