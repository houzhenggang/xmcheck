package cayte.check.xiami.ui.other;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import cayte.check.handler.D;
import cayte.check.view.Item;
import cayte.check.xiami.R;
import cayte.check.xiami.helper.DialogHelper;
import cayte.check.xiami.helper.DialogHelper.SelectAppbgTypeCallBack;
import cayte.check.xiami.helper.PaperHelper;
import cayte.check.xiami.helper.SpfHelper;

import com.umeng.analytics.MobclickAgent;

public class SettingActivity extends Activity {

	private SpfHelper spf;

	private Item appbg, islog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		appbg = new Item(findViewById(R.id.appbg));
		islog = new Item(findViewById(R.id.islog));
		appbg.setKey(R.string.appbgkey);
		islog.setKey(R.string.islogkey);
		spf = new SpfHelper(this);
		appbg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int currType = (Integer) v.getTag();
				DialogHelper dialog = new DialogHelper(SettingActivity.this);
				dialog.showSelectAppbgType(currType,
						new SelectAppbgTypeCallBack() {

							@Override
							public void onSelect(int lastWhich, int witch) {
								onSelectAppbg(lastWhich, witch);
							}
						});
			}
		});
		islog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isShow = !spf.isShowDetailLog();
				spf.setShowDetailLog(isShow);
				if (isShow) {
					islog.changeVaule(R.string.open);
				} else {
					islog.changeVaule(R.string.close);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
		initData();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	private void initData() {
		int type = spf.getAppbgType();
		switch (type) {
		default:
		case 0:
			appbg.setVaule(R.string.appbgvalue1);
			break;
		case 1:
			appbg.setVaule(R.string.appbgvalue2);
			break;
		case 2:
			appbg.setVaule(R.string.appbgvalue3);
			break;
		case 3:
			appbg.setVaule(R.string.appbgvalue4);
			break;
		case 4:
			appbg.setVaule(R.string.appbgvalue5);
			break;
		}
		appbg.getItem().setTag(type);

		if (spf.isShowDetailLog()) {
			islog.setVaule(R.string.open);
		} else {
			islog.setVaule(R.string.close);
		}
	}

	private void onSelectAppbg(int lastWhich, int witch) {
		switch (witch) {
		default:
		case 0:
			spf.setAppbgType(0);
			appbg.changeVaule(R.string.appbgvalue1);
			if (lastWhich == 4)
				PaperHelper.setPaper(this, null);
			break;
		case 1:
			spf.setAppbgType(1);
			appbg.changeVaule(R.string.appbgvalue2);
			if (lastWhich == 4)
				PaperHelper.setPaper(this, null);
			break;
		case 2:
			spf.setAppbgType(2);
			appbg.changeVaule(R.string.appbgvalue3);
			if (lastWhich == 4)
				PaperHelper.setPaper(this, null);
			break;
		case 3:
			spf.setAppbgType(3);
			appbg.changeVaule(R.string.appbgvalue4);
			if (lastWhich == 4)
				PaperHelper.setPaper(this, null);
			break;
		case 4:
			doPhoto4Location();
			break;
		}
	}

	private static final int LOC = 0;

	private void doPhoto4Location() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, LOC);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case LOC:
				try {
					if (data == null)
						return;
					Uri uri = data.getData();
					if (uri == null)
						return;
					String imagePath = null;
					if (uri.getScheme().equals("content")) {
						ContentResolver cr = getContentResolver();
						Cursor mCur = cr.query(uri, null, null, null, null);
						if (mCur == null)
							return;
						if (!mCur.moveToFirst()) {
							if (mCur != null) {
								mCur.close();
								mCur = null;
							}
							return;
						}
						imagePath = mCur
								.getString(mCur.getColumnIndex("_data"));
						if (mCur != null) {
							mCur.close();
							mCur = null;
						}
					} else if (uri.getScheme().equals("file")) {
						imagePath = uri.getPath();
					}
					if (imagePath == null)
						return;
					doPhotoHandler(imagePath);
				} catch (Exception e) {
					// TODO: handle exception
					D.show(this, R.string.setPaperFailTip);
				}
			}
		}
	}

	private void doPhotoHandler(String imagePath) {
		Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
		bitmap = PaperHelper.handlerWallpaper(this, bitmap);
		PaperHelper.setPaper(this, bitmap);
		spf.setAppbgType(4);
		appbg.changeVaule(R.string.appbgvalue5);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		this.finish();
		overridePendingTransition(R.anim.slide_left_in, R.anim.slide_left_out);
	}
}
