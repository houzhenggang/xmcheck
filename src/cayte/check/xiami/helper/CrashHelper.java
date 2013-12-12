package cayte.check.xiami.helper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;
import cayte.check.handler.D;
import cayte.check.xiami.R;

import com.umeng.analytics.MobclickAgent;

public class CrashHelper implements UncaughtExceptionHandler {
	public static final String TAG = "CrashHelper";

	private Thread.UncaughtExceptionHandler defaultHandler;
	private static CrashHelper INSTANCE;
	private Context con;

	private CrashHelper() {
	}

	public static CrashHelper instance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashHelper();
		}
		return INSTANCE;
	}

	public void init(Context context) {
		con = context;
		defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && defaultHandler != null) {
			defaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				Log.e(TAG, "Error : ", e);
			}
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);
		}
	}

	private boolean handleException(Throwable ex) {
		if (ex == null) {
			Log.w(TAG, "handleException --- ex==null");
			return true;
		}

		String dateinfo = getDateInfo();
		String versioninfo = getVersionInfo();
		String mobileinfo = getMobileInfo();
		String errorinfo = getErrorInfo(ex);

		if (errorinfo == null || errorinfo.equals("")) {
			return false;
		}
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast toast = Toast.makeText(con.getApplicationContext(),
						con.getString(R.string.errorTip), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				Looper.loop();
			}
		}.start();

		D.e(">>>>>>>>>> error <<<<<<<<<<");
		D.e(errorinfo);

		MobclickAgent.reportError(con, dateinfo + "\n" + versioninfo + "\n"
				+ mobileinfo + "\n" + errorinfo);
		return true;
	}

	private String getErrorInfo(Throwable ex) {
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		ex.printStackTrace(pw);
		pw.close();
		String error = writer.toString();
		return error;
	}

	private String getMobileInfo() {
		StringBuffer sb = new StringBuffer();
		try {

			Field[] fields = Build.class.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				String name = field.getName();
				String value = field.get(null).toString();
				sb.append(name + "=" + value);
				sb.append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString() + "\n=============================";
	}

	private String getVersionInfo() {
		try {
			PackageManager pm = con.getPackageManager();
			PackageInfo info = pm.getPackageInfo(con.getPackageName(), 0);
			return "Version : " + info.versionName
					+ "\n=============================";
		} catch (Exception e) {
			e.printStackTrace();
			return "Version unknown!" + "\n=============================";
		}
	}

	@SuppressLint("SimpleDateFormat")
	private String getDateInfo() {
		// TODO Auto-generated method stub
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd-HH-mm-ss");
		return dateFormat.format(new Date())
				+ "\n=============================";
	}
}
