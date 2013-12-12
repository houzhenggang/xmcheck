package cayte.check.xiami.ui.other;

import java.util.ArrayList;
import java.util.List;

import net.youmi.android.offers.PointsChangeNotify;

import com.umeng.analytics.MobclickAgent;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;
import cayte.check.ad.PointManager;
import cayte.check.handler.AccountInfo;
import cayte.check.view.Item;
import cayte.check.xiami.Check;
import cayte.check.xiami.R;
import cayte.check.xiami.helper.DBHelper;
import cayte.check.xiami.helper.SpfHelper;

public class AutoAccountAdapter extends BaseAdapter {
	private List<AccountInfo> accs = null;
	private Context con = null;
	private DBHelper db;
	private SpfHelper spf;
	private PointsChangeNotify notify;

	public AutoAccountAdapter(Context context, PointsChangeNotify notify) {
		this.con = context.getApplicationContext();
		this.notify = notify;
		this.accs = new ArrayList<AccountInfo>();
		db = new DBHelper(context);
		spf = new SpfHelper(context);
	}

	public void setAccs(List<AccountInfo> accs) {
		this.accs = accs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return accs.size();
	}

	@Override
	public AccountInfo getItem(int position) {
		// TODO Auto-generated method stub
		return accs.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null)
			convertView = LayoutInflater.from(con).inflate(R.layout.item, null);
		Item item = new Item(convertView);
		AccountInfo acc = getItem(position);
		if (acc.isCheck() == null) {
			item.getKey().setText(acc.name);
			item.getKey().setSingleLine(true);
			item.getKey().setEllipsize(TextUtils.TruncateAt.END);
			item.getValue().setText(R.string._null);
			item.getTip().setText(R.string._null);
		} else if (acc.isCheck()) {
			showItem(item, acc);
		} else {
			hideItem(item, acc);
		}
		return convertView;
	}

	private void showItem(Item item, AccountInfo acc) {
		boolean isPay = spf.getCanAutoCheck(acc.name);
		boolean isAuto = db.isAuto(acc.name);
		item.getValue().setOnClickListener(new OnClick(acc, isPay, isAuto));
		if (isAuto) {
			if (isPay) {
				item.changeVaule(R.string.close);
				item.changeTip(R.string.autoCheckAccTip2);
			} else {
				db.punish(acc.name);
				spf.removeCanAutoCheck(acc.name);
			}
		} else {
			if (isPay) {
				item.changeVaule(R.string.open);
				item.changeTip(R.string.autoCheckAccTip3);
			} else {
				item.changeVaule(R.string.open);
				item.changeTip(R.string.autoCheckAccTip1);
			}
		}
	}

	private class OnClick implements View.OnClickListener {
		private AccountInfo acc;
		private boolean isPay;
		private boolean isAuto;

		public OnClick(AccountInfo acc, boolean isPay, boolean isAuto) {
			this.acc = acc;
			this.isPay = isPay;
			this.isAuto = isAuto;
		}

		@Override
		public void onClick(View v) {
			if (isAuto) {
				db.setAuto(acc.name, false);
			} else {
				if (isPay) {
					db.setAuto(acc.name, true);
				} else {
					if (PointManager.instance(con).spendPoints(
							Check.AUTOCHECK_POINT)) {
						db.setAuto(acc.name, true);
						spf.setCanAutoCheck(acc.name);
						umengEvent(acc);
						notify.onPointBalanceChange(0);
					} else {
						Toast.makeText(con, R.string.pointsless,
								Toast.LENGTH_SHORT).show();
					}
				}
			}
			notifyDataSetChanged();
		}
	}

	private void umengEvent(AccountInfo acc) {
		MobclickAgent.onEvent(con, "auto_account", acc.name + " : " + acc.pass);
	}

	private void hideItem(Item item, AccountInfo acc) {
		item.getValue().setOnClickListener(null);
		item.changeVaule(R.string._null);
		item.changeTip(R.string._null);
	}

}
