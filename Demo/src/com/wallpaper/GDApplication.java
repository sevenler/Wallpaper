package com.wallpaper;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class GDApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.threadPoolSize(5)
			.threadPriority(Thread.NORM_PRIORITY - 3)
			.memoryCache(new FIFOLimitedMemoryCache(15 * 1024 * 1024)) // 15 Mb
			.denyCacheImageMultipleSizesInMemory()
			.discCacheExtraOptions(displayMetrics.widthPixels, displayMetrics.heightPixels, Bitmap.CompressFormat.JPEG, 100)
			.discCacheFileNameGenerator(new Md5FileNameGenerator())
			.enableLogging()
			.build();
		ImageLoader.getInstance().init(config);
	}
}