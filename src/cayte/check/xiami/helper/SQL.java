package cayte.check.xiami.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQL extends SQLiteOpenHelper {

	private static final String DB_NAME = "ACCOUNT_DB";
	private static final int VERSION = 2;

	public static final String TABLE = "table_account";
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String PASS = "pass";
	public static final String LAST = "last";
	public static final String DAY = "day";
	public static final String INTEGRAL = "integral";
	public static final String LEVEL = "level";
	public static final String ISAUTO = "isAutoCheck";

	public SQL(Context context) {
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (" + ID
				+ " INTEGER PRIMARY KEY," + NAME + " VARCHAR," + PASS
				+ " VARCHAR," + LAST + " INTEGER," + DAY + " INTEGER,"
				+ INTEGRAL + " VARCHAR," + LEVEL + " VARCHAR," + ISAUTO
				+ " INTEGER" + ")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(db);
	}

}