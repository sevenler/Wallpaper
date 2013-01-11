package com.wallpaper.ui;

import android.app.Activity;
import android.os.Bundle;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.wallpaper.utils.DisplayManager;

public abstract class BaseActivity extends Activity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	protected static final ImageSize DISPLAY_SIZE = new ImageSize(
					DisplayManager.getInstance().getDisplayWidth(), 
					DisplayManager.getInstance().getDisplayHeight());

	@Override
	public void onSaveInstanceState(Bundle outState) {
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
