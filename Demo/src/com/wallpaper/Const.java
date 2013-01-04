package com.wallpaper;

public class Const {
	public static final class URL{
	}
	
	public static final class FORMAT{
		/**
		 * tag,startid,width,height,reqno,sesstime
		 */
		public static final String CLASSES_ARGUMENT = "http://app.image.baidu.com/app?tag=%s&reqtype=latest&startid=%s&width=%s&height=%s&reqno=%s&netenv=wifi&sesstime=%s&appname=wallpaper&color=0&func=get&ie=utf-8";
		
		/**
		 * width,height,sec,src
		 */
		public static final String THUMB_ARGUMENT = "http://app.image.baidu.com/timg?list&appname=wallpaper&channelid=1426h&size=f%s_%s&quality=60&sec=%s&di=7EA1E08E71FF1A1FA058CDF57BBCFB65&src=%s";
	}
	
	public static final class TAGS{
		public static final String TAG_CALSS_GRIL = "1";
		public static final String TAG_CALSS_STAR = "3";
	}
}
