package com.wallpaper.ui;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.wallpaper.Const;
import com.wallpaper.R;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.SpeedDetectorOnScrollListener;
import com.wallpaper.utils.Utils;

public class LocalImagesActivity extends BaseActivity {
	protected Handler handler = new Handler();
	protected ClassAdapter adapter;
	
	private String mTag;
	public static final String DATA_LOAD_TAG = "tag";
	public static final String ACTION_SHOW_CLASS = "com.wallpaper.class.action";
	
	public static final String MESSAGE_GET_TAG = "get tag %s";
	protected static final String MESSAGE_LOAD_IMAGE = "loading image:%s [size:%sx%s]";
	
	private List<String> images = new LinkedList<String>();
	private String mDirectroy = "//mnt//sdcard//androidesk//onekeywallpapers";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		
		setContentView(R.layout.ac_class);
		Intent intent = getIntent();
		Bundle bl = intent.getExtras();
		mTag = (bl == null) ? Const.TAGS.TAG_CALSS_GRIL : bl.getString(DATA_LOAD_TAG);
		LOG.i(this, String.format(MESSAGE_GET_TAG, mTag));
		
		GridView grid = (GridView) findViewById(R.id.grid_view);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			}
		});
		grid.setOnScrollListener(new SpeedDetectorOnScrollListener());
		String[] imgs = getImages(mDirectroy);
		try{
			images.addAll(Arrays.asList(imgs));
		}catch(Exception ex){
			LOG.e(this, ex);
		}
		adapter = new ClassAdapter();
		grid.setAdapter(adapter);
	}
	
	private  String[] getImages(String dir){
		File directroy = new File(dir);
		if(directroy.exists()){
			return directroy.list();
		}
		return null;
	}
	
	private String generateAbsolute(String dir, String file){
		return String.format("file://%s%s%s", dir, "//", file);
	}

	private class ClassAdapter extends BaseAdapter {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			    .showStubImage(R.drawable.ic_launcher)
			    .showImageForEmptyUri(R.drawable.ic_launcher)
			    .bitmapConfig(Bitmap.Config.RGB_565)
				.cacheInMemory().cacheOnDisc().build();
		
		private static final int GRID_COLUME_NUMBER = 3;
		private int mItemWidth = DISPLAY_SIZE.getWidth() / GRID_COLUME_NUMBER - (int)Utils.Densitys.dip2px(LocalImagesActivity.this, 5);
		private int mItemHeight = mItemWidth;

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final View item;
			if (convertView == null) {
				item = (View) getLayoutInflater().inflate(R.layout.item_image_class, parent, false);
				item.setLayoutParams(new AbsListView.LayoutParams(mItemWidth, mItemHeight));
			} else {
				item = (View) convertView;
			}
			final ImageView image = (ImageView) item.findViewById(R.id.image);
			String img = images.get(position);

			ImageSize loadSize = Utils.Images.getImageSizeScaleTo(image);
			String uri = generateAbsolute(mDirectroy, img);
			LOG.i(LocalImagesActivity.this, String.format(MESSAGE_LOAD_IMAGE, uri, loadSize.getWidth(), loadSize.getHeight()));
			imageLoader.displayImage(uri, image, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(Bitmap loadedImage) {
					Animation anim = AnimationUtils.loadAnimation(LocalImagesActivity.this, R.anim.fade_in);
					image.setAnimation(anim);
					anim.start();
				}
			}, null, loadSize);
			return item;
		}
	}
}
