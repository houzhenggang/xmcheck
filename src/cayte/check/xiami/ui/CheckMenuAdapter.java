package cayte.check.xiami.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cayte.check.handler.AccountInfo;
import cayte.check.xiami.R;
import cayte.check.xiami.helper.DBHelper;
import cayte.check.xiami.helper.DateHelper;

public class CheckMenuAdapter extends BaseAdapter {
	private List<AccountInfo> accs = null;
	private Context con = null;
	private String day, last;
	private DBHelper db = null;

	private ViewHolder holder = null;

	public CheckMenuAdapter(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
		this.db = new DBHelper(con);
		this.accs = new ArrayList<AccountInfo>();
		this.day = con.getApplicationContext().getString(R.string.itemDay);
		this.last = con.getApplicationContext().getString(R.string.itemLast);
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

	public void notifyDataAccChanged() {
		setAccs(db.query());
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = LayoutInflater.from(con).inflate(
					R.layout.check_menu_item, null);
			initHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AccountInfo acc = getItem(position);
		holder.name.setText(acc.name);
		if (acc.lastCheck <= 0)
			holder.last.setText(R.string._null);
		else
			holder.last.setText(String.format(last,
					DateHelper.getFriendlyDate(con, acc.lastCheck)));
		if (acc.day == 0) {
			holder.day.setText(R.string._null);
			holder.tian.setVisibility(View.INVISIBLE);
		} else {
			holder.day.setText(String.format(day, acc.day));
			holder.tian.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	private void initHolder(View v) {
		// TODO Auto-generated method stub
		holder = new ViewHolder();
		holder.name = (TextView) v.findViewById(R.id.name);
		holder.day = (TextView) v.findViewById(R.id.day);
		holder.last = (TextView) v.findViewById(R.id.last);
		holder.tian = (TextView) v.findViewById(R.id.tian);
	}

	private class ViewHolder {
		// TODO Auto-generated method stub
		TextView name, day, last, tian;
	}
}