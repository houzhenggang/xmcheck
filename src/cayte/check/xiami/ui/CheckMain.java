package cayte.check.xiami.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.youmi.android.smart.SmartBannerManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cayte.check.handler.AccountInfo;
import cayte.check.handler.CheckinHandler;
import cayte.check.handler.D;
import cayte.check.view.ProgressView;
import cayte.check.xiami.R;
import cayte.check.xiami.helper.PaperHelper;
import cayte.check.xiami.ui.CheckAsyncTask.CheckinCallBack;

public class CheckMain {
	private CheckActivity act;

	private View rootView;
	private FrameLayout progressLayout;
	private TextView dayText, dayTip;
	private ScrollView dayDetailScroll;
	private TextView dayDetail;
	// private View tran;

	private CheckAsyncTask checkTask = null;

	private final Handler handler = new Handler();

	public CheckMain(CheckActivity act) {
		this.act = act;
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(act
				.getApplicationContext());
		rootView = inflater.inflate(R.layout.check_main, null);
		progressLayout = (FrameLayout) rootView
				.findViewById(R.id.progressLayout);
		dayText = (TextView) rootView.findViewById(R.id.dayText);
		dayTip = (TextView) rootView.findViewById(R.id.dayTip);
		dayDetailScroll = (ScrollView) rootView
				.findViewById(R.id.dayDetailScroll);
		dayDetail = (TextView) rootView.findViewById(R.id.dayDetail);
		// tran = rootView.findViewById(R.id.layoutTransparent);
	}

	@SuppressWarnings("deprecation")
	public View getRootView() {
		rootView.setBackgroundDrawable(act.getWallpaper());
		return rootView;
	}

