package com.wallpaper.ui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
import com.wallpaper.task.ImgListUpdateFromTagTask;
import com.wallpaper.task.OnProgressListenner;
import com.wallpaper.ui.MyScrollListener.onScrollListenner;
import com.wallpaper.utils.DisplayManager;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.Utils;

public class HomeActivity extends BaseActivity {

	private static final ImageSize IMAGE_SIZE = new ImageSize(
			DisplayManager.getInstance().getWallpaperWidth(), 
			DisplayManager.getInstance().getWallpaperHeight());
	private static final ImageSize IMAGE_THUBMNAIL_SIZE = new ImageSize(200, 200);
	
	private static final int GRID_COLUMES = 3;
	private static final int LIMIT_EACH_PAGE = GRID_COLUMES * 10;
	private int skip = 0;
	private List<String> imags = new LinkedList<String>();
	
	private Handler handler = new Handler();
	private ClassAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
	
	private void refreshToLoad(){
		new ImgListUpdateFromTagTask(Const.TAGS.TAG_CALSS_GRIL, IMAGE_SIZE, skip, LIMIT_EACH_PAGE).setOnProgressListenner(new OnProgressListenner() {
			@Override
			public void onFinish(Object... result) {
				final String[] urls = (String[]) result[1];
				if (LOG.isLoggindAble) LOG.i(HomeActivity.this, String.format("get %s images:%s",urls.length, Arrays.toString(urls)));
				handler.post(new Runnable() {
					@Override
					public void run() {
						skip = skip + urls.length;
						for(int i = 0;i < urls.length; i++){
							imags.add(urls[i]);
						}
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
			return imags.size() - imags.size() % GRID_COLUMES;
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

			String url = Utils.Http.generateThumbImageUrl(IMAGE_THUBMNAIL_SIZE, imags.get(position));
			if (LOG.isLoggindAble) LOG.i(HomeActivity.this, String.format("loading image:%s", url));
			imageLoader.displayImage(url, image, options, new SimpleImageLoadingListener() {
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
