package com.wallpaper.utils;

import android.app.WallpaperManager;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class DisplayManager {
	private static DisplayManager instance = null;
	private int displayWidth = 0;
	private int displayHeight = 0;
	private int desiredWidth = 0;
	private int desiredHeight = 0;
	private int wallpaperWidth = 0;
	private int wallpaperHeight = 0;
	private int densityDpi = 0;
	private DisplayMetrics dm = null;

	public static DisplayManager getInstance() {
		if (instance == null) {
			synchronized (DisplayManager.class) {
				if (instance == null) {
					instance = new DisplayManager();
				}
			}
		}
		return instance;
	}

	private DisplayManager() { }

	private String init(Context context) {
		try {
			dm = new DisplayMetrics();
			WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(dm);
			displayWidth = dm.widthPixels;
			displayHeight = dm.heightPixels;
			densityDpi = dm.densityDpi;
		} catch (Exception e) {
		}
		try {
			desiredWidth = WallpaperManager.getInstance(context).getDesiredMinimumWidth();
			desiredHeight = WallpaperManager.getInstance(context).getDesiredMinimumHeight();
		} catch (Exception e) {
		}
		int widthTmp = desiredWidth <= 0 ? displayWidth : desiredWidth;
		int heightTmp = desiredHeight <= 0 ? displayHeight : desiredHeight;
		try {
			if (desiredHeight > 0 && desiredWidth > 0 && desiredHeight > desiredWidth) {
				this.wallpaperWidth = widthTmp > heightTmp ? displayWidth : widthTmp;
				this.wallpaperHeight = widthTmp > heightTmp ? displayHeight : heightTmp;
			} else {
				this.wallpaperWidth = widthTmp < heightTmp ? widthTmp * 2 : widthTmp;
				this.wallpaperHeight = heightTmp;
			}
		} catch (Exception e) {
			this.wallpaperWidth = displayWidth;
			this.wallpaperHeight = displayHeight;
		}
		return "dis/des/res: " + displayWidth + "x" + displayHeight + "/" + desiredWidth + "x" + desiredHeight + "/" 
				+ this.wallpaperWidth + "x" + this.wallpaperHeight;
	}

	public void refresh(Context context) {
		this.init(context);
	}

	public boolean isVerticalScreen() {
		return desiredHeight > 0 && desiredWidth > 0 && desiredHeight > desiredWidth;
	}

	public int getDisplayWidth() {
		return displayWidth;
	}

	public int getDisplayHeight() {
		return displayHeight;
	}

	public int getDesiredWidth() {
		return desiredWidth;
	}

	public int getDesiredHeight() {
		return desiredHeight;
	}

	public int getWallpaperWidth() {
		return wallpaperWidth;
	}

	public int getWallpaperHeight() {
		return wallpaperHeight;
	}

	public DisplayMetrics getDisplayMetrics() {
		return dm;
	}

	public int getDensityDpi() {
		return densityDpi;
	}

}
