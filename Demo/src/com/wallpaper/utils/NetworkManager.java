package com.wallpaper.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.TextUtils;

public class NetworkManager {
	/**
	 * 网络不可用
	 */
	public static final String NETWORK_TYPE_DISABLED = "DISABLED";
	/**
	 * wifi网络
	 */
	public static final String NETWORK_TYPE_WIFI = "WIFI";
	/**
	 * 电信,移动,联通,等mobile网络
	 */
	public static final String NETWORK_TYPE_MOBILE = "MOBILE";
	/**
	 * 移动wap
	 */
	public static final String NETWORK_TYPE_CMWAP = "CMWAP";
	/**
	 * 联通wap
	 */
	public static final String NETWORK_TYPE_UNIWAP = "UNIWAP";
	/**
	 * 电信wap
	 */
	public static final String NETWORK_TYPE_CTWAP = "CTWAP";
	/**
	 * 其它未知网络
	 */
	public static final String NETWORK_TYPE_OTHER = "OTHR";
	/**
	 * 2G/3G 网络
	 */
	public static final String GPRS = "GPRS";
	// 获取当前网络类型uri
	private static Uri URI_PREFERAPN = Uri.parse("content://telephony/carriers/preferapn");
	private static final String TAG = "NetworkManager";

	public static final int BUFFER_SIZE = 8192;
	public static final int SO_TIMEOUT = 15000 * 2;
	public static final int CONNECT_TIMEOUT = 10000 * 2;

	/**
	 * 网络类型： 0.无网络 1.wifi 2.net网络 3.移动wap 4.联通wap 4.电信wap 5.未知网络
	 **/
	public static String getNetworkType(Context context) {
		try {
			ConnectivityManager connect = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo actNetInfo = connect.getActiveNetworkInfo();
			if (actNetInfo == null) {
				return NETWORK_TYPE_DISABLED;
			}
			if (!actNetInfo.isAvailable()) {
				return NETWORK_TYPE_DISABLED;
			}
			int netType = actNetInfo.getType();
			if (netType == ConnectivityManager.TYPE_WIFI) {
				return NETWORK_TYPE_WIFI;
			}
			if (netType != ConnectivityManager.TYPE_MOBILE) {
				return NETWORK_TYPE_OTHER;
			}
			Cursor cursor = context.getContentResolver().query(URI_PREFERAPN, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				String user = cursor.getString(cursor.getColumnIndex("user"));
				if (!TextUtils.isEmpty(user)) {
					if (user.startsWith("ctwap")) {
						return NETWORK_TYPE_CTWAP;
					}
				}
			}
			cursor.close();
			String extrainfo = actNetInfo.getExtraInfo();
			if (extrainfo != null) {
				extrainfo = extrainfo.toLowerCase();
				// 3gnet/3gwap/uninet/uniwap/cmnet/cmwap/ctnet/ctwap
				if (extrainfo.equals("cmwap")) {
					return NETWORK_TYPE_CMWAP;
				}
				if (extrainfo.equals("uniwap")) {
					return NETWORK_TYPE_UNIWAP;
				}
			}
			return NETWORK_TYPE_MOBILE;
		} catch (Exception e) {
			LOG.e(TAG, e);
			return NETWORK_TYPE_OTHER;
		}
	}

	/**
	 * 是否有可用网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		return isWifiConnected(context) || isMobileConnected(context);
	}

	/**
	 * WIFI是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifi != null && wifi.isConnected()) {
				return true;
			}
		} catch (Exception e) {
			LOG.e(TAG, e);
		}
		return false;
	}

	/**
	 * 2G/3G是否连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobile != null && mobile.isConnected()) {
				return true;
			}
		} catch (Exception e) {
			LOG.e(TAG, e);
		}
		return false;
	}

	public static String getLocalIpAddress(Context context) {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			LOG.e(TAG, ex);
		}
		return null;
	}
}
