package cayte.check.ad;

import net.youmi.android.offers.OffersManager;
import net.youmi.android.offers.PointsManager;
import android.content.Context;
import android.util.Log;
import cayte.check.cryptic.Cryptic;
import cayte.check.handler.D;
import cayte.check.xiami.Check;

import com.umeng.analytics.MobclickAgent;

public class PointManager {

	private static PointManager instance;

	private Context context;

	public static PointManager instance(Context context) {
		if (instance == null) {
			instance = new PointManager(context);
		}

		return instance;
	}

	public PointManager(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context.getApplicationContext();
	}

	public void infoMsg(String msg) {
		Log.e("cayte", msg);
	}

	/**
	 * 查询积分
	 * 
	 * @param context
	 * @return
	 */
	public int queryPoints() {
		int points = PointsManager.getInstance(context).queryPoints();
		if (points >= 500)
			umengEventPointCount(points);
		return points;
	}

	private void umengEventPointCount(int point) {
		MobclickAgent.onEvent(context, "point_count",
				point + ">>>" + Check.getAccount(context));
	}

	/**
	 * 消费积分
	 * 
	 * @param context
	 * @param amount
	 * @return
	 */
	public boolean spendPoints(String amount) {
		int points = Integer.parseInt(Cryptic.decrypt(amount));
		return spendPoints(points);
	}

	/**
	 * 消费积分
	 * 
	 * @param context
	 * @param amount
	 * @return
	 */
	public boolean spendPoints(int amount) {
		return PointsManager.getInstance(context).spendPoints(amount);
	}

	/**
	 * 奖励积分
	 * 
	 * @param context
	 * @param amount
	 * @return
	 */
	public boolean awardPoints(int amount) {
		return PointsManager.getInstance(context).awardPoints(amount);
	}

	public void openYoumiOffer(Context act) {
		if (D.OWNER)
			return;
		// 显示积分墙
		OffersManager.getInstance(act).showOffersWall();
	}

}
