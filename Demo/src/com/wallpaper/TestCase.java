package com.wallpaper;

import java.io.IOException;
import java.net.URI;

import android.test.AndroidTestCase;
import android.util.Log;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.MyHashCodeFileNameGenerator;
import com.wallpaper.utils.MyHttpImageDownloader;
import com.wallpaper.utils.MyURLConnectionImageDownloader;

public class TestCase extends AndroidTestCase {
	public void test(int z) {
		LOG.level(Log.DEBUG);
		String url = "http://t2.baidu.com/it/u=3420700231,1907296026&fm=17";
		URI uri = URI.create(url);
		MyHttpImageDownloader loader = new MyHttpImageDownloader();
		try {
			for (int i = 0; i < 100; i++) {
				LOG.i(this, i + " result:" + loader.getStringFromNetwork(uri));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		MyURLConnectionImageDownloader con = new MyURLConnectionImageDownloader();
		try {
			LOG.i(this, " 100 result:" + con.getStringFromNetwork(URI.create(url)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void test() {
		String url = "http://t2.baidu.com/it/u=3420700231,1907296026&fm=17";
		
		MyHashCodeFileNameGenerator generator = new MyHashCodeFileNameGenerator();
		System.out.println("key:" + generator.generate(url, new ImageSize(100,100)));
	}
}
