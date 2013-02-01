package com.wallpaper.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.IconPagerAdapter;
import com.wallpaper.R;
import com.wallpaper.model.Cover;
import com.wallpaper.model.Covers;
import com.wallpaper.task.CoverListUpdateTask;
import com.wallpaper.task.OnProgressListenner;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.Utils;

public class CoverActivity extends BaseActivity {
	protected Handler handler = new Handler();

	protected Covers banners = new Covers();
	protected Covers covers = new Covers();
	
	protected static final String MESSAGE_LOAD_IMAGE = "loading image:%s [size:%sx%s]";
	
	DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher).bitmapConfig(Bitmap.Config.RGB_565)
	.cacheInMemory().cacheOnDisc().build();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_cover);
		
		final ViewPageAdapter mAdapter = new ViewPageAdapter();
		ViewPager mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setViewPager(mPager);
		indicator.setSnap(true);
		
		
		GridView grid = (GridView) findViewById(R.id.grid_view);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(ClassActivity.ACTION_SHOW_CLASS);
				Bundle bl = new Bundle();
				bl.putString(ClassActivity.DATA_LOAD_TAG, "" + covers.get(arg2).getTag());
				intent.putExtras(bl);
				startActivity(intent);
			}
		});
		final ClassAdapter adapter = new ClassAdapter();
		grid.setAdapter(adapter);
		
		new CoverListUpdateTask().setOnProgressListenner(new OnProgressListenner() {
			@Override
			public void onFinish(Object... result) {
				banners = (Covers)result[0];
				covers = (Covers)result[1];
				
				handler.post(new Runnable() {
					@Override
					public void run() {
						adapter.notifyDataSetChanged();
						mAdapter.notifyDataSetChanged();
					}
				});
			}
		}).start();
	}
	
	private class ViewPageAdapter extends PagerAdapter implements IconPagerAdapter {

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(arg0);
		}

		@Override
		public int getCount() {
			return banners.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			ImageView image = new ImageView(CoverActivity.this);
			image.setLayoutParams(mParams);
			image.setScaleType(ScaleType.CENTER_CROP);
			
			Cover banner = banners.get(arg1);
			ImageSize sourceSize = new ImageSize(banner.getWidth(), banner.getHeight());
			String url = Utils.Http.generateThumbImageUrl(sourceSize, banner.getSource());
			imageLoader.displayImage(url, image, options, null, sourceSize, sourceSize);
			
			((ViewPager) arg0).addView(image, 0);
			return image;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public int getIconResId(int index) {
			return 0;
		}

	}

	private class ClassAdapter extends BaseAdapter {
		
		private static final int GRID_COLUME_NUMBER = 2;
		private int mItemWidth = DISPLAY_SIZE.getWidth() / GRID_COLUME_NUMBER - (int)Utils.Densitys.dip2px(CoverActivity.this, 5);
		private int mItemHeight = (int)Math.floor(mItemWidth * 0.7);

		@Override
		public int getCount() {
			return covers.size();
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
				item = (View) getLayoutInflater().inflate(R.layout.item_image_cover, parent, false);
				item.setLayoutParams(new AbsListView.LayoutParams(mItemWidth, mItemHeight));
			} else {
				item = (View) convertView;
			}
			final ImageView image = (ImageView) item.findViewById(R.id.image);
			final TextView tile = (TextView)item.findViewById(R.id.title);

			Cover cover = covers.get(position);
			tile.setText(cover.getName());
			ImageSize sourceSize = new ImageSize(cover.getWidth(), cover.getHeight());
			String url = Utils.Http.generateThumbImageUrl(sourceSize, cover.getSource());
			LOG.i(CoverActivity.this, String.format(MESSAGE_LOAD_IMAGE, url, sourceSize.getWidth(), sourceSize.getHeight()));
			imageLoader.displayImage(url, image, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(Bitmap loadedImage) {
					Animation anim = AnimationUtils.loadAnimation(CoverActivity.this, R.anim.fade_in);
					image.setAnimation(anim);
					anim.start();
				}
			}, sourceSize, sourceSize);
			return item;
		}
	}
}
