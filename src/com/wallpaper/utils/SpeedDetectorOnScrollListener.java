package com.wallpaper.utils;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class SpeedDetectorOnScrollListener implements OnScrollListener {

    private long timeStamp;
    private int lastFirstVisibleItem = 0;
    private long scrollingSpeed = 0 ;
    private final OnScrollListener externalListener;
    
    private long loadMaxSpeed = 20;
    
    public SpeedDetectorOnScrollListener () {
    	this(null);
    }
    
    public SpeedDetectorOnScrollListener (OnScrollListener externalListener) {
        timeStamp = System.currentTimeMillis();
        lastFirstVisibleItem = 0;
        this.externalListener = externalListener;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (externalListener != null) {
			externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
		
		long currentTime = System.currentTimeMillis();
        long duration = currentTime - timeStamp;
        int mDurationItem = Math.abs(firstVisibleItem - lastFirstVisibleItem);
		scrollingSpeed = ((mDurationItem == 0) ? 1 : mDurationItem) * 500 / ((duration == 0) ? 1 : duration);
		if((firstVisibleItem + visibleItemCount == totalItemCount) || (firstVisibleItem == 0)) scrollingSpeed = 1;
		if(mDurationItem == 0) return;
		timeStamp = currentTime;
		lastFirstVisibleItem = firstVisibleItem;
		
		if(scrollingSpeed < loadMaxSpeed){
			ImageLoader.getInstance().resume();
		}else{
			ImageLoader.getInstance().pause();
		}
    }

     public long getSpeed()
     {
       return scrollingSpeed;
     }

	@Override
	public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {
		if (externalListener != null) {
			externalListener.onScrollStateChanged(paramAbsListView, paramInt);
		}
	}
 }