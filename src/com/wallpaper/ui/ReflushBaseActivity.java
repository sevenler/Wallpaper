package com.wallpaper.ui;

import com.wallpaper.model.Images;

public class ReflushBaseActivity extends BaseActivity {
	protected int mGridColumes = 3;
	protected int mLimitEachPage = 18;
	protected int skip = 0;

	protected Images images = new Images();

	protected static final String MESSAGE_GETED_IMAGES = "get %s images:%s";
	protected static final String MESSAGE_LOAD_IMAGE = "loading image:%s [size:%sx%s]";

	protected void refreshToLoad() {
	}
}
