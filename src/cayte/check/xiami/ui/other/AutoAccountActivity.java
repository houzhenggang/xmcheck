package cayte.check.xiami.ui.other;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.youmi.android.offers.PointsChangeNotify;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cayte.check.ad.PointManager;
import cayte.check.handler.AccountInfo;
import cayte.check.view.Item;
import cayte.check.xiami.R;
import cayte.check.xiami.helper.DBHelper;

import com.umeng.analytics.MobclickAgent;

public class AutoAccountActivity extends Activity implements PointsChangeNotify {
	private Item integral;
	private ListView list;
	private AutoAccountAdapter adapter;
	private DBHelper db;
	private List<AccountInfo> accs;
	private int lastIndex = -1;
	private boolean isItemClick = true;
	private Timer timer = new Timer(true);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auto_account);
		db = new DBHelper(this);
		accs = db.query();
		integral = new Item(findViewById(R.id.integral));
		integral.setKey(R.string.integral);
		list = (ListView) findViewById(R.id.list);
		adapter = new AutoAccountAdapter(this, this);
		adapter.setAccs(accs);
		list.setAdapter(adapter);
		notifyDataSetChanged();

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos,
					long arg3) {
				if (!isItemClick)
					return;
				isItemClick = false;
				startTimer();
				if (lastIndex == pos) {
					accs.get(pos).change();
				} else {
					if (lastIndex > -1 && lastIndex < adapter.getCount())
						accs.get(lastIndex).setCheck(false);
					accs.get(pos).setCheck(true);
					lastIndex = pos;
				}
				notifyDataSetChanged();
			}
		});
	}

	private void startTimer() {
		// TODO Auto-generated method stub
		timer.cancel();
		timer.purge();
		timer = new Timer(true);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				isItemClick = true;
			}
		}, 500);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		integral.setVaule(PointManager.instance(this).queryPoints() + "");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	public void notifyDataSetChanged() {
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onPointBalanceChange(int point) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				integral.changeVaule(PointManager.instance(
						AutoAccountActivity.this).queryPoints()
						+ "");
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
	}

}
