package com.wallpaper.ui;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.wallpaper.R;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.SpeedDetectorOnScrollListener;
import com.wallpaper.utils.Utils;

public class LocalAlbumActivity extends BaseActivity {
	protected Handler handler = new Handler();
	protected ClassAdapter adapter;
	private GridView grid;
	
	private static final String MESSAGE_LOAD_IMAGE = "loading image:%s [size:%sx%s]";
	
	private Map<String, Integer> folders = new LinkedHashMap<String, Integer>();//相册文件夹
	private Map<String, List<String>> covers = new LinkedHashMap<String, List<String>>();//文件夹对应的图片封面
	private String[] foldersArray;//数组结构的相册文件夹
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_local_album);
		grid = (GridView) findViewById(R.id.grid_view);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				try{
					String dir = new File(covers.get(foldersArray[arg2]).get(0)).getParentFile().getAbsolutePath().replace("/", "//");
					Intent intent = new Intent(LocalAlbumActivity.this, LocalFolderActivity.class);
					intent.putExtra(LocalFolderActivity.DATA_LOAD_DIR, dir);
					startActivity(intent);
				}catch(Exception ex){
					LOG.e(LocalAlbumActivity.this, ex);
				}
			}
		});
		grid.setOnScrollListener(new SpeedDetectorOnScrollListener());
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		getAblumCovers(this, 4);
		String[] array = new String[folders.size()];
		foldersArray = folders.keySet().toArray(array);
		adapter = new ClassAdapter();
		grid.setAdapter(adapter);
	}

	//读取相册的文件夹和封面数据
	private Map<String, Integer> getAblumCovers(Context context, int coverNumMax){
		String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.ORIENTATION };
		Cursor cursor = null;
		try{
			cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, 
					projection, null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
		}catch(Exception ex){
			LOG.e(LocalAlbumActivity.this, ex);
		}
		
		folders.clear();
		covers.clear();
		
		String folderTemp;
		File fileTemp;
		List<String> cover;
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
			try {
				fileTemp = new File(path);
				folderTemp = fileTemp.getParentFile().getName();
				
				if(folders.containsKey(folderTemp)){
					folders.put(folderTemp, folders.get(folderTemp) + 1);
					//添加封面图片
					cover = covers.get(folderTemp);
					if(cover.size() < coverNumMax){
						cover.add(fileTemp.getAbsolutePath());
					}
				}else{
					folders.put(folderTemp, 1);
					//添加封面图片
					cover = new LinkedList<String>();
					cover.add(fileTemp.getAbsolutePath());
					covers.put(folderTemp, cover);
				}
			} catch (Exception e) {
				LOG.e(LocalAlbumActivity.this, e);
			}
		}
		return folders;
	}
	
	private String generateAbsolute(String file, boolean isPrefix){
		file = file.replaceAll("/", "//");
		if(isPrefix) return String.format("file://%s", file);
		else return String.format("//%s", file);
	}
	
	private class ClassAdapter extends BaseAdapter {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			    .showStubImage(R.drawable.ic_launcher)
			    .showImageForEmptyUri(R.drawable.ic_launcher)
			    .bitmapConfig(Bitmap.Config.RGB_565)
				.cacheInMemory().cacheOnDisc().build();
		
		private static final int GRID_COLUME_NUMBER = 2;
		private int mItemWidth = DISPLAY_SIZE.getWidth() / GRID_COLUME_NUMBER - (int)Utils.Densitys.dip2px(LocalAlbumActivity.this, 20);
		private int mItemHeight = mItemWidth;
		
		@Override
		public int getCount() {
			return foldersArray.length;
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
				item = (View) getLayoutInflater().inflate(R.layout.item_image_local_album, parent, false);
				item.setLayoutParams(new AbsListView.LayoutParams(mItemWidth, mItemHeight));
			} else {
				item = (View) convertView;
			}
			String folder = foldersArray[position];
			
			TextView title = (TextView)item.findViewById(R.id.folder_name);
			title.setText(String.format("%s(%s)", folder, folders.get(folder)));
			
			
			ImageView image;
			String img;
			ImageSize loadSize;
			String uri;
			List<String> temp;
			for(int i= 0 ;i < 4;i++){
				image  = (ImageView) item.findViewById(R.id.image0 + i);
				temp = covers.get(folder); 
				if(temp.size() > i){
					img = temp.get(i);
					loadSize = Utils.Images.getImageSizeScaleTo(image);
					uri = generateAbsolute(img, true);
					imageLoader.displayImage(uri, image, options, null, null, loadSize);
					LOG.i(LocalAlbumActivity.this, String.format(MESSAGE_LOAD_IMAGE, uri, loadSize.getWidth(), loadSize.getHeight()));
				}
			}
			return item;
		}
	}
}
