package com.wallpaper;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

import android.test.AndroidTestCase;
import android.util.Log;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.wallpaper.model.Covers;
import com.wallpaper.task.CoverListUpdateTask;
import com.wallpaper.task.OnProgressListenner;
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

	public void test() throws InterruptedException {
		String url = "http://t2.baidu.com/it/u=3420700231,1907296026&fm=17";
		
		MyHashCodeFileNameGenerator generator = new MyHashCodeFileNameGenerator();
		System.out.println("key:" + generator.generate(url, new ImageSize(100,100)));
		
		final CountDownLatch latch = new CountDownLatch(1);
		new CoverListUpdateTask().setOnProgressListenner(new OnProgressListenner() {
			@Override
			public void onFinish(Object... result) {
				Covers banners = (Covers)result[0];
				Covers covers = (Covers)result[1];
				System.out.println("" + Arrays.toString(banners.sort()) + "\n" + Arrays.toString(covers.sort()) );
				
				latch.countDown();
			}
		}).start();
		
		latch.await();
	}
}
