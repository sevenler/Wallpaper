package com.wallpaper.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import com.wallpaper.model.Image;
import com.wallpaper.model.Images;
import com.wallpaper.task.ImgListUpdateFromTagTask;
import com.wallpaper.task.OnProgressListenner;
import com.wallpaper.utils.DisplayManager;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.Utils;

public class ImageActivity extends ReflushBaseActivity {

	protected Handler handler = new Handler();
	protected SamplePagerAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_image);
		ViewPager pager = (ViewPager) findViewById(R.id.pager);
		adapter = new SamplePagerAdapter();
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				//到底后隐藏方向图标
				if( (arg0 == (adapter.getCount() - 1))){
					refreshToLoad();
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }
			
			@Override
			public void onPageScrollStateChanged(int arg0) { }
		});
		refreshToLoad();
	}
	
	@Override
	protected void refreshToLoad(){
		new ImgListUpdateFromTagTask(Const.TAGS.TAG_CALSS_GRIL, WALLPAPER_SIZE, skip, mLimitEachPage).setOnProgressListenner(new OnProgressListenner() {
			@Override
			public void onFinish(Object... result) {
				final Images imgs = (Images) result[0];
				if(imgs == null) return;
				LOG.i(ImageActivity.this, String.format(MESSAGE_GETED_IMAGES, imgs.size(), imgs.toString()));
				skip = skip + imgs.size();
				System.out.println("skip:" + skip);
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
	
	protected class SamplePagerAdapter extends PagerAdapter {

		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.bitmapConfig(Bitmap.Config.ARGB_8888)
		.cacheInMemory().cacheOnDisc().build();
		
		private ImageSize mLoadSize;
		private SamplePagerAdapter(){
			mLoadSize = new ImageSize(DisplayManager.getInstance().getDisplayWidth(), DisplayManager.getInstance().getDisplayHeight() / 2);
		}
		@Override
		public int getCount() {
			return images.size() - images.size() % mGridColumes;
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View item = (View) getLayoutInflater().inflate(R.layout.item_image, null, false);
			container.addView(item, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			final ImageView image = (ImageView) item.findViewById(R.id.image);
			
			Image img = images.get(position);
			String url = Utils.Http.generateThumbImageUrl(mLoadSize, img.getSource());
			LOG.i(ImageActivity.this, String.format(MESSAGE_LOAD_IMAGE, url, mLoadSize.getWidth(), mLoadSize.getHeight()));
			imageLoader.displayImage(url, image, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(Bitmap loadedImage) {
					Animation anim = AnimationUtils.loadAnimation(ImageActivity.this, R.anim.fade_in);
					image.setAnimation(anim);
					anim.start();
				}
			}, mLoadSize, mLoadSize);
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
