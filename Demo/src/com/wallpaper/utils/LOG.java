package com.wallpaper.utils;

import android.util.Log;

public class LOG {
	private static final String PREFIX_FORMAT = "<%s>%s";
	
	public static final boolean isLoggindAble = true;
	
	public static void i(Object o, String msg){
		String name = o.getClass().getSimpleName();
		Log.i(name, String.format(PREFIX_FORMAT, name, msg));
	}
	
	public static void i(String tag, String msg){
		Log.i(tag, String.format(PREFIX_FORMAT, tag, msg));
	}
}
