package com.wallpaper;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.cache.memory.impl.FIFOLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wallpaper.utils.DisplayManager;
import com.wallpaper.utils.MyHashCodeFileNameGenerator;

public class MyApplication extends Application {

	public ImageLoaderConfiguration config;
	@Override
	public void onCreate() {
		super.onCreate();

		DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
		config = new ImageLoaderConfiguration.Builder(getApplicationContext())
			.threadPoolSize(5)
			.threadPriority(Thread.NORM_PRIORITY - 3)
			.memoryCache(new FIFOLimitedMemoryCache(15 * 1024 * 1024))
			.denyCacheImageMultipleSizesInMemory()
			.discCacheExtraOptions(displayMetrics.widthPixels, displayMetrics.heightPixels, Bitmap.CompressFormat.JPEG, 100)
			.discCacheFileNameGenerator(new MyHashCodeFileNameGenerator())
			.memoryCacheKeyGenarator(new MyHashCodeFileNameGenerator())
			.tasksProcessingOrder(QueueProcessingType.FIFO)
			//.imageDownloader(new MyHttpImageDownloader())
			.enableLogging()
			.build();
		ImageLoader.getInstance().init(config);
		
		DisplayManager.getInstance().refresh(getApplicationContext());
	}
}