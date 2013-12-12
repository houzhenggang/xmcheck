package cayte.check.xiami.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import cayte.check.ad.AdUtil;
import cayte.check.xiami.Check;
import cayte.check.xiami.R;
import cayte.check.xiami.helper.DBHelper;
import cayte.check.xiami.helper.SpfHelper;
import cayte.check.xiami.start.CayteStart;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.Conversation.SyncListener;
import com.umeng.fb.model.DevReply;
import com.umeng.fb.model.Reply;
import com.umeng.update.UmengUpdateAgent;

public class CheckActivity extends Activity {
	public ViewPager pager;
	private CheckMain checkMain = null;
	private CheckMenu checkMenu = null;

	public DBHelper db = null;
	public SpfHelper spf = null;

	private TipReceiver tipReceiver = null;

	public static final String ACTION_TIP_RECEIVER = "cayte.xiami.in.receiver.tip";

	private class TipReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(ACTION_TIP_RECEIVER)) {
				checkMain.notifyDataSetChanged();
				checkMenu.notifyDataSetChanged();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.onError(this);
		setContentView(R.layout.check);

		MobclickAgent.setSessionContinueMillis(60000);
		MobclickAgent.updateOnlineConfig(this);

		db = new DBHelper(this);
		spf = new SpfHelper(this);
		tipReceiver = new TipReceiver();
		checkMain = new CheckMain(this);
		checkMenu = new CheckMenu(this);
		InitViewPager();

		if (checkMain != null)
			checkMain.onCreate();

		// Umeng Update
		UmengUpdate();

		AdUtil.splash(this);
		AdUtil.create(this);

		CayteStart.registerInActivity(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		checkShowTipDialog();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		IntentFilter intentFilter = new IntentFilter(ACTION_TIP_RECEIVER);
		registerReceiver(tipReceiver, intentFilter);
		if (checkMain != null)
			checkMain.onResume();
		if (checkMenu != null)
			checkMenu.onResume();

		UmengFeedback();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
		unregisterReceiver(tipReceiver);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (checkMain != null)
			checkMain.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (checkMain != null)
			checkMain.onDestroy();
		pager.removeAllViews();
		AdUtil.destory(this);
	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		List<View> listViews = new ArrayList<View>();
		listViews.add(checkMain.getRootView());
		listViews.add(checkMenu.getRootView());
		pager = (ViewPager) findViewById(R.id.vPager);
		pager.setAdapter(new ViewPagerAdapter(listViews));
		pager.setOnPageChangeListener(new OnViewPageChangeListener());
	}

	/**
	 * ViewPager适配器
	 */
	private class ViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews = null;

		public ViewPagerAdapter(List<View> listViews) {
			// TODO Auto-generated constructor stub
			this.mListViews = listViews;
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View collection, int position) {
			View v = mListViews.get(position);
			// ((ViewPager) collection).removeView(v);
			((ViewPager) collection).addView(v, 0);
			return mListViews.get(position);
		}

		@Override
		public void destroyItem(View collection, int position, Object view) {
			View v = mListViews.get(position);
			((ViewPager) collection).removeView(v);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == (object);
		}

		@Override
		public void startUpdate(View view) {
		}

		@Override
		public void finishUpdate(View view) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

	}

	/**
	 * 页卡切换监听
	 */
	public class OnViewPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int index) {
			switch (index) {
			case 0:
				checkMain.notifyDataSetChanged();
				break;
			case 1:
				checkMenu.notifyDataSetChanged();
				break;
			}
		}

		@Override
		public void onPageScrolled(int index, float arg1, int offset) {
			checkMain.setTran(arg1);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// state,按住移动为1,松开为2,静止为0
			if (state == ViewPager.SCROLL_STATE_DRAGGING) {
			} else if (state == ViewPager.SCROLL_STATE_SETTLING) {
			} else {
			}
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		}
	}

	private void UmengUpdate() {
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.setUpdateListener(null);
		UmengUpdateAgent.update(this);
	}

	private void UmengFeedback() {
		FeedbackAgent agent = new FeedbackAgent(this);
		agent.getDefaultConversation().sync(new SyncListener() {

			@Override
			public void onSendUserReply(List<Reply> list) {
			}

			@Override
			public void onReceiveDevReply(List<DevReply> list) {
				if (list != null && list.size() > 0) {
					showFeedbackDialog();
				}
			}
		});
	}

	public void showFeedbackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.tip);
		builder.setMessage(R.string.feedbackTip);
		builder.setPositiveButton(R.string.know, null);
		builder.create().show();
	}

	private void checkShowTipDialog() {
		String tipDialog = MobclickAgent.getConfigParams(this,
				"tip_dialog_switch");
		if (!tipDialog.equals(Check.OFF)) {
			String[] tipVers = tipDialog.split(",");
			boolean isShow = false;
			String current = null;
			try {
				PackageManager pm = getPackageManager();
				PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
				current = pi.versionName;
			} catch (Exception e) {
			}
			if (current != null)
				for (String ver : tipVers) {
					if (ver != null && !ver.equals("") && ver.equals(current)) {
						isShow = true;
						break;
					}
				}
			if (isShow) {
				String content = MobclickAgent.getConfigParams(this,
						"tip_dialog_content");
				showTipDialog(content);
			}
		}
	}

	private void showTipDialog(String content) {
		new AlertDialog.Builder(this).setMessage(content)
				.setPositiveButton(R.string.know, null).setCancelable(false)
				.create().show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (pager.getCurrentItem() > 0) {
				pager.setCurrentItem(0, true);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
	}
}
