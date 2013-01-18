package com.wallpaper.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.nostra13.universalimageloader.core.download.URLConnectionImageDownloader;

public class MyURLConnectionImageDownloader extends URLConnectionImageDownloader {

	MyHashCodeFileNameGenerator genarator = new MyHashCodeFileNameGenerator();
	@Override
	public InputStream getStreamFromNetwork(URI imageUri) throws IOException {
		return super.getStreamFromNetwork(imageUri);
	}

	public String getStringFromNetwork(URI imageUri) throws IOException {
		InputStream is = getStreamFromNetwork(imageUri);
		return Utils.Stream.convertStreamToString(is);
	}
}
