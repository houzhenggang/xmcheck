package cayte.check.xiami;

import android.app.Application;
import cayte.check.handler.D;
import cayte.check.xiami.helper.CrashHelper;
import cayte.check.xiami.start.CayteStart;

public class CheckApp extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		D.e("======== Application Start ========");

		CrashHelper.instance().init(this);
		Thread.setDefaultUncaughtExceptionHandler(CrashHelper.instance());

		CayteStart.registerInApplication(this);
	}
}
