package cayte.check.handler;

import java.io.Serializable;

public class AccountInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6320000860174872333L;

	public String name = "";
	public String pass = "";
	public int state = 0;
	public long lastCheck = 0;
	public int day = 0;
	public String integral = "";
	public String level = "";
	public String tip = "";
	public boolean isAuto = false;

	private Boolean check = null;

	public void reCheck() {
		check = null;
	}

	public void setCheck(boolean c) {
		check = c;
	}

	public void change() {
		if (check == null)
			check = true;
		else
			check = !check;
	}

	public Boolean isCheck() {
		return check;
	}

}