	public void onCreate() {
		// ad
		if (!act.spf.isRemoveAd()) {
			SmartBannerManager.init(act);
		}

		setTextStyle();

		setBoldTextView(dayText);

		dayText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (isCheckTaskRun())
					return;
				// ad
				if (!act.spf.isRemoveAd()) {
					SmartBannerManager.show(act);
				}
				AccountInfo[] arr = act.db.queryArray();
				if (arr.length > 0)
					doCheckTask(arr);
				else {
					D.show(act, R.string.noAccountTip);
					act.pager.setCurrentItem(1, true);
				}
			}
		});
	}

	public void onResume() {
		initData();
		initDraw();
	}

	public void notifyDataSetChanged() {
		setTextStyle();
		initData();
	}

	public void onStop() {
		timer.cancel();
		timer.purge();
	}

	public void onDestroy() {
		cancelCheckTask();
	}

	private void initData() {
		// TODO Auto-generated method stub
		String lastCheckString = "";
		List<AccountInfo> accs = act.db.query();
		if (accs != null && accs.size() == 0) {
			lastCheckString = "";
		} else if (accs != null && accs.size() == 1) {
			if (accs.get(0).day > 0)
				lastCheckString = String.format(
						act.getString(R.string.simpleDetail), accs.get(0).day);
			else
				lastCheckString = "";
		} else {
			lastCheckString = act.spf.getLastCheckString();
		}
		accs = null;
		if (act.spf.checkLastCheckDate()) {
			dayText.setText(R.string.everydayCheckin);
			if (D.isNull(act.spf.getLastCheckString()))
				dayTip.setText("");
			else
				dayTip.setText(String.format(act.getString(R.string.itemLast),
						act.spf.getLastCheckString()));
		} else {
			dayText.setText(R.string.checkined);
			dayTip.setText(lastCheckString);
		}
	}

	@SuppressWarnings("deprecation")
	private void initDraw() {
		// change background
		switch (act.spf.getAppbgType()) {
		case 0:// ±ÚÖ½
			try {
				Drawable draw0 = new BitmapDrawable(
						PaperHelper.handlerWallpaper(act, act.getWallpaper()));
				rootView.setBackgroundDrawable(draw0);
			} catch (Exception e) {
				rootView.setBackgroundColor(Color.WHITE);
			}
			break;
		case 1:
			try {
				Drawable draw1 = new BitmapDrawable(
						PaperHelper.handlerWallpaper(act, act.getResources()
								.getDrawable(R.drawable.default_bg)));
				rootView.setBackgroundDrawable(draw1);
			} catch (Exception e) {
				rootView.setBackgroundColor(Color.WHITE);
			}
			break;
		default:
		case 2:
			rootView.setBackgroundColor(Color.WHITE);
			break;
		case 3:
			rootView.setBackgroundColor(Color.BLACK);
			break;
		case 4:
			try {
				Drawable draw4 = new BitmapDrawable(PaperHelper.getPaper(act));
				rootView.setBackgroundDrawable(draw4);
			} catch (Exception e) {
				rootView.setBackgroundColor(Color.WHITE);
			}
			break;
		}

		// change day detail
		if (act.spf.isShowDetailLog()) {
			dayDetailScroll.setVisibility(View.VISIBLE);
			dayDetailScroll.setOnTouchListener(startPanelListener);
		} else {
			dayDetail.setText("");
			dayDetailScroll.setVisibility(View.GONE);
		}
	}

	private boolean isCheckTaskRun() {
		// TODO Auto-generated method stub
		if (checkTask == null)
			return false;
		return checkTask.isRun;
	}

	private void cancelCheckTask() {
		// TODO Auto-generated method stub
		if (checkTask != null)
			checkTask.cancel();
		checkTask = null;
	}

	private void doCheckTask(AccountInfo[] arr) {
		// TODO Auto-generated method stub
		cancelCheckTask();
		checkTask = new CheckAsyncTask(act.getApplicationContext(),
				new CheckinCallBack() {
					private ProgressView progress = null;

					@Override
					public void CheckinStart() {
						int color = act.spf.getAppbgType() == 2 ? R.color.black
								: R.color.white;
						progress = new ProgressView(act, R.color.transparent,
								color);
						progress.show(progressLayout, 10f);
						// if (!act.spf.isRemoveAd()) {
						// //show ad
						// showDomob();
						// }
					}

					@Override
					public void CheckinOne(AccountInfo acc) {
						// TODO Auto-generated method stub
						if (acc.state == CheckinHandler.SUCCESS
								|| acc.state == CheckinHandler.IS_CHECKED) {
							act.db.updataOne(acc);
						}
					}

					@Override
					public void CheckinEnd(ArrayList<AccountInfo> accs) {
						// TODO Auto-generated method stub
						D.e("CheckinActivity : CheckinEnd");
						boolean success = true;
						StringBuffer sb = new StringBuffer();
						for (AccountInfo acc : accs) {
							if (acc.state != CheckinHandler.SUCCESS
									&& acc.state != CheckinHandler.IS_CHECKED) {
								if (D.isNull(sb))
									sb.append(act.getString(R.string.checkFail));
								sb.append("\n" + "-------------------");
								sb.append("\n" + acc.name);
								sb.append("\n" + acc.tip);
							}
						}
						success = D.isNull(sb);
						D.e("CheckinActivity : CheckinEnd , success is "
								+ success);
						if (success) {
							act.spf.saveLastCheck();
							dayText.setText(R.string.checkined);
							if (accs.size() == 1)
								dayTip.setText(String.format(
										act.getString(R.string.simpleDetail),
										accs.get(0).day));
							else
								dayTip.setText(act.spf.getLastCheckString());

						} else {
							// Toast.makeText(CheckinActivity.this, sb,
							// Toast.LENGTH_LONG).show();
							if (!act.spf.checkLastCheckDate()) {
								dayText.setText(R.string.checkined);
								if (accs.size() == 1)
									dayTip.setText(String.format(act
											.getString(R.string.simpleDetail),
											accs.get(0).day));
								else
									dayTip.setText(act.spf.getLastCheckString());
							} else {
								dayText.setText(R.string.everydayCheckin);
								dayTip.setText(R.string.tCheckFail);
							}
						}

						if (progress != null) {
							progress.dismiss(progressLayout);
							progress = null;
						}
						if (!act.spf.isRemoveAd()) {
							// close ad
						}
						fadeOutDayDetail();
					}

					@Override
					public void CheckinLog(String log) {
						// TODO Auto-generated method stub
						appendDayDetailText(log);
					}
				});
		checkTask.execute(arr);
	}

	private void setBoldTextView(TextView tv) {
		tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		tv.getPaint().setFakeBoldText(true);
	}

	private String dayDetailText = "";

	Runnable dayDetailTextRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			dayDetail.setText(dayDetailText);
			dayDetailScroll.scrollBy(0, dayDetail.getMeasuredHeight());
		}
	};

	private void appendDayDetailText(String dayDetailText) {
		// TODO Auto-generated method stub
		this.dayDetailText += dayDetailText + "\n";
		handler.post(dayDetailTextRunnable);
	}

	private void fadeOutDayDetail() {
		// TODO Auto-generated method stub
		startPanelTimer();
	}

	private boolean isDoPanelAnim = false;
	private Timer timer = new Timer(true);

	private boolean isPanelShow() {
		// TODO Auto-generated method stub
		return !dayDetail.getText().equals("");
	}

	private void startPanelTimer() {
		// TODO Auto-generated method stub
		timer.cancel();
		timer.purge();
		timer = new Timer(true);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				act.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						hidePanel();
					}
				});
			}
		}, 5000);
	}

	private void hidePanel() {
		// TODO Auto-generated method stub
		if (!isPanelShow())
			return;
		if (isDoPanelAnim)
			return;
		isDoPanelAnim = true;
		dayDetailScroll.clearAnimation();
		Animation anim = new AlphaAnimation(1f, 0f);
		anim.setDuration(500);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				dayDetailText = "";
				dayDetail.setText("");
				isDoPanelAnim = false;
			}
		});
		dayDetailScroll.startAnimation(anim);
	}

	private OnTouchListener startPanelListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				timer.cancel();
				timer.purge();
				break;
			case MotionEvent.ACTION_UP:
				startPanelTimer();
				break;
			default:
				break;
			}
			return false;
		}
	};

	private void setTextStyle() {
		switch (act.spf.getAppbgType()) {
		case 2:
			dayText.setTextColor(Color.BLACK);
			dayTip.setTextColor(Color.BLACK);
			dayDetail.setTextColor(Color.BLACK);
			dayText.setShadowLayer(2, 0, 0, Color.GRAY);
			dayTip.setShadowLayer(2, 0, 0, Color.GRAY);
			dayDetail.setShadowLayer(2, 0, 0, Color.GRAY);
			dayText.setOnTouchListener(null);
			break;
		case 3:
			dayText.setTextColor(Color.WHITE);
			dayTip.setTextColor(Color.WHITE);
			dayDetail.setTextColor(Color.WHITE);
			dayText.setShadowLayer(2, 0, 0, Color.GRAY);
			dayTip.setShadowLayer(2, 0, 0, Color.GRAY);
			dayDetail.setShadowLayer(2, 0, 0, Color.GRAY);
			dayText.setOnTouchListener(null);
			break;
		default:
			dayText.setTextColor(Color.WHITE);
			dayTip.setTextColor(Color.WHITE);
			dayDetail.setTextColor(Color.WHITE);
			dayText.setShadowLayer(5, 0, 0, Color.GRAY);
			dayTip.setShadowLayer(5, 0, 0, Color.GRAY);
			dayDetail.setShadowLayer(5, 0, 0, Color.GRAY);

			dayText.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						dayText.setShadowLayer(25, 0, 0, Color.GRAY);
						break;
					case MotionEvent.ACTION_UP:
						dayText.setShadowLayer(5, 0, 0, Color.GRAY);
						break;
					default:
						break;
					}
					return false;
				}
			});
			break;
		}
	}

	public void setTran(float f) {
		// tran.setBackgroundColor(Color.argb((int) (f * 178.5f), 0, 0, 0));
	}

}
