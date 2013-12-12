package cayte.check.xiami.helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import cayte.check.handler.D;
import cayte.check.xiami.Check;
import cayte.check.xiami.R;

public class DialogHelper {
	private Activity act;
	private AlertDialog ad = null;

	public interface AddAccountCallBack {
		public void addDone(String name, String pass);
	}

	public interface RemoveAccountCallBack {
		public void removeDone();
	}

	public interface SelectAppbgTypeCallBack {
		public void onSelect(int lastWhich, int witch);
	}

	public interface DismissCallBack {
		public void dismiss();
	}

	public DialogHelper(Activity act) {
		// TODO Auto-generated constructor stub
		this.act = act;
	}

	public void showAddAccount(AddAccountCallBack cb, final DismissCallBack dcb) {
		if (ad != null && ad.isShowing())
			return;
		ad = createAddAccount(cb).create();
		ad.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				ad = null;
				if (dcb != null)
					dcb.dismiss();
			}
		});
		ad.show();
	}

	private AlertDialog.Builder createAddAccount(final AddAccountCallBack cb) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		LayoutInflater layout = LayoutInflater.from(act);
		View view = layout.inflate(R.layout.add_account_dialog, null);
		final EditText editName = (EditText) view.findViewById(R.id.inputName);
		final EditText editPass = (EditText) view.findViewById(R.id.inputPass);
		builder.setView(view);
		builder.setIcon(android.R.drawable.ic_menu_add);
		builder.setTitle(R.string.addAccountTip);
		builder.setPositiveButton(R.string.done, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (cb == null)
					return;
				String name = editName.getText().toString().trim();
				String pass = editPass.getText().toString().trim();
				if (Check.isNull(name)) {
					D.show(act, R.string.noEditNameTip);
					return;
				}
				if (Check.isNull(pass)) {
					D.show(act, R.string.noEditPassTip);
					return;
				}
				cb.addDone(name, pass);
			}
		});
		return builder;
	}

	public void showRemoveAccount(RemoveAccountCallBack cb,
			final DismissCallBack dcb) {
		if (ad != null && ad.isShowing())
			return;
		ad = createRemoveAccount(cb).create();
		ad.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				ad = null;
				if (dcb != null)
					dcb.dismiss();
			}
		});
		ad.show();
	}

	private AlertDialog.Builder createRemoveAccount(
			final RemoveAccountCallBack cb) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setIcon(android.R.drawable.ic_menu_delete);
		builder.setTitle(R.string.tip);
		builder.setMessage(R.string.removeAccountTip);
		builder.setPositiveButton(R.string.sure, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (cb == null)
					return;
				cb.removeDone();
			}
		});
		builder.setNegativeButton(R.string.cancel, null);
		return builder;
	}

	public void showSelectAppbgType(final int lastWhich,
			final SelectAppbgTypeCallBack cb) {
		if (ad != null && ad.isShowing())
			return;
		ad = createSelectAppbgType(lastWhich, cb).create();
		ad.show();
	}

	private AlertDialog.Builder createSelectAppbgType(final int lastWhich,
			final SelectAppbgTypeCallBack cb) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		String[] option = { act.getString(R.string.appbgvalue1),
				act.getString(R.string.appbgvalue2),
				act.getString(R.string.appbgvalue3),
				act.getString(R.string.appbgvalue4),
				act.getString(R.string.appbgvalue5) };
		builder.setItems(option, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (cb != null)
					cb.onSelect(lastWhich, which);
			}
		});
		return builder;
	}

	public void showTipDialog(int tipId) {
		if (ad != null && ad.isShowing())
			return;
		ad = createTipDialog(tipId).create();
		ad.show();
	}

	private AlertDialog.Builder createTipDialog(final int tipId) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setIcon(android.R.drawable.stat_sys_warning);
		builder.setTitle(R.string.instructions);
		builder.setMessage(tipId);
		builder.setPositiveButton(R.string.know, null);
		return builder;
	}

	public void showConfirmDialog(int msgId, DismissCallBack cb) {
		if (ad != null && ad.isShowing())
			return;
		ad = createConfirmDialog(msgId, cb).create();
		ad.show();
	}

	private AlertDialog.Builder createConfirmDialog(int msgId,
			final DismissCallBack cb) {
		AlertDialog.Builder builder = new AlertDialog.Builder(act);
		builder.setIcon(android.R.drawable.stat_sys_warning);
		builder.setTitle(R.string.tip);
		builder.setMessage(msgId);
		builder.setPositiveButton(R.string.sure, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (cb != null)
					cb.dismiss();
			}
		});
		builder.setNegativeButton(R.string.cancel, null);
		return builder;
	}

}
