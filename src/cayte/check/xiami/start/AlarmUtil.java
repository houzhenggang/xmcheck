package cayte.check.xiami.start;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmUtil {
	private static final int CODE = 0x98;

	public static void start(Context con) {
		Intent intent = new Intent(con, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(con, CODE, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) con.getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		long interval = CayteStart.timeInterval * 60l * 1000l;
		long time = System.currentTimeMillis() + interval;
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, interval,
				sender);
	}

	public static void end(Context con) {
		Intent intent = new Intent(con, AlarmReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(con, CODE, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) con.getApplicationContext()
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}
}
