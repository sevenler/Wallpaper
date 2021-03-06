package com.wallpaper.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import com.nostra13.universalimageloader.core.download.ImageDownloader;

public class MyImageDownloader extends ImageDownloader {

	@Override
	protected InputStream getStreamFromNetwork(URI imageUri) throws IOException {
		return super.getStream(imageUri);
	}

	public String getStringFromNetwork(URI imageUri) throws IOException {
		InputStream is = getStreamFromNetwork(imageUri);
		return Utils.Stream.convertStreamToString(is);
	}

}
