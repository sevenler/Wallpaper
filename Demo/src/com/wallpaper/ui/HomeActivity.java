package com.wallpaper.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.wallpaper.Const;
import com.wallpaper.R;
import com.wallpaper.model.Image;
import com.wallpaper.model.Images;
import com.wallpaper.task.ImgListUpdateFromTagTask;
import com.wallpaper.task.OnProgressListenner;
import com.wallpaper.ui.MyScrollListener.onScrollListenner;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.Utils;

public class HomeActivity extends ReflushBaseActivity {
	protected Handler handler = new Handler();
	protected ClassAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_main);
		GridView grid = (GridView) findViewById(R.id.grid_view);
		grid.setOnScrollListener(new MyScrollListener(new onScrollListenner() {
			@Override
			public void onTouchBottom() {
				refreshToLoad();
			}
		}));
		adapter = new ClassAdapter();
		grid.setAdapter(adapter);
		refreshToLoad();
	}
	
	@Override
	protected void refreshToLoad(){
		new ImgListUpdateFromTagTask(Const.TAGS.TAG_CALSS_GRIL, DISPLAY_SIZE, skip, mLimitEachPage).setOnProgressListenner(new OnProgressListenner() {
			@Override
			public void onFinish(Object... result) {
				final Images imgs = (Images) result[0];
				LOG.i(HomeActivity.this, String.format(MESSAGE_GETED_IMAGES, imgs.size(), imgs.toString()));
				if((imgs == null) || (imgs.size() <= 0)) return;
				skip += imgs.size();
				
				handler.post(new Runnable() {
					@Override
					public void run() {
						images.addAll(imgs.getAll());
						adapter.notifyDataSetChanged();
					}
				});
			}
		}).start();
	}
	
	private class ClassAdapter extends BaseAdapter {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.cacheInMemory().cacheOnDisc().build();

		@Override
		public int getCount() {
			return images.size() - images.size() % mGridColumes;
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
				item = (View) getLayoutInflater().inflate(R.layout.item_image_main, parent, false);
			} else {
				item = (View) convertView;
			}
			final ImageView image = (ImageView) item.findViewById(R.id.image);

			Image img = images.get(position);
			
			ImageSize loadSize = Utils.Images.getImageSizeScaleTo(image);
			String url = Utils.Http.generateThumbImageUrl(loadSize, img.getSource());
			LOG.i(HomeActivity.this, String.format(MESSAGE_LOAD_IMAGE, url, img.getWidth(), img.getHeight()));
			imageLoader.displayImage(url, image, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(Bitmap loadedImage) {
					Animation anim = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.fade_in);
					image.setAnimation(anim);
					anim.start();
				}
			}, loadSize, loadSize);
			return item;
		}
	}
}
