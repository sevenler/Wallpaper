package com.wallpaper.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.client.HttpClient;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.wallpaper.Const;

public class Utils {
	public static final class Stream {
		public static String convertStreamToString(InputStream is) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}
	}

	public static final class Http {
		public static HttpClient createHttpClient(Context context) {
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, Const.HTTPS.CONNECT_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, Const.HTTPS.SO_TIMEOUT);
			HttpConnectionParams.setSocketBufferSize(httpParams, Const.HTTPS.BUFFER_SIZE);
			HttpClientParams.setRedirecting(httpParams, true);
			httpParams.setParameter("Connection", "closed");
			HttpClient httpClient = new DefaultHttpClient(httpParams);
			return httpClient;
		}

		public static String generateThumbImageUrl(ImageSize target, String source) {
			return String.format(Const.FORMAT.THUMB_ARGUMENT, target.getWidth(), target.getHeight(), System.currentTimeMillis(), source);
		}
	}

	public static final class Files {
		public static String[] getImagsFormDir(String dir) {
			File file = new File(dir);
			String[] imags = file.list();
			for (int i = 0; i < imags.length; i++) {
				imags[i] = String.format("file://%s/%s", dir, imags[i]);
			}
			return imags;
		}
	}
}
