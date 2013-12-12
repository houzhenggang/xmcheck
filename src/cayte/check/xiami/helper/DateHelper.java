package cayte.check.xiami.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import cayte.check.handler.D;
import cayte.check.xiami.R;

@SuppressLint("SimpleDateFormat")
public class DateHelper {

	public static SimpleDateFormat dateSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdateSdf = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat dtimeSdf = new SimpleDateFormat(
			"MM-dd HH:mm:ss");
	public static SimpleDateFormat timeSdf = new SimpleDateFormat("HH:mm:ss");

	public static String getNowDateString() {
		// TODO Auto-generated method stub
		return getDateString(new Date());
	}

	public static String getDateString(Date date) {
		return dateSdf.format(date);
	}

	public static String longToString(long date) {
		if (date <= 0)
			return "";
		return dateSdf.format(new Date(date));
	}

	public static String getFriendlyDate(Context con, String date) {
		// TODO Auto-generated method stub
		if (D.isNull(date))
			return "";
		try {
			return getFriendlyDate(con, dateSdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static String getFriendlyDate(Context con, long date) {
		// TODO Auto-generated method stub
		if (date <= 0)
			return "";
		return getFriendlyDate(con, new Date(date));
	}

	@SuppressWarnings("deprecation")
	public static String getFriendlyDate(Context con, Date date) {
		// TODO Auto-generated method stub
		if (date == null)
			return "";
		Date now = new Date();
		if (date.getYear() == now.getYear()) {
			if (date.getMonth() == now.getMonth()) {
				if (date.getDate() == now.getDate())
					return con.getString(R.string.today) + " "
							+ timeSdf.format(date);
				else if (isSameDay(now, new Date(date.getTime() + 24l * 60l
						* 60l * 1000l)))
					return con.getString(R.string.yesterday) + " "
							+ timeSdf.format(date);
			} else if (isSameDay(now, new Date(date.getTime() + 24l * 60l * 60l
					* 1000l)))
				return con.getString(R.string.yesterday) + " "
						+ timeSdf.format(date);
			return dtimeSdf.format(date);
		} else if (isSameDay(now, new Date(date.getTime() + 24l * 60l * 60l
				* 1000l)))
			return con.getString(R.string.yesterday) + " "
					+ timeSdf.format(date);
		return sdateSdf.format(date);
	}

	@SuppressWarnings("deprecation")
	private static boolean isSameDay(Date date1, Date date2) {
		if (date1.getYear() == date2.getYear())
			if (date1.getMonth() == date2.getMonth())
				if (date1.getDate() == date2.getDate())
					return true;
		return false;
	}

}
