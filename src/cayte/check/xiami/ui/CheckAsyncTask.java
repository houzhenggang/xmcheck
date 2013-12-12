package cayte.check.xiami.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import cayte.check.handler.AccountInfo;
import cayte.check.handler.CheckinHandler;
import cayte.check.handler.CheckinHandler.LogCallback;

@SuppressLint("HandlerLeak")
public class CheckAsyncTask extends
		AsyncTask<AccountInfo, Void, ArrayList<AccountInfo>> implements
		LogCallback {
	private final int CHECK_START = 8;
	private final int CHECK_ONE = 1;
	private final int CHECK_ALL_END = 9;
	private final int CHECK_LOG = 4;

	private Context context;
	public CheckinHandler check = null;

	public boolean isRun = false;
	private boolean isCancel = false;

	public void cancel() {
		// TODO Auto-generated method stub
		isCancel = true;
		this.cancel(true);
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (isCancel || callBack == null)
				return;
			switch (msg.what) {
			case CHECK_START:
				callBack.CheckinStart();
				break;
			case CHECK_ONE:
				AccountInfo acc = (AccountInfo) msg.obj;
				callBack.CheckinOne(acc);
				break;
			case CHECK_ALL_END:
				AccountInfo[] arr = (AccountInfo[]) msg.obj;
				ArrayList<AccountInfo> accs = new ArrayList<AccountInfo>();
				for (AccountInfo a : arr) {
					accs.add(a);
				}
				callBack.CheckinEnd(accs);
				break;
			case CHECK_LOG:
				callBack.CheckinLog(msg.obj.toString());
				break;
			default:
				break;
			}
		}
	};

	public interface CheckinCallBack {
		public void CheckinStart();

		public void CheckinOne(AccountInfo acc);

		public void CheckinEnd(ArrayList<AccountInfo> accs);

		public void CheckinLog(String log);
	}

	private CheckinCallBack callBack = null;

	public CheckAsyncTask(Context context, CheckinCallBack callBack) {
		// TODO Auto-generated constructor stub
		create(context, callBack);
	}

	private void create(Context context, CheckinCallBack callBack) {
		// TODO Auto-generated method stub
		this.context = context;
		this.callBack = callBack;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		isRun = true;
		sendMessage(CHECK_START, null);
		check = new CheckinHandler(context);
		check.setLogCb(this);
	}

	@Override
	protected ArrayList<AccountInfo> doInBackground(AccountInfo... params) {
		// TODO Auto-generated method stub
		ArrayList<AccountInfo> accs = new ArrayList<AccountInfo>();
		if (params != null) {
			AccountInfo a = null;
			for (AccountInfo acc : params) {
				a = checkOne(acc);
				accs.add(a);
				sendMessage(CHECK_ONE, a);
			}
		}
		return accs;
	}

	@Override
	protected void onPostExecute(ArrayList<AccountInfo> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		AccountInfo[] accs = new AccountInfo[result.size()];
		for (int i = 0; i < accs.length; i++) {
			accs[i] = result.get(i);
		}
		sendMessage(CHECK_ALL_END, accs);
		isRun = false;
	}

	private AccountInfo checkOne(AccountInfo acc) {
		// TODO Auto-generated method stub
		acc = check.check(acc);
		return acc;
	}

	private void sendMessage(int what, Object obj) {
		// TODO Auto-generated method stub
		if (handler == null)
			return;
		Message msg = handler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	@Override
	public void Callback(String log) {
		// TODO Auto-generated method stub
		sendMessage(CHECK_LOG, log);
	}
}