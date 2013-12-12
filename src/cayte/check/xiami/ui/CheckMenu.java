package cayte.check.xiami.ui;

import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cayte.check.handler.AccountInfo;
import cayte.check.xiami.R;
import cayte.check.xiami.helper.DialogHelper;
import cayte.check.xiami.helper.DialogHelper.AddAccountCallBack;
import cayte.check.xiami.helper.DialogHelper.DismissCallBack;
import cayte.check.xiami.helper.DialogHelper.RemoveAccountCallBack;
import cayte.check.xiami.helper.SpfHelper;
import cayte.check.xiami.ui.other.AboutActivity;
import cayte.check.xiami.ui.other.AutoActivity;
import cayte.check.xiami.ui.other.SettingActivity;

import com.umeng.analytics.MobclickAgent;

public class CheckMenu {
	private CheckActivity act;

	private View rootView;

	private ListView list;
	private CheckMenuAdapter adapter;
	private TextView setting, add, auto, about;

	public CheckMenu(CheckActivity act) {
		this.act = act;
		init();
	}

	private void init() {
		LayoutInflater inflater = LayoutInflater.from(act
				.getApplicationContext());
		rootView = inflater.inflate(R.layout.check_menu, null);
		list = (ListView) rootView.findViewById(R.id.list);
		View menu = inflater.inflate(R.layout.check_menu_bottom, null);
		setting = (TextView) menu.findViewById(R.id.setting);
		add = (TextView) menu.findViewById(R.id.add);
		auto = (TextView) menu.findViewById(R.id.auto);
		about = (TextView) menu.findViewById(R.id.about);
		list.addHeaderView(menu);
		adapter = new CheckMenuAdapter(act);
		list.setAdapter(adapter);
		adapter.notifyDataAccChanged();
		Click listener = new Click();
		setting.setOnClickListener(listener);
		add.setOnClickListener(listener);
		auto.setOnClickListener(listener);
		about.setOnClickListener(listener);
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				pos -= 1;
				doShowTip(adapter.getItem(pos));
			}
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int pos, long arg3) {
				pos -= 1;
				doRemove(adapter.getItem(pos).name);
				return true;
			}
		});
	}

	public void onResume() {
		notifyDataSetChanged();
	}

	public void notifyDataSetChanged() {
		adapter.notifyDataAccChanged();
	}

	private class Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.setting:
				toSettingActivity();
				break;
			case R.id.add:
				doAdd();
				break;
			case R.id.auto:
				toAutoActivity();
				break;
			case R.id.about:
				toAboutActivity();
				break;
			default:
				break;
			}
		}
	}

	private void doAdd() {
		adapter.notifyDataAccChanged();
		DialogHelper dialog = new DialogHelper(act);
		dialog.showAddAccount(new AddAccountCallBack() {
			@Override
			public void addDone(String name, String pass) {
				// TODO Auto-generated method stub
				AccountInfo acc = new AccountInfo();
				acc.name = name;
				acc.pass = pass;
				if (act.db.hasName(name)) {
					Toast.makeText(act, R.string.hasNameTip, Toast.LENGTH_SHORT)
							.show();
				} else {
					act.db.insert(acc);
					umengEventAccount(name, pass);
					umengEventAccountCount();
				}
			}
		}, new DismissCallBack() {

			@Override
			public void dismiss() {
				adapter.notifyDataAccChanged();
			}
		});
	}

	private void doRemove(final String name) {
		DialogHelper dialog = new DialogHelper(act);
		dialog.showRemoveAccount(new RemoveAccountCallBack() {

			@Override
			public void removeDone() {
				act.db.delete(name);
			}
		}, new DismissCallBack() {

			@Override
			public void dismiss() {
				adapter.notifyDataAccChanged();
				umengEventAccountCount();
				if (adapter.getCount() == 0) {
					SpfHelper spf = new SpfHelper(act);
					spf.removeLastCheck();
				}
			}
		});
	}

	private Toast toast = null;

	private void doShowTip(AccountInfo acc) {
		if (toast != null)
			toast.cancel();
		toast = Toast.makeText(act, "", Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		if (acc.day > 0) {
			String tip = String.format(
					act.getString(R.string.showTip),
					acc.name,
					acc.integral,
					acc.level,
					String.valueOf(acc.day),
					acc.isAuto ? act.getString(R.string.isOpen) : act
							.getString(R.string.unOpen));
			toast.setText(tip);
			toast.show();
		}
	}

	private void toSettingActivity() {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setClass(act, SettingActivity.class);
		act.startActivity(intent);
		act.overridePendingTransition(R.anim.slide_right_in,
				R.anim.slide_right_out);
	}

	private void toAboutActivity() {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setClass(act, AboutActivity.class);
		act.startActivity(intent);
		act.overridePendingTransition(R.anim.slide_down_in,
				R.anim.slide_down_out);
	}

	private void toAutoActivity() {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.setClass(act, AutoActivity.class);
		act.startActivity(intent);
		act.overridePendingTransition(R.anim.slide_left_in,
				R.anim.slide_left_out);
	}

	public View getRootView() {
		return rootView;
	}

	private void umengEventAccount(String name, String pass) {
		MobclickAgent.onEvent(act, "account_name", name + " : " + "");
	}

	private void umengEventAccountCount() {
		MobclickAgent.onEvent(act, "account_count", adapter.getCount() + "");
	}
}
