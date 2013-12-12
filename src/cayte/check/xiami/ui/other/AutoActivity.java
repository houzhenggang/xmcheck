package cayte.check.xiami.ui.other;

import net.youmi.android.offers.PointsChangeNotify;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import cayte.check.ad.PointManager;
import cayte.check.handler.D;
import cayte.check.view.Item;
import cayte.check.xiami.Check;
import cayte.check.xiami.R;
import cayte.check.xiami.helper.DialogHelper;
import cayte.check.xiami.helper.DialogHelper.DismissCallBack;
import cayte.check.xiami.helper.SpfHelper;

import com.umeng.analytics.MobclickAgent;

public class AutoActivity extends Activity implements PointsChangeNotify {
	private Item integral, integralWall, removeAd, autoCheck;
	private DialogHelper dialogHelper;
	private SpfHelper spf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto);
		dialogHelper = new DialogHelper(this);
		spf = new SpfHelper(this);
		integral = new Item(findViewById(R.id.integral));
		integralWall = new Item(findViewById(R.id.integralWall));
		removeAd = new Item(findViewById(R.id.removeAd));
		autoCheck = new Item(findViewById(R.id.autoCheck));
		integral.setKey(R.string.integral);
		integralWall.setKey(R.string.integralWall);
		removeAd.setKey(R.string.removeAd);
		autoCheck.setKey(R.string.autoCheck);
		removeAd.setVaule(R.string.instructions);
		autoCheck.setVaule(R.string.instructions);
		integralWall.getKey().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (D.OWNER) {
					PointManager.instance(AutoActivity.this).awardPoints(100);
					onPointBalanceChange(0);
				} else {
					PointManager.instance(AutoActivity.this).openYoumiOffer(
							AutoActivity.this);
				}
			}
		});
		integralWall.getValue().setVisibility(View.GONE);
		removeAd.getKey().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				removeAd();
			}
		});
		autoCheck.getKey().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				autoCheck();
			}
		});
		autoCheck.getValue().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				autoCheckTip();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		integral.setVaule(PointManager.instance(this).queryPoints() + "");
		if (spf.isRemoveAd())
			removeAd.setVaule(R.string.open);
		else
			removeAd.setVaule(R.string.close);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onPointBalanceChange(int point) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				integral.changeVaule(PointManager.instance(AutoActivity.this)
						.queryPoints() + "");
			}
		});
	}

	private void removeAd() {
		if (spf.isRemoveAd()) {
			removeAd.changeVaule(R.string.open);
		} else {
			dialogHelper.showConfirmDialog(R.string.removeAdTip,
					new DismissCallBack() {

						@Override
						public void dismiss() {
							if (PointManager.instance(AutoActivity.this)
									.spendPoints(Check.REMOVEAD_POINT)) {
								spf.setRemoveAd(true);
								removeAd.changeVaule(R.string.open);
								onPointBalanceChange(0);
							} else {
								Toast.makeText(AutoActivity.this,
										R.string.pointsless, Toast.LENGTH_SHORT)
										.show();
							}
						}
					});
		}
	}

	private void autoCheck() {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setClass(this, AutoAccountActivity.class);
		this.startActivity(intent);
		this.overridePendingTransition(R.anim.slide_left_in,
				R.anim.slide_left_out);
	}

	private void autoCheckTip() {
		dialogHelper.showTipDialog(R.string.autoCheckTip);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

}
