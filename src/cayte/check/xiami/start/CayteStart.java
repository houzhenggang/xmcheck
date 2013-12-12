package cayte.check.xiami.start;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class CayteStart {

	public static int timeInterval = 30;

	public static void registerInApplication(Context context) {
		BootReceiver bootReceiver = new BootReceiver();
		IntentFilter intentFilter = new IntentFilter();
		for (String a : bootReceiver.actionList) {
			intentFilter.addAction(a);
		}
		context.registerReceiver(bootReceiver, intentFilter);
	}

	public static void registerInActivity(Context context) {
		Intent in = new Intent(context.getApplicationContext(),
				StartService.class);
		in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.getApplicationContext().startService(in);
	}

}
