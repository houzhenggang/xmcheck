package cayte.check.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ProgressView {
	private static final String TAG = "ProgressView";
	private Context con;
	private ViewGroup view;
	private ImageView progress;
	private float scale;
	private int paddingBottom = 0;

	private int backColor, cursorColor;

	public static final int BOTH_DIRECTION = 0;
	public static final int SINGLE_DIRECTION = 1;
	private int style = BOTH_DIRECTION;
	private int duration = 800;

	public ProgressView(Activity act, int backColor, int cursorColor) {
		// TODO Auto-generated constructor stub
		DisplayMetrics dm = new DisplayMetrics();
		act.getWindowManager().getDefaultDisplay().getMetrics(dm);
		this.scale = dm.density;
		this.con = act.getApplicationContext();
		this.backColor = backColor;
		this.cursorColor = cursorColor;
		act = null;
	}

	public void setStyle(int style) {
		this.style = style;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	private int dip2px(float dipValue) {
		return (int) (dipValue * scale + 0.5f);
	}

	private void startCursorAnim(float start, float end) {
		Animation animation = new TranslateAnimation(start, end, 0, 0);
		switch (style) {
		default:
		case BOTH_DIRECTION:
			animation.setInterpolator(AnimationUtils.loadInterpolator(con,
					android.R.anim.anticipate_overshoot_interpolator));
			animation.setRepeatMode(Animation.REVERSE);
			break;
		case SINGLE_DIRECTION:
			animation.setInterpolator(AnimationUtils.loadInterpolator(con,
					android.R.anim.accelerate_decelerate_interpolator));
			animation.setRepeatMode(Animation.RESTART);
			break;
		}
		animation.setRepeatCount(Animation.INFINITE);
		animation.setDuration(duration);
		progress.startAnimation(animation);
	}

	private void showProgress(ViewGroup root, LayoutParams lp) {
		// TODO Auto-generated method stub
		view = new FrameLayout(con);
		view.setBackgroundResource(backColor);
		progress = new ImageView(con);
		progress.setTag(TAG);

		int w = lp.width;
		int h = lp.height;
		int drawWidth = w / 10;

		progress.setLayoutParams(new LayoutParams(drawWidth, h));

		Bitmap bitmap = Bitmap
				.createBitmap(drawWidth, h, Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(con.getResources().getColor(cursorColor));
		progress.setImageBitmap(bitmap);

		view.addView(progress);
		root.addView(view, lp);
		root.invalidate();

		switch (style) {
		default:
		case BOTH_DIRECTION:
			startCursorAnim(drawWidth, w - 2 * drawWidth);
			break;
		case SINGLE_DIRECTION:
			startCursorAnim(-drawWidth, w);
			break;
		}
	}

	public void show(ViewGroup root, float heightDip) {
		// TODO Auto-generated method stub
		int height = dip2px(heightDip);
		paddingBottom = root.getPaddingBottom();
		LayoutParams lp = new LayoutParams(root.getMeasuredWidth(), height);
		showProgress(root, lp);
	}

	public void show(LinearLayout root, float heightDip) {
		// TODO Auto-generated method stub
		int height = dip2px(heightDip);
		paddingBottom = root.getPaddingBottom();
		root.setPadding(root.getPaddingLeft(), root.getPaddingTop(),
				root.getPaddingRight(),
				Math.max(root.getPaddingBottom() - height, 0));
		LayoutParams lp = new LayoutParams(root.getMeasuredWidth(), height);
		showProgress(root, lp);
	}

	public void dismiss(ViewGroup root) {
		progress.clearAnimation();
		progress.setImageBitmap(null);
		root.removeView(view);
		root.setPadding(root.getPaddingLeft(), root.getPaddingTop(),
				root.getPaddingRight(), paddingBottom);
		paddingBottom = 0;
		root.invalidate();
		view.removeAllViews();
		progress = null;
		view = null;
	}
}
