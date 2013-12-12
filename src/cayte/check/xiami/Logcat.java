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
	 * ����־�ļ���д����־
	 * 
	 * @return
	 * **/
	@SuppressLint("SimpleDateFormat")
	public static void log(String text) {// �½������־�ļ�
		Date nowtime = new Date();
		String needWriteMessage = new SimpleDateFormat("yyyy-MM-dd HH:mm")
				.format(nowtime) + ">>>>>" + text;
		File file = new File(Environment.getExternalStorageDirectory(),
				"check_log.txt");
		try {
			FileWriter filerWriter = new FileWriter(file, true);// ����������������ǲ���Ҫ�����ļ���ԭ�������ݣ������и���
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
