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
		// ��ʼ������ǽ�����������Activity�е��ø÷������г�ʼ��
		AdManager.getInstance(act).init("7ae2cdcd330603a4", "1483cd666a88dc78",
				false);
		// AdManager.getInstance(act).setEnableDebugLog(true);
	}

	public static void create(Activity act) {
		if (D.OWNER)
			return;
		// �������Ӧ�ó������Activity��onCreate�е��û���ǽ�����ӿ�
		OffersManager.getInstance(act).onAppLaunch();
	}

	public static void destory(Activity act) {
		if (D.OWNER)
			return;
		// �������Ӧ�ó������Activity��onDestroy�е��û���ǽ�˳��ӿ�
		OffersManager.getInstance(act).onAppExit();
	}

	public static void register(Activity act, PointsChangeNotify notify) {
		if (D.OWNER)
			return;
		// ע��:����ص���registerNotify��ע������������򽫵ò��������˻����䶯��֪ͨ��
		PointsManager.getInstance(act).registerNotify(notify);
	}

	public static void unregister(Activity act, PointsChangeNotify notify) {
		if (D.OWNER)
			return;
		// ע��:�������onDestroy�е���unRegisterNotify��ע������
		PointsManager.getInstance(act).unRegisterNotify(notify);
	}

	public static void showPlaque(Activity act) {
		if (D.OWNER)
			return;
		if (MobclickAgent.getConfigParams(act, "inter_cut").equals(Check.ON)) {

		}
	}
}
