package cayte.check.xiami.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import cayte.check.handler.AccountInfo;

public class DBHelper {
	protected Context con;
	private SQL sql = null;
	private SQLiteDatabase db = null;
	private Cursor c = null;

	public DBHelper(Context con) {
		// TODO Auto-generated constructor stub
		this.con = con;
	}

	protected void open() {
		open(false);
	}

	protected void open(boolean isOnlyRead) {
		sql = new SQL(con.getApplicationContext());
		if (isOnlyRead)
			db = sql.getReadableDatabase();
		else
			db = sql.getWritableDatabase();
	}

	protected void close() {
		if (c != null)
			c.close();
		c = null;
		if (db != null)
			db.close();
		db = null;
		if (sql != null)
			sql.close();
		sql = null;
	}

	public List<AccountInfo> query() {
		// TODO Auto-generated method stub
		List<AccountInfo> accs = new ArrayList<AccountInfo>();
		try {
			open();
			c = db.query(SQL.TABLE, null, null, null, null, null, SQL.ID
					+ " desc", null);
			c.moveToFirst();
			AccountInfo acc = null;
			for (int i = 0; i < c.getCount(); i++) {
				acc = new AccountInfo();
				acc.name = c.getString(c.getColumnIndex(SQL.NAME));
				acc.pass = c.getString(c.getColumnIndex(SQL.PASS));
				acc.lastCheck = c.getLong(c.getColumnIndex(SQL.LAST));
				acc.day = c.getInt(c.getColumnIndex(SQL.DAY));
				acc.integral = c.getString(c.getColumnIndex(SQL.INTEGRAL));
				acc.level = c.getString(c.getColumnIndex(SQL.LEVEL));
				acc.isAuto = c.getInt(c.getColumnIndex(SQL.ISAUTO)) == 1 ? true
						: false;
				accs.add(0, acc);
				acc = null;
				c.moveToNext();
			}
			return accs;
		} catch (Exception e) {
			return accs;
		} finally {
			close();
		}
	}

	public AccountInfo[] queryArray() {
		AccountInfo[] array = new AccountInfo[] {};
		return query().toArray(array);
	}

	public void updata(ArrayList<AccountInfo> accs) {
		// TODO Auto-generated method stub
		for (AccountInfo acc : accs) {
			updataOne(acc);
		}
	}

	public void updataOne(AccountInfo acc) {
		// TODO Auto-generated method stub
		try {
			open();
			ContentValues tcv = null;
			String whereClause = SQL.NAME + " = ?";
			String[] whereArgs = null;
			whereArgs = new String[] { acc.name };
			tcv = new ContentValues();
			tcv.put(SQL.PASS, acc.pass);
			tcv.put(SQL.LAST, acc.lastCheck);
			tcv.put(SQL.DAY, acc.day);
			tcv.put(SQL.INTEGRAL, acc.integral);
			tcv.put(SQL.LEVEL, acc.level);
			tcv.put(SQL.ISAUTO, acc.isAuto ? 1 : 0);
			db.update(SQL.TABLE, tcv, whereClause, whereArgs);
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public boolean hasName(String name) {
		try {
			open();
			c = db.query(SQL.TABLE, null, SQL.NAME + "=?",
					new String[] { name }, null, null, null, null);
			return c.moveToFirst();
		} catch (Exception e) {
		} finally {
			close();
		}
		return false;
	}

	public boolean insert(AccountInfo acc) {
		// TODO Auto-generated method stub
		try {
			open();
			ContentValues tcv = new ContentValues();
			tcv.put(SQL.NAME, acc.name);
			tcv.put(SQL.PASS, acc.pass);
			tcv.put(SQL.LAST, acc.lastCheck);
			tcv.put(SQL.DAY, acc.day);
			tcv.put(SQL.INTEGRAL, acc.integral);
			tcv.put(SQL.LEVEL, acc.level);
			tcv.put(SQL.ISAUTO, acc.isAuto ? 1 : 0);
			long id = db.insert(SQL.TABLE, null, tcv);
			if (id == -1)
				return false;
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			close();
		}
	}

	public void delete(String name) {
		// TODO Auto-generated method stub
		try {
			open();
			db.delete(SQL.TABLE, SQL.NAME + " = ?", new String[] { name });
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public boolean isHasAutoCheck() {
		try {
			open();
			String[] args = { "1" };
			c = db.query(SQL.TABLE, null, SQL.ISAUTO + "=?", args, null, null,
					null, null);
			return c.moveToFirst();
		} catch (Exception e) {
		} finally {
			close();
		}
		return false;
	}

	public void setAuto(String name, boolean isAuto) {
		try {
			open();
			ContentValues tcv = new ContentValues();
			tcv.put(SQL.ISAUTO, isAuto ? 1 : 0);
			String whereClause = SQL.NAME + " = ?";
			String[] whereArgs = null;
			whereArgs = new String[] { name };
			db.update(SQL.TABLE, tcv, whereClause, whereArgs);
		} catch (Exception e) {
		} finally {
			close();
		}
	}

	public boolean isAuto(String name) {
		// TODO Auto-generated method stub
		try {
			open();
			c = db.query(SQL.TABLE, null, SQL.NAME + "=?",
					new String[] { name }, null, null, null, null);
			if (c.moveToFirst()) {
				return c.getInt(c.getColumnIndex(SQL.ISAUTO)) == 1 ? true
						: false;
			}
		} catch (Exception e) {
		} finally {
			close();
		}
		return false;
	}

	public void punish(String name) {
		try {
			open();
			ContentValues tcv = new ContentValues();
			tcv.put(SQL.ISAUTO, 0);
			String whereClause = SQL.NAME + " = ?";
			String[] whereArgs = null;
			whereArgs = new String[] { name };
			db.update(SQL.TABLE, tcv, whereClause, whereArgs);
		} catch (Exception e) {
		} finally {
			close();
		}
	}

}
