package com.wallpaper.ui;

import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wallpaper.R;

public class ProgressDialog {
	android.app.AlertDialog ad;

	TextView titleView;
	TextView contentView;
	ProgressBar progress;

	public ProgressDialog(Context context, String title, String content) {
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.show();
		Window window = ad.getWindow();
		window.setContentView(R.layout.progress_dialog);

		titleView = (TextView) window.findViewById(R.id.id_dialog_title);
		titleView.setText(title);
		contentView = (TextView) window.findViewById(R.id.id_dialog_content);
		contentView.setText(content);
		progress = (ProgressBar) window.findViewById(R.id.id_dialog_progressbar);
		
		setCancelable(false);
	}

	public void setProgress(int value) {
		progress.setProgress(value);
	}
	
	public void setMax(int value) {
		progress.setMax(value);
	}

	public void dismiss() {
		ad.dismiss();
	}

	public void setCancelable(boolean flag) {
		ad.setCancelable(flag);
	}

	public void setCanceledOnTouchOutside(boolean flag) {
		ad.setCanceledOnTouchOutside(flag);
	}
	
	public void updateContent(String content){
		contentView.setText(content);
	}

	public boolean isShowing() {
		return ad.isShowing();
	}
}