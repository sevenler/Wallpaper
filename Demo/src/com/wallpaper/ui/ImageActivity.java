package com.wallpaper.ui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.wallpaper.Const;
import com.wallpaper.R;
import com.wallpaper.task.ImgListUpdateFromTagTask;
import com.wallpaper.task.OnProgressListenner;
import com.wallpaper.utils.DisplayManager;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.Utils;

public class ImageActivity extends BaseActivity {

	private static final ImageSize IMAGE_SIZE = new ImageSize(
			DisplayManager.getInstance().getWallpaperWidth(), 
			DisplayManager.getInstance().getWallpaperHeight());
	private static final ImageSize IMAGE_THUBMNAIL_SIZE = new ImageSize(
			DisplayManager.getInstance().getWallpaperWidth(), 
			DisplayManager.getInstance().getWallpaperHeight());
	
	protected int mGridColumes = 3;
	protected int mLimitEachPage = mGridColumes * 10;
	protected int skip = 0;
	protected List<String> imags = new LinkedList<String>();
	protected Handler handler = new Handler();
	protected SamplePagerAdapter adapter;

	private static final String MESSAGE_GETED_IMAGES = "get %s images:%s";
	private static final String MESSAGE_LOAD_IMAGE = "loading image:%s";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_image);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		adapter = new SamplePagerAdapter();
		pager.setAdapter(adapter);
		refreshToLoad();
	}
	
	private void refreshToLoad(){
		new ImgListUpdateFromTagTask(Const.TAGS.TAG_CALSS_GRIL, IMAGE_SIZE, skip, mLimitEachPage).setOnProgressListenner(new OnProgressListenner() {
			@Override
			public void onFinish(Object... result) {
				final String[] urls = (String[]) result[1];
				if (LOG.isLoggindAble) LOG.i(ImageActivity.this, String.format(MESSAGE_GETED_IMAGES,urls.length, Arrays.toString(urls)));
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
	
	protected class SamplePagerAdapter extends PagerAdapter {

		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.cacheInMemory().cacheOnDisc().build();
		
		@Override
		public int getCount() {
			return imags.size() - imags.size() % mGridColumes;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			
			
			View item = (View) getLayoutInflater().inflate(R.layout.item_image, null, false);
			container.addView(item, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			
			final ImageView image = (ImageView) item.findViewById(R.id.image);
			String url = Utils.Http.generateThumbImageUrl(IMAGE_THUBMNAIL_SIZE, imags.get(position));
			if (LOG.isLoggindAble) LOG.i(ImageActivity.this, String.format(MESSAGE_LOAD_IMAGE, url));
			imageLoader.displayImage(url, image, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(Bitmap loadedImage) {
					Animation anim = AnimationUtils.loadAnimation(ImageActivity.this, R.anim.fade_in);
					image.setAnimation(anim);
					anim.start();
				}
			});
			
			return item;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
		
		@Override
		public int getItemPosition(Object object) {
		    return POSITION_NONE;
		}
	}
}
