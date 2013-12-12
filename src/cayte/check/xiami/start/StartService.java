package cayte.check.xiami.start;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import cayte.check.xiami.service.CheckService;
import cayte.check.xiami.service.CheckServiceAIDL;

public class StartService extends IntentService {

	private static final String TAG = StartService.class.getSimpleName();

	public StartService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		AlarmUtil.end(this);
		AlarmUtil.start(this);
		doStart(getApplicationContext());
	}

	private CheckServiceAIDL aidlService = null;

	public void doStart(Context con) {
		Bundle args = new Bundle();
		Intent intent = new Intent(con, CheckService.class);
		intent.putExtras(args);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ServiceConnection aidlConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				aidlService = CheckServiceAIDL.Stub.asInterface(service);
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				aidlService = null;
			}
		};
		con.bindService(intent, aidlConnection, Context.BIND_AUTO_CREATE);
		if (aidlService != null) {
			try {
				if (!aidlService.isRun())
					con.startService(intent);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
