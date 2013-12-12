package cayte.check.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regular {

	private static final int FLAGS = Pattern.COMMENTS | Pattern.DOTALL
			| Pattern.MULTILINE;

	public static String get(String str, String start, String end) {
		try {
			Matcher matcher = null;
			matcher = Pattern.compile(start + "(.*?)" + end + "", FLAGS)
					.matcher(str);
			if (matcher.find()) {
				String res = String.valueOf(matcher.group(1));
				return res.trim();
			}
		} catch (Exception e) {
		}
		return "";
	}
}
