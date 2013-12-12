package cayte.check.xiami.helper;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

public class PaperHelper {

	public static Bitmap handlerWallpaper(Activity act, Drawable drawable) {
		return handlerWallpaper(act, ((BitmapDrawable) drawable).getBitmap());
	}

	public static Bitmap handlerWallpaper(Activity act, Bitmap bitmap) {
		// TODO Auto-generated method stub
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int SCREEN_WIDTH = dm.widthPixels;
		int SCREEN_HEIGHT = dm.heightPixels;
		act = null;

		Bitmap res = null;

		int l, t, w, h;

		if (bitmap.getWidth() >= SCREEN_WIDTH) {
			l = (bitmap.getWidth() - SCREEN_WIDTH) / 2;
			w = SCREEN_WIDTH;
		} else {
			l = 0;
			w = bitmap.getWidth();
		}

		if (bitmap.getHeight() >= SCREEN_HEIGHT) {
			t = (bitmap.getHeight() - SCREEN_HEIGHT) / 2;
			h = SCREEN_HEIGHT;
		} else {
			t = 0;
			h = bitmap.getHeight();
		}

		res = Bitmap.createBitmap(bitmap, l, t, w, h);

		return res;
	}

	public static Bitmap getPaper(Activity act) {
		String path = act.getFilesDir().getAbsoluteFile() + File.separator
				+ "paper.png";
		Bitmap bitmap = null;
		try {
			File file = new File(path);
			InputStream is = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (bitmap == null) {
			DisplayMetrics dm = new DisplayMetrics();
			act.getWindowManager().getDefaultDisplay().getMetrics(dm);
			int SCREEN_WIDTH = dm.widthPixels;
			int SCREEN_HEIGHT = dm.heightPixels;
			bitmap = Bitmap.createBitmap(SCREEN_WIDTH, SCREEN_HEIGHT,
					Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(bitmap);
			canvas.drawARGB(255, 135, 135, 135);
		}
		act = null;
		return bitmap;
	}

	public static void setPaper(Activity act, Bitmap bitmap) {
		String path = act.getFilesDir().getAbsoluteFile() + File.separator
				+ "paper.png";
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
					act = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
