package com.wallpaper.ui;

import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wallpaper.R;

public class CommonDialog {
	Context context;
	android.app.AlertDialog ad;
	
	Button bt_commit;
	Button bt_cancel;
	TextView titleView;
	TextView contentView;

	public CommonDialog(Context context, String title, String content) {
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.show();
		Window window = ad.getWindow();
		window.setContentView(R.layout.common_dialog);
		
		titleView = (TextView) window.findViewById(R.id.id_dialog_title);
		contentView = (TextView) window.findViewById(R.id.id_dialog_content);
		bt_commit = (Button) window.findViewById(R.id.id_dialog_commit);
		bt_cancel = (Button) window.findViewById(R.id.id_dialog_cancel);
		
	}

	/**
	 * 设置按钮
	 * 
	 * @param text
	 *            现实的文字
	 * @param listener
	 *            按钮的监听事件
	 */
	public void setPositiveButton(String text,
			final View.OnClickListener listener) {
		bt_commit.setVisibility(View.VISIBLE);
		bt_commit.setText(text);
		bt_commit.setOnClickListener(listener);
	}

	/**
	 * 设置按钮
	 * 
	 * @param text
	 *            显示的文字
	 * @param listener
	 *            按钮监听的事件
	 */
	public void setNegativeButton(String text,
			final View.OnClickListener listener) {
		bt_cancel.setVisibility(View.VISIBLE);
		bt_cancel.setText(text);
		bt_cancel.setOnClickListener(listener);
	}

	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}

	/**
	 * 对话框显示的时候,是否可以取消
	 */
	public void setCancelable(boolean flag) {
		ad.setCancelable(flag);
	}

	/**
	 * 如果你触摸屏幕其它区域,消失Dialog
	 * 
	 * @param flag
	 */
	public void setCanceledOnTouchOutside(boolean flag) {
		ad.setCanceledOnTouchOutside(flag);
	}

	/**
	 * 判断dialog是否在显示
	 * @return
	 */
	public boolean isShowing() {
		return ad.isShowing();
	}
}