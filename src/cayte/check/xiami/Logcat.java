package cayte.check.xiami;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Environment;

public class Logcat {
	/**
	 * 打开日志文件并写入日志
	 * 
	 * @return
	 * **/
	@SuppressLint("SimpleDateFormat")
	public static void log(String text) {// 新建或打开日志文件
		Date nowtime = new Date();
		String needWriteMessage = new SimpleDateFormat("yyyy-MM-dd HH:mm")
				.format(nowtime) + ">>>>>" + text;
		File file = new File(Environment.getExternalStorageDirectory(),
				"check_log.txt");
		try {
			FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(needWriteMessage);
			bufWriter.newLine();
			bufWriter.close();
			filerWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
