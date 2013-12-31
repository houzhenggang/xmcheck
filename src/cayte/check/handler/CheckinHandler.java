package cayte.check.handler;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import cayte.check.xiami.R;
import cayte.check.xiami.helper.DateHelper;

public class CheckinHandler {
	private DefaultHttpClient client = null;

	public static final int UNKNOWN_FAIL = 0;
	public static final int NET_FAIL = -1;
	public static final int SERVER_FAIL = -2;
	public static final int LOGIN_FAIL = -3;
	public static final int LOGIN_VALIDATE_FAIL = -4;
	public static final int CHECK_FAIL = -5;
	public static final int IS_CHECKED = 2;
	public static final int SUCCESS = 1;

	private Context context = null;

	private String T_UNKNOWN_FAIL, T_NET_FAIL, T_SERVER_FAIL, T_LOGIN_FAIL,
			T_LOGIN_VALIDATE_FAIL, T_CHECK_FAIL;

	private LogCallback logCb = null;

	public interface LogCallback {
		void Callback(String log);
	}

	public void setLogCb(LogCallback logCb) {
		this.logCb = logCb;
	}

	public void log(String log) {
		// TODO Auto-generated method stub
		if (logCb != null)
			logCb.Callback(log);
	}

	public CheckinHandler(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		T_UNKNOWN_FAIL = context.getString(R.string.tUnknownFail);
		T_NET_FAIL = context.getString(R.string.tNetFail);
		T_SERVER_FAIL = context.getString(R.string.tServerFail);
		T_LOGIN_FAIL = context.getString(R.string.tLoginFail);
		T_CHECK_FAIL = context.getString(R.string.tCheckFail);
		T_LOGIN_VALIDATE_FAIL = context.getString(R.string.tLoginValidateFail);
	}

	private void initClient() {
		// TODO Auto-generated method stub
		client = new DefaultHttpClient();
		int TIMEOUT = 30 * 1000;
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, TIMEOUT);
		params.setParameter("User-Agent",
				"Mozilla/5.0 (X11; Linux x86_64; rv:11.0) Gecko/20100101 Firefox/11.0");
		params.setParameter("http.useragent",
				"Mozilla/5.0 (X11; Linux x86_64; rv:11.0) Gecko/20100101 Firefox/11.0");
		params.setParameter("Accept-Language", "zh-CN,zh;q=0.8");
		params.setParameter("Accept-Encoding", "gzip,deflate,sdch");
		params.setParameter("Accept",
				"text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		client.setParams(params);
		BasicCookieStore localBasicCookieStore = new BasicCookieStore();
		client.setCookieStore(localBasicCookieStore);
	}

	private void shutdown() {
		// TODO Auto-generated method stub
		if (client != null)
			client.getConnectionManager().shutdown();
		client = null;
	}

	public AccountInfo check(AccountInfo acc) {
		// TODO Auto-generated method stub
		if (!NetCheck.isConnected(context)) {
			acc.state = NET_FAIL;
			acc.tip = state2Tip(acc.state, "");
			log(acc.tip);
			return acc;
		}
		log(context.getString(R.string.logStart));
		initClient();
		D.e(">>>>check start , name : " + acc.name);
		log(context.getString(R.string.logName) + acc.name);
		log(context.getString(R.string.logLogining));
		acc = loginIn(acc);
		if (acc.state == SUCCESS) {// 登录成功,未签到
			log(context.getString(R.string.logLoginSuccess));
			// 签到
			log(context.getString(R.string.logChecking));
			acc = checkIn(acc);
			if (acc.state == SUCCESS) {// 签到成功
				acc.lastCheck = System.currentTimeMillis();
				log(context.getString(R.string.logCheckinSuccess));
				log(context.getString(R.string.logTime)
						+ DateHelper.longToString(acc.lastCheck));
			} else {// 签到失败
				acc.tip = state2Tip(acc.state, "");
				// log(context.getString(R.string.logCheckinFail));
				log(acc.tip);
			}
		} else if (acc.state == IS_CHECKED) {// 登录成功,已签到
			log(context.getString(R.string.logLoginSuccess));
			log(context.getString(R.string.logCheckined));
			acc.lastCheck = System.currentTimeMillis();
			log(context.getString(R.string.logCheckinSuccess));
			log(context.getString(R.string.logTime)
					+ DateHelper.longToString(acc.lastCheck));
		} else {// 未知错误
			acc.tip = state2Tip(acc.state, "");
			// log(context.getString(R.string.logCheckinFail));
			log(acc.tip);
		}

		shutdown();
		D.e(">>>>check end , state : " + acc.state + " , tip : " + acc.tip);
		log(context.getString(R.string.logEnd));
		return acc;
	}

