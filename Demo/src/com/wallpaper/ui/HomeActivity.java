package com.wallpaper.ui;

import java.io.File;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.wallpaper.R;

public class HomeActivity extends Activity {
	private ImageLoader imageLoader = ImageLoader.getInstance(); 
	private String[] imags = {};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		imags = getImagsFormDir();
		GridView grid = (GridView)findViewById(R.id.grid_view);
		final ClassAdapter adapter = new ClassAdapter();
		grid.setAdapter(adapter);
	}
	
	private final String IMAGES_DIR = "/mnt/sdcard/androidesk/onekeywallpapers";
	private final String FORMAT_LOAD_URL = "file://%s/%s";
	private String[] getImagsFormDir(){
		File file = new File(IMAGES_DIR);
		String[] imags = file.list();
		for(int i = 0;i < imags.length;i++){
			imags[i] = String.format(FORMAT_LOAD_URL, IMAGES_DIR, imags[i]);
		}
		return imags;
	}
	
	private class ClassAdapter extends BaseAdapter {

		DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.cacheInMemory().build();
		
		@Override
		public int getCount() {
			return imags.length;
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
				item = (View) getLayoutInflater().inflate(R.layout.item_image, parent, false);
			} else {
				item = (View) convertView;
			}
			final ImageView image = (ImageView) item.findViewById(R.id.image);
			imageLoader.displayImage(imags[position], image, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(Bitmap loadedImage) {
					Animation anim = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fade_in);
					image.setAnimation(anim);
					anim.start();
				}
			});
			return item;
		}
	}

}
