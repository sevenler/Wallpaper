package com.wallpaper.ui;

import android.widget.GridView;

import com.wallpaper.R;

public class HomeActivity extends BaseListActivity {

	@Override
	protected GridView initUI() {
		setContentView(R.layout.ac_main);
		GridView grid = (GridView) findViewById(R.id.grid_view);
		return grid;
	}
}
