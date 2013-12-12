package cayte.check.view;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import cayte.check.xiami.R;

public class Item {
	private Context con;
	private View item;
	private TextView key, value, tip;
	public boolean isClickable = true;

	public Item(View item) {
		this.item = item;
		this.con = item.getContext();
		key = (TextView) item.findViewById(R.id.key);
		value = (TextView) item.findViewById(R.id.value);
		tip = (TextView) item.findViewById(R.id.tip);
	}
	
	public void clear() {
		key.setText(R.string._null);
		value.setText(R.string._null);
		tip.setText(R.string._null);
	}

	public View getItem() {
		return item;
	}

	public TextView getKey() {
		return key;
	}

	public TextView getValue() {
		return value;
	}

	public TextView getTip() {
		return tip;
	}

	public void setKey(int id) {
		key.setText(id);
	}

	public void setVaule(int id) {
		value.setText(id);
	}

	public void setVaule(String text) {
		value.setText(text);
	}

	public void changeTip(final int id) {
		changeTip(con.getString(id));
	}

	public void changeTip(final String text) {
		isClickable = false;
		tip.clearAnimation();
		Animation anim1 = AnimationUtils
				.loadAnimation(con, R.anim.item_tip_out);
		anim1.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				tip.setText(text);
				Animation anim2 = AnimationUtils.loadAnimation(con,
						R.anim.item_tip_in);
				anim2.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						isClickable = true;
						tip.clearAnimation();
					}
				});
				tip.startAnimation(anim2);
			}
		});
		tip.startAnimation(anim1);
	}

	public void changeVaule(final int id) {
		changeVaule(con.getString(id));
	}

	public void changeVaule(final String text) {
		isClickable = false;
		value.clearAnimation();
		Animation anim1 = AnimationUtils.loadAnimation(con,
				R.anim.item_value_out);
		anim1.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				value.setText(text);
				Animation anim2 = AnimationUtils.loadAnimation(con,
						R.anim.item_value_in);
				anim2.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						isClickable = true;
						value.clearAnimation();
					}
				});
				value.startAnimation(anim2);
			}
		});
		value.startAnimation(anim1);
	}

	public void setOnClickListener(View.OnClickListener listener) {
		item.setOnClickListener(listener);
	}

}
