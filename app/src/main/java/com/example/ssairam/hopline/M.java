package com.example.ssairam.hopline;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

/**
 * Logs with extra features.
 */
public class M {
	private static final boolean showLog = true;
	public static void log(String TAG, String text) {
		if (showLog) Log.d(TAG, text);

		File logFile = new File("sdcard/log.txt");
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
			buf.append(mydate + "     " + TAG + " --->  " + text);
			Log.d(TAG, text);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
