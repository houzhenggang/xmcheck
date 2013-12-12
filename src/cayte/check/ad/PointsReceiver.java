package cayte.check.ad;

import net.youmi.android.offers.EarnPointsOrderList;
import android.content.Context;
import cayte.check.xiami.helper.NotifyUtil;

public class PointsReceiver extends net.youmi.android.offers.PointsReceiver {

	@Override
	protected void onEarnPoints(Context context, EarnPointsOrderList list) {
	}

	@Override
	protected void onViewPoints(Context context) {
		NotifyUtil.showPoints(context);
	}

}
