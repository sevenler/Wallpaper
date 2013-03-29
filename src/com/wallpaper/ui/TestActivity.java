package com.wallpaper.ui;

import java.io.File;
import java.util.HashSet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.wallpaper.R;
import com.wallpaper.widget.GridViewSpecial;
import com.wallpaper.widget.IImage;
import com.wallpaper.widget.IImageList;
import com.wallpaper.widget.Image;
import com.wallpaper.widget.ImageList;
import com.wallpaper.widget.ImageLoader;

public class TestActivity extends BaseActivity implements
		GridViewSpecial.Listener, GridViewSpecial.DrawAdapter {
	protected ImageLoader mLoader = null;
	protected IImageList mImageList = getImageList();
	protected GridViewSpecial grid= null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLoader = new ImageLoader(getContentResolver(), new Handler());
		
		setContentView(R.layout.ac_test);
		grid = (GridViewSpecial)findViewById(R.id.grid);
		grid.setAdapter(new ImageAdapter());
		
		grid.setListener(this);
		grid.setDrawAdapter(this);
		grid.setOnCreateContextMenuListener(
                new CreateContextMenuListener());
		
		grid.start();
	}
	
	private class CreateContextMenuListener implements
			View.OnCreateContextMenuListener {
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenu.ContextMenuInfo menuInfo) {
			onLongPressClicked();
		}
	}
	
	protected void onLongPressClicked(){
		IImage image = getCurrentImage();
		String title = (image == null ? "" : image.getTitle());
		Toast.makeText(TestActivity.this, title, Toast.LENGTH_SHORT).show();
	}
	
	private IImage getCurrentImage() {
        int currentSelection = grid.getCurrentSelection();
        if (currentSelection < 0
                || currentSelection >= mImageList.getCount()) {
            return null;
        } else {
            return mImageList.getImageAt(currentSelection);
        }
    }
	
	private static String IMAGE_DIR = "sdcard/androidesk/wallpapers"; 
	public IImageList getImageList(){
		File file = new File(IMAGE_DIR);
		File[] files = file.listFiles();
		ImageList images = new ImageList(files.length);
		Image img = null;
		for(File f : files){
			img = new Image(f.getAbsolutePath());
			images.add(img);
		}
		return images;
	}
	
	private class ImageAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mImageList.getCount();
		}

		@Override
		public Object getItem(int paramInt) {
			return mImageList.getImageAt(paramInt);
		}

		@Override
		public long getItemId(int paramInt) {
			return paramInt;
		}

		@Override
		public View getView(int paramInt, View paramView,
				ViewGroup paramViewGroup) {
			return null;
		}
		
	}
	
	
	protected final Rect mSrcRect = new Rect();
	protected final Rect mDstRect = new Rect();
	protected final Paint mPaint = new Paint(Paint.FILTER_BITMAP_FLAG);
	
	@Override
	public void drawImage(Canvas canvas, Bitmap b, int xPos,
			int yPos, int w, int h) {
		if (b != null) {
            // if the image is close to the target size then crop,
            // otherwise scale both the bitmap and the view should be
            // square but I suppose that could change in the future.

            int bw = b.getWidth();
            int bh = b.getHeight();

            int deltaW = bw - w;
            int deltaH = bh - h;

            if (deltaW >= 0 && deltaW < 10 &&
                deltaH >= 0 && deltaH < 10) {
                int halfDeltaW = deltaW / 2;
                int halfDeltaH = deltaH / 2;
                mSrcRect.set(0 + halfDeltaW, 0 + halfDeltaH,
                        bw - halfDeltaW, bh - halfDeltaH);
                mDstRect.set(xPos, yPos, xPos + w, yPos + h);
                canvas.drawBitmap(b, mSrcRect, mDstRect, null);
            } else {
                mSrcRect.set(0, 0, bw, bh);
                mDstRect.set(xPos, yPos, xPos + w, yPos + h);
                canvas.drawBitmap(b, mSrcRect, mDstRect, mPaint);
            }
        } else {
            // If the thumbnail cannot be drawn, put up an error icon
            // instead
          /*  Bitmap error = getErrorBitmap(image);
            int width = error.getWidth();
            int height = error.getHeight();
            mSrcRect.set(0, 0, width, height);
            int left = (w - width) / 2 + xPos;
            int top = (w - height) / 2 + yPos;
            mDstRect.set(left, top, left + width, top + height);
            canvas.drawBitmap(error, mSrcRect, mDstRect, null);*/
        }

	}

	@Override
	public void drawDecoration(Canvas canvas, int index, int xPos, int yPos, int w, int h) {
	}
	@Override
	public boolean needsDecoration() {
		return false;
	}
	
	
	private float mScrollPosition = INVALID_POSITION;
	private static final float INVALID_POSITION = -1f;
	
	private boolean mLayoutComplete;
	private Uri mCropResultUri;
	@Override
	public void onImageClicked(int index) {
	}
	@Override
	public void onImageTapped(int index) {
		onImageClicked(index);
	}
	@Override
	public void onLayoutComplete(boolean changed) {
		 /*mLayoutComplete = true;
	        if (mCropResultUri != null) {
	            IImage image = mAllImages.getImageForUri(mCropResultUri);
	            mCropResultUri = null;
	            if (image != null) {
	                mSelectedIndex = mAllImages.getImageIndex(image);
	            }
	        }
	        mGvs.setSelectedIndex(mSelectedIndex);
	        if (mScrollPosition == INVALID_POSITION) {
	            if (mSortAscending) {
	                mGvs.scrollTo(0, mGvs.getHeight());
	            } else {
	                mGvs.scrollToImage(0);
	            }
	        } else if (mConfigurationChanged) {
	            mConfigurationChanged = false;
	            mGvs.scrollTo(mScrollPosition);
	            if (mGvs.getCurrentSelection() != GridViewSpecial.INDEX_NONE) {
	                mGvs.scrollToVisible(mSelectedIndex);
	            }
	        } else {
	            mGvs.scrollTo(mScrollPosition);
	        }*/
	}
	@Override
	public void onScroll(float scrollPosition) {
		mScrollPosition = scrollPosition;
	}

	@Override
	public boolean onLoadImage(int index, ImageLoader.LoadedCallback callback) {
		IImage image = mImageList.getImageAt(index);
		if(image != null){
			mLoader.getBitmap(image, callback, index);
			return true;
		}else return false;
	}

	@Override
	public int[] onClearLoaderQueue() {
		int[] tags = mLoader.clearQueue();
		return tags;
	}

	@Override
	public boolean onCancelRequests(Object ob) {
		return mLoader.cancel((IImage)ob);
	}
	
	
	
}
