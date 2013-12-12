package cayte.check.xiami.ui.other;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cayte.check.xiami.R;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.fb.model.DevReply;
import com.umeng.fb.model.Reply;
import com.umeng.fb.model.Conversation.SyncListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;

public class AboutActivity extends Activity implements OnClickListener {

	private TextView vomitSlot, version;
	private View update, feedback;
	private Toast toast;

	private FeedbackAgent agent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		agent = new FeedbackAgent(this);
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

		vomitSlot = (TextView) findViewById(R.id.vomitSlot);
		version = (TextView) findViewById(R.id.version);
		update = findViewById(R.id.update);
		feedback = findViewById(R.id.feedback);

		try {
			PackageManager pm = getPackageManager();
			PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
			version.setText(getString(R.string.iVersion) + pi.versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		update.setOnClickListener(this);
		feedback.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		String content = MobclickAgent.getConfigParams(this, "vomit_slot");
		if (content == null || content.isEmpty()) {
			vomitSlot.setText(R.string.vomitSlotContent);
		} else {
			vomitSlot.setText(content);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update:
			showToast(R.string.updateStart);
			update();
			break;
		case R.id.feedback:
			agent.startFeedbackActivity();
			break;
		default:
			break;
		}
	}

	private void update() {
		// TODO Auto-generated method stub
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateAutoPopup(false);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
			@Override
			public void onUpdateReturned(int updateStatus,
					UpdateResponse updateInfo) {
				switch (updateStatus) {
				case 0: // has update
					if (toast != null)
						toast.cancel();
					UmengUpdateAgent.showUpdateDialog(AboutActivity.this,
							updateInfo);
					break;
				case 1: // has no update
					showToast(R.string.updateNotUp);
					break;
				case 2: // none wifi
					showToast(R.string.updateNoWifi);
					break;
				case 3: // time out
					showToast(R.string.updateTimeOut);
					break;
				}
			}
		});

		UmengUpdateAgent.forceUpdate(this);
	}

	private void showToast(int resId) {
		// TODO Auto-generated method stub
		if (toast == null)
			toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		else
			toast.cancel();
		toast.setText(resId);
		toast.show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		overridePendingTransition(R.anim.slide_up_in, R.anim.slide_up_out);
	}

	public void showFeedbackDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(R.string.tip);
		builder.setMessage(R.string.feedbackTip);
		builder.setPositiveButton(R.string.know, null);
		builder.create().show();
	}
}
