package cayte.check.xiami.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.DisplayMetrics;

import com.umeng.analytics.MobclickAgent;

public class SplashBgHelper {

	private static int WIDTH, HEIGHT;

	private Context con;

	public SplashBgHelper(Activity act) {
		// TODO Auto-generated constructor stub
		this.con = act;
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		WIDTH = dm.widthPixels;
		HEIGHT = dm.heightPixels;
		SharedPreferences spf = act.getApplicationContext()
				.getSharedPreferences("cayte_screen", 4);
		spf.edit().putInt("WIDTH", WIDTH).commit();
		spf.edit().putInt("HEIGHT", HEIGHT).commit();
	}

	public SplashBgHelper(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
		SharedPreferences spf = con.getApplicationContext()
				.getSharedPreferences("cayte_screen", 4);
		WIDTH = spf.getInt("WIDTH", 720);
		HEIGHT = spf.getInt("HEIGHT", 1280);
	}

	public Bitmap getBg() {
		// TODO Auto-generated method stub
		String path = con.getFilesDir().getAbsoluteFile() + File.separator
				+ "splash.png";
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		return bitmap;
	}

	public void checkBg() {
		// TODO Auto-generated method stub
		String verStr = MobclickAgent.getConfigParams(con, "splash_image_ver");
		String url = MobclickAgent.getConfigParams(con, "splash_image_url");

		if (verStr.equals("") || url.equals(""))
			return;

		int ver = 0;
		try {
			ver = Integer.parseInt(verStr);
		} catch (Exception e) {
			// TODO: handle exception
		}

		String path = con.getFilesDir().getAbsoluteFile() + File.separator
				+ "splash.png";
		try {
			if (ver == 0) {
				new File(path).delete();
			} else {
				SharedPreferences spf = con.getApplicationContext()
						.getSharedPreferences("cayte_screen", 4);
				int lastVer = spf.getInt("splash_image_ver", 0);
				if (ver != lastVer) {
					if (downloadNewBg(url))
						spf.edit().putInt("splash_image_ver", ver);
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private boolean downloadNewBg(String url) {
		// TODO Auto-generated method stub
		boolean success = true;

		Bitmap bitmap = getPaperBitmap(url);

		saveImage(bitmap);

		return success;
	}

	private void saveImage(Bitmap bitmap) {
		String path = con.getFilesDir().getAbsoluteFile() + File.separator
				+ "splash.png";
		File file = new File(path);
		if (bitmap == null) {
			file.delete();
		} else {
			BufferedOutputStream bos = null;
			try {
				bos = new BufferedOutputStream(new FileOutputStream(file));
				if (bitmap != null) {
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				try {
					if (bos != null) {
						bos.flush();
						bos.close();
					}
					file = null;
					bitmap = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Bitmap getPaperBitmap(String URL) {
		// TODO Auto-generated method stub
		Bitmap bit = getHttpBitmap(URL);
		int h = bit.getHeight();
		int w = bit.getWidth();

		float B = HEIGHT / (float) WIDTH;
		float b = h / (float) w;

		float scale = 1f;

		if (h >= HEIGHT && w >= WIDTH) {
			if (B > b) {
				// 按高算缩放比
				scale = HEIGHT / (float) h;
				// 缩小
				bit = scale(bit, scale);
				// 截宽
				bit = cut(bit);
			} else {
				// 按宽算缩放比
				scale = WIDTH / (float) w;
				// 缩小
				bit = scale(bit, scale);
				// 截高
				bit = cut(bit);
			}
		} else if (h >= HEIGHT) {
			// 按宽算缩放比
			scale = WIDTH / (float) w;
			// 放大
			bit = scale(bit, scale);
			// 截高
			bit = cut(bit);
		} else if (w >= WIDTH) {
			// 截宽
			bit = cut(bit);
		} else {
			// 按宽算缩放比
			scale = WIDTH / (float) w;
			// 放大
			bit = scale(bit, scale);
			// 截取
			bit = cut(bit);
		}

		if (HEIGHT - bit.getHeight() > 30) {
			// 圆角
			bit = round(bit);
		}

		return bit;
	}

	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myFileURL
					.openConnection();
			conn.setConnectTimeout(6000);
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	private Bitmap scale(Bitmap bitmap, float scale) {
		if (scale == 1f)
			return bitmap;
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	private Bitmap cut(Bitmap bitmap) {
		int h = bitmap.getHeight();
		int w = bitmap.getWidth();

		int x, y, width, height;

		if (h > HEIGHT) {
			y = (h - HEIGHT) / 2;
			height = HEIGHT;
		} else {
			y = 0;
			height = h;
		}

		if (w > WIDTH) {
			x = (w - WIDTH) / 2;
			width = WIDTH;
		} else {
			x = 0;
			width = w;
		}

		Bitmap cutBmp = Bitmap.createBitmap(bitmap, x, y, width, height);

		return cutBmp;
	}

	private Bitmap round(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
					bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);

			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(),
					bitmap.getHeight());
			final RectF rectF = new RectF(rect);
			final float roundPx = 25;

			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(bitmap, rect, rect, paint);

			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}
}
