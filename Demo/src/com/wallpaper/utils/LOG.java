package com.wallpaper.utils;

import android.util.Log;

public class LOG {
	private static final String PREFIX_FORMAT = "<%s>%s";
	public static int mLogLevel = Log.DEBUG;
	
	public static final String TAG_LOADING_URL = "LOADING URL";

	public static void level(int level) {
		mLogLevel = level;
	}
	
	public static void d(Object o, String msg) {
		if (mLogLevel > Log.DEBUG) return;
		String name = o.getClass().getSimpleName();
		Log.d(name, String.format(PREFIX_FORMAT, name, msg));
	}

	public static void d(String tag, String msg) {
		if (mLogLevel > Log.DEBUG) return;
		Log.d(tag, String.format(PREFIX_FORMAT, tag, msg));
	}

	public static void i(Object o, String msg) {
		if (mLogLevel > Log.INFO) return;
		String name = o.getClass().getSimpleName();
		Log.i(name, String.format(PREFIX_FORMAT, name, msg));
	}

	public static void i(String tag, String msg) {
		if (mLogLevel > Log.INFO) return;
		Log.i(tag, String.format(PREFIX_FORMAT, tag, msg));
	}

	public static void w(Object o, String msg) {
		if (mLogLevel > Log.WARN) return;
		String name = o.getClass().getSimpleName();
		Log.w(name, String.format(PREFIX_FORMAT, name, msg));
	}

	public static void w(String tag, String msg) {
		if (mLogLevel > Log.WARN) return;
		Log.w(tag, String.format(PREFIX_FORMAT, tag, msg));
	}

	public static void e(Object o, String msg, Throwable ex) {
		if (mLogLevel > Log.ERROR) return;
		String name = o.getClass().getSimpleName();
		if(msg != null) Log.e(name, String.format(PREFIX_FORMAT, name, msg));
		if(ex != null) ex.printStackTrace();
	}

	public static void e(String tag, String msg, Throwable ex) {
		if (mLogLevel > Log.ERROR) return;
		if(msg != null) Log.e(tag, String.format(PREFIX_FORMAT, tag, msg));
		if(ex != null) ex.printStackTrace();
	}
	
	public static void e(Object o, String msg) {
		e(o, msg, null);
	}

	public static void e(String tag, String msg) {
		e(tag, msg, null);
	}
	
	public static void e(Object o, Throwable ex) {
		e(o, null, ex);
	}

	public static void e(String tag, Throwable ex) {
		e(tag, null, ex);
	}
}
