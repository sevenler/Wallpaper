package com.wallpaper.ui;

import java.util.HashSet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.KeyEvent;

import com.wallpaper.widget.GridViewSpecial;
import com.wallpaper.widget.IImage;

public class EnableMuiltSelectActivity extends TestActivity{
	private HashSet<IImage> mMultiSelected = null;
	
	private void closeMultiSelectMode() {
        if (mMultiSelected == null) return;
        mMultiSelected = null;
        grid.invalidate();
    }

    private void openMultiSelectMode() {
        if (mMultiSelected != null) return;
        mMultiSelected = new HashSet<IImage>();
        grid.invalidate();
    }
    
    private boolean isInMultiSelectMode() {
        return mMultiSelected != null;
    }
    
    private void toggleMultiSelected(IImage image) {
        if (!mMultiSelected.add(image)) {
            mMultiSelected.remove(image);
        }
        grid.invalidate();
    }
    
    @Override
	public void drawDecoration(Canvas canvas, int index, int xPos, int yPos, int w, int h) {
		if (isInMultiSelectMode()) {
            Bitmap bit = mMultiSelected.contains(mImageList.getImageAt(index))
            		? grid.mOutline[GridViewSpecial.OUTLINE_SELECTED]
            		: grid.mOutline[GridViewSpecial.OUTLINE_EMPTY];
            
            canvas.drawBitmap(bit, xPos, yPos, null);
        }
	}
    
    @Override
    public boolean needsDecoration() {
        return isInMultiSelectMode();
    }
    
    @Override
	public void onImageClicked(int index) {
    	if(isInMultiSelectMode()){
    		toggleMultiSelected(mImageList.getImageAt(index));
    	}else{
    		super.onImageClicked(index);
    	}
	}
    
    @Override
    protected void onLongPressClicked(){
    	openMultiSelectMode();
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
            	if(isInMultiSelectMode()) {
            		closeMultiSelectMode();
            		return true;
            	}
        }
        return super.onKeyDown(keyCode, event);
    }
}
