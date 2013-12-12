package cayte.check.xiami.start;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent in = new Intent(context.getApplicationContext(),
				StartService.class);
		in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.getApplicationContext().startService(in);
	}

}