	private String state2Tip(int state, String defaultString) {
		// TODO Auto-generated method stub
		String tip = "";
		switch (state) {
		case UNKNOWN_FAIL:
			tip = T_UNKNOWN_FAIL;
			break;
		case NET_FAIL:
			tip = T_NET_FAIL;
			break;
		case SERVER_FAIL:
			tip = T_SERVER_FAIL;
			break;
		case LOGIN_FAIL:
			tip = T_LOGIN_FAIL;
			break;
		case LOGIN_VALIDATE_FAIL:
			tip = T_LOGIN_VALIDATE_FAIL;
			break;
		case CHECK_FAIL:
			tip = T_CHECK_FAIL;
			break;
		default:
			tip = defaultString;
			break;
		}
		return tip;
	}

	private AccountInfo loginIn(AccountInfo acc) {
		// TODO Auto-generated method stub
		if (client == null) {
			acc.state = UNKNOWN_FAIL;
			return acc;
		}
		ArrayList<BasicNameValuePair> valueList = new ArrayList<BasicNameValuePair>();
		BasicNameValuePair value = null;
		value = new BasicNameValuePair("email", acc.name);
		valueList.add(value);
		value = new BasicNameValuePair("password", acc.pass);
		valueList.add(value);
		value = new BasicNameValuePair("LoginButton", "登录");
		valueList.add(value);

		try {
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
					valueList, "UTF-8");
			HttpPost post = new HttpPost("http://www.xiami.com/web/login");
			post.setHeader("Referer", "http://www.xiami.com/web/login");
			post.setEntity(urlEncodedFormEntity);
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 200) {
				String res = EntityUtils
						.toString(response.getEntity(), "UTF-8");
				if (res.contains("会员登录")) {
					acc.state = LOGIN_FAIL;
				} else if (res.contains("请输入验证码")) {
					acc.state = LOGIN_VALIDATE_FAIL;
				} else if (res.contains("快捷操作")) {
					acc = isCheck(acc);
					if (acc.day > 0)
						acc.state = IS_CHECKED;
					else
						acc.state = SUCCESS;
				} else {
					acc.state = SERVER_FAIL;
				}
			} else {
				acc.state = SERVER_FAIL;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return acc;
	}

	private AccountInfo checkIn(AccountInfo acc) {
		// TODO Auto-generated method stub
		if (client == null) {
			acc.state = UNKNOWN_FAIL;
			return acc;
		}
		ArrayList<BasicNameValuePair> valueList = new ArrayList<BasicNameValuePair>();
		try {
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
					valueList, "UTF-8");
			HttpPost post = new HttpPost("http://www.xiami.com/task/signin");
			post.setHeader("Referer", "http://www.xiami.com/");
			post.setEntity(urlEncodedFormEntity);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				acc = isCheck(acc);
				if (acc.day > 0)
					acc.state = SUCCESS;
				else
					acc.state = CHECK_FAIL;
			} else {
				acc.state = SERVER_FAIL;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return acc;
	}

	private AccountInfo isCheck(AccountInfo acc) {
		// TODO Auto-generated method stub
		try {
			HttpGet get = new HttpGet("http://www.xiami.com/web?1");
			HttpResponse resp = client.execute(get);
			if (resp.getStatusLine().getStatusCode() == 200) {
				String res = EntityUtils.toString(resp.getEntity(), "UTF-8");
				printLongString(res);
				acc = isCheck(acc, res);
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return acc;
	}

	private AccountInfo isCheck(AccountInfo acc, String html) {
		// TODO Auto-generated method stub
		int days = 0;
		//
		// String integral = "";
		// String level = "";
		// String dayNum = "";
		// /** get check days */
		// String str = Regular.get(html, "sidebar", "checkin_popup");
		// integral = Regular.get(Regular.get(str, "help9_2", "</a>"),
		// "<strong>",
		// "</strong>");
		// level = Regular.get(str, "level_popup\">", "<p>");
		// dayNum = Regular.get(str, "done\">", "</b>");
		// if (dayNum.contains(V_DAY))
		// days = Integer.parseInt(dayNum.replace(V_DAY, "").trim());
		// acc.integral = integral;
		// acc.level = level;
		// acc.day = days;
		// acc.tip = integral + "-" + level + "-" + dayNum;
		//
		if (html.contains("每日签到")) {
			acc.day = 0;
		} else {
			String dayNum = Regular.get(html, "已连续签到", "天");
			days = Integer.parseInt(dayNum.trim());
			acc.day = days;
		}
		return acc;
	}

	public void printLongString(String str) {
		int tag = 100;
		if (str.length() > tag) {
			for (int i = 0; i < (str.length() / tag); i++) {
				System.out.println(str.substring(i * tag, (i + 1) * tag));
			}
			if (str.length() % 100 > 0) {
				System.out.println(str.substring(str.length() - str.length()
						% tag));
			}
		} else
			System.out.println(str);
	}

}