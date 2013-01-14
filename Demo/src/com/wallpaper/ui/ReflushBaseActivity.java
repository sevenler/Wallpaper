package com.wallpaper.ui;

import java.util.LinkedList;
import java.util.List;

import com.wallpaper.model.Image;

public class ReflushBaseActivity extends BaseActivity {
	protected int mGridColumes = 3;
	protected int mLimitEachPage = 18;
	protected int skip = 0;

	protected List<Image> images = new LinkedList<Image>();

	protected static final String MESSAGE_GETED_IMAGES = "get %s images:%s";
	protected static final String MESSAGE_LOAD_IMAGE = "loading image:%s [size:%sx%s]";

	protected void refreshToLoad() {
	}
}
