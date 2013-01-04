package com.wallpaper.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

public class MyHttpClientImageDownloader extends ImageDownloader {

	private HttpClient httpClient;

	public MyHttpClientImageDownloader(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	@Override
	protected InputStream getStreamFromNetwork(URI imageUri) throws IOException {
		HttpGet httpRequest = new HttpGet(imageUri.toString());
		httpRequest.addHeader("Referer", "Referer:http://app.image.baidu.com");
		HttpResponse response = httpClient.execute(httpRequest);
		HttpEntity entity = response.getEntity();
		BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(entity);
		return bufHttpEntity.getContent();
	}
	
	public String getStringFromNetwork(URI imageUri) throws IOException {
		InputStream is = getStreamFromNetwork(imageUri);
		return Utils.Stream.convertStreamToString(is);
	}
}
