package cayte.check.ad;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsChangeNotify;
import net.youmi.android.offers.PointsManager;
import android.app.Activity;
import cayte.check.handler.D;
import cayte.check.xiami.Check;

import com.umeng.analytics.MobclickAgent;

public class AdUtil {
	public static void splash(Activity act) {
		if (D.OWNER)
			return;
		// 初始化积分墙，请务必在主Activity中调用该方法进行初始化
		AdManager.getInstance(act).init("7ae2cdcd330603a4", "1483cd666a88dc78",
				false);
		// AdManager.getInstance(act).setEnableDebugLog(true);
	}

	public static void create(Activity act) {
		if (D.OWNER)
			return;
		// 请务必在应用程序的主Activity的onCreate中调用积分墙启动接口
		OffersManager.getInstance(act).onAppLaunch();
	}

	public static void destory(Activity act) {
		if (D.OWNER)
			return;
		// 请务必在应用程序的主Activity的onDestroy中调用积分墙退出接口
		OffersManager.getInstance(act).onAppExit();
	}

	public static void register(Activity act, PointsChangeNotify notify) {
		if (D.OWNER)
			return;
		// 注意:请务必调用registerNotify来注册监听器，否则将得不到积分账户余额变动的通知。
		PointsManager.getInstance(act).registerNotify(notify);
	}

	public static void unregister(Activity act, PointsChangeNotify notify) {
		if (D.OWNER)
			return;
		// 注意:请务必在onDestroy中调用unRegisterNotify来注销监听
		PointsManager.getInstance(act).unRegisterNotify(notify);
	}

	public static void showPlaque(Activity act) {
		if (D.OWNER)
			return;
		if (MobclickAgent.getConfigParams(act, "inter_cut").equals(Check.ON)) {

		}
	}
}
