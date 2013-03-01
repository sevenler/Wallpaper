package com.wallpaper.utils;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

//gridview 滑动速度检测的listener
public class SpeedDetectorOnScrollListener implements OnScrollListener {

    private long timeStamp = System.currentTimeMillis();
    private int lastOffset = 0;
    private long scrollingSpeed = 0 ;
    private long loadMaxSpeed = 1000;//低于这个速度就开始加载
    private final OnScrollListener externalListener;
    
    public SpeedDetectorOnScrollListener () {
        this(null);
    }
    
    public SpeedDetectorOnScrollListener (OnScrollListener externalListener) {
        this.externalListener = externalListener;
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    	if (externalListener != null) {
			externalListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
    	
		long currentTime = System.currentTimeMillis();
        long durationTime = currentTime - timeStamp;
        int currentOffset = computeVerticalScrollOffset(view, firstVisibleItem, visibleItemCount);
        int durationOffset = currentOffset - lastOffset;
        timeStamp = currentTime;
		lastOffset = currentOffset;
		if(durationTime < 10) return;//为了保证速度检测的有效性，时间很短的话，容易得到的offset为0
		
        scrollingSpeed = Math.abs(durationOffset * 1000 / ((durationTime == 0) ? 1L : durationTime));
        //System.out.println(String.format("scrollingSpeed:%s durationOffset:%s durationTime:%s", scrollingSpeed, durationOffset, durationTime));
        
        if((firstVisibleItem + visibleItemCount == totalItemCount) || (firstVisibleItem == 0)) scrollingSpeed = 0;
        if(scrollingSpeed < loadMaxSpeed){
			ImageLoader.getInstance().resume();
		}else{
			ImageLoader.getInstance().pause();
		}
    }
    
    //计算gridview的滑动偏移量
    private int computeVerticalScrollOffset(AbsListView grid, int mFirstPosition, int mItemCount) {
        if (grid != null && mFirstPosition >= 0 && grid.getChildCount() > 0) {
            final View view = grid.getChildAt(0);
            final int top = view.getTop();
            int height = view.getHeight();
            if (height > 0) {
                final int numColumns = 3;
                final int whichRow = mFirstPosition / numColumns;
                final int rowCount = (mItemCount + numColumns - 1) / numColumns;
                return Math.max(whichRow * 100 - (top * 100) / height +
                        (int) ((float) grid.getScrollY() / grid.getHeight() * rowCount * 100), 0);
            }
        }
        return 0;
    }

	@Override
	public void onScrollStateChanged(AbsListView paramAbsListView, int scrollState) {
		if (externalListener != null) {
			externalListener.onScrollStateChanged(paramAbsListView, scrollState);
		}
		//上面的速度检测，不能百分百准确 下面的系统滑动机制的判断一定能保证滑动停止后加载图片
		switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				ImageLoader.getInstance().resume();
				break;
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				ImageLoader.getInstance().pause();
				break;
		}
	}
 }