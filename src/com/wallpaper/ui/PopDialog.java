package com.wallpaper.ui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;

public class PopDialog {
	public static PopupWindow popup(Context ctx, View parent, int layout) {
		
		LayoutInflater layoutInflater = (LayoutInflater) ctx.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View window = layoutInflater.inflate(layout, null);
		PopupWindow popupWindow = new PopupWindow(window, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		popupWindow.showAsDropDown(parent, 0, 0);
		
		return popupWindow;
	}
}
