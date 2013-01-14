package com.wallpaper.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

public class MyHttpImageDownloader extends MyImageDownloader {
	private final static int TIMEOUT_SOCKET = 20000;

	private final HttpClient httpClient;

	public MyHttpImageDownloader() {
		this.httpClient = new HttpClient();
	}

	private GetMethod getHttpGet(String url, String cookie, String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// 设置 请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_SOCKET);
		httpGet.setRequestHeader("Connection", "Keep-Alive");
		httpGet.setRequestHeader("Cookie", cookie);
		httpGet.setRequestHeader("User-Agent", userAgent);
		httpGet.setRequestHeader("Referer", "http://app.image.baidu.com");
		return httpGet;
	}

	@Override
	protected InputStream getStreamFromNetwork(URI imageUri) throws IOException {
		GetMethod httpGet = null;
		httpGet = getHttpGet(imageUri.toURL().toString(), null, null);
		this.httpClient.executeMethod(httpGet);
		LOG.i(this, String.format("loading image %s", imageUri.toURL().toString()));
		InputStream is = new BufferedInputStream(httpGet.getResponseBodyAsStream());
		//httpGet.releaseConnection();
		return is;
	}

	public String getStringFromNetwork(URI imageUri) throws IOException {
		InputStream is = getStreamFromNetwork(imageUri);
		return Utils.Stream.convertStreamToString(is);
	}
}
