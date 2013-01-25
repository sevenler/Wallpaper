package com.wallpaper.ui;

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
	
	private String mTag;
	public static final String DATA_LOAD_TAG = "tag";
	public static final String ACTION_SHOW_CLASS = "com.wallpaper.class.action";
	
	public static final String MESSAGE_GET_TAG = "get tag %s";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_main);
		Intent intent = getIntent();
		Bundle bl = intent.getExtras();
		mTag = (bl == null) ? Const.TAGS.TAG_CALSS_GRIL : bl.getString(DATA_LOAD_TAG);
		LOG.i(this, String.format(MESSAGE_GET_TAG, mTag));
		
		GridView grid = (GridView) findViewById(R.id.grid_view);
		grid.setOnScrollListener(new MyScrollListener(new onScrollListenner() {
			@Override
			public void onTouchBottom() {
				refreshToLoad();
			}
		}));
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(ImageActivity.ACTION_IMAGE);
				Bundle bl = new Bundle();
				bl.putSerializable(ImageActivity.DATA_IMAGE_LIST, images);
				bl.putSerializable(ImageActivity.DATA_IMAGE_INDEX_DEFUEAT, arg2);
				intent.putExtras(bl);
				startActivity(intent);
			}
		});
		adapter = new ClassAdapter();
		grid.setAdapter(adapter);
		refreshToLoad();
	}

	@Override
	protected void refreshToLoad() {
		new ImgListUpdateFromTagTask(mTag, DISPLAY_SIZE, skip, mLimitEachPage).setOnProgressListenner(new OnProgressListenner() {
			@Override
			public void onFinish(Object... result) {
				final Images imgs = (Images) result[0];
				LOG.i(HomeActivity.this, String.format(MESSAGE_GETED_IMAGES, imgs.size(), imgs.toString()));
				if ((imgs == null) || (imgs.size() <= 0))
					return;
				skip += imgs.size();

				handler.post(new Runnable() {
					@Override
					public void run() {
						images.add(imgs);
						adapter.notifyDataSetChanged();
					}
				});
			}
		}).start();
	}

	private class ClassAdapter extends BaseAdapter {
		DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher).bitmapConfig(Bitmap.Config.RGB_565)
				.cacheInMemory().cacheOnDisc().build();
		
		private static final int GRID_COLUME_NUMBER = 3;
		private int mItemWidth = DISPLAY_SIZE.getWidth() / GRID_COLUME_NUMBER - (int)Utils.Densitys.dip2px(HomeActivity.this, 5);
		private int mItemHeight = mItemWidth;

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
				item.setLayoutParams(new AbsListView.LayoutParams(mItemWidth, mItemHeight));
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
