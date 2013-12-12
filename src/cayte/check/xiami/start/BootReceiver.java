package cayte.check.xiami.start;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

public class BootReceiver extends BroadcastReceiver {
	public ArrayList<String> actionList = new ArrayList<String>();

	public BootReceiver() {
		actionList = new ArrayList<String>();
		actionList.add(Intent.ACTION_BOOT_COMPLETED);
		actionList.add(Intent.ACTION_BATTERY_CHANGED);
		actionList.add(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
		actionList.add(Intent.ACTION_DATE_CHANGED);
		actionList.add(Intent.ACTION_HEADSET_PLUG);
		actionList.add(Intent.ACTION_SCREEN_OFF);
		actionList.add(Intent.ACTION_SCREEN_ON);
		actionList.add(Intent.ACTION_TIME_TICK);
		actionList.add(WifiManager.WIFI_STATE_CHANGED_ACTION);
		actionList.add(WifiManager.NETWORK_STATE_CHANGED_ACTION);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (actionList != null && actionList.contains(intent.getAction())) {
			Intent in = new Intent(context.getApplicationContext(),
					StartService.class);
			in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.getApplicationContext().startService(in);
		}
	}
}