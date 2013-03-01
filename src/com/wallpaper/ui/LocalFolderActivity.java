package com.wallpaper.ui;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.wallpaper.Const;
import com.wallpaper.R;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.SpeedDetectorOnScrollListener;
import com.wallpaper.utils.Utils;

public class LocalFolderActivity extends BaseActivity {
	protected Handler handler = new Handler();
	protected ClassAdapter adapter;
	
	private boolean isSelectedMode = false;
	
	private List<String> images = new LinkedList<String>();
	private Set<Integer> selectedImages = new HashSet<Integer>();
	private String mDirectroy;
	
	public static final String DATA_LOAD_DIR = "dir";
	private static final String MESSAGE_GET_TAG = "get dir %s";
	private static final String MESSAGE_LOAD_IMAGE = "loading image:%s [size:%sx%s]";
	private static final String DELETE_IMAGES = "delete %s files";
	private static final String DELETEING_IMAGE = "delete [%s] %s";
	private static final String DELETEING_IMAGE_DIALOG = "deleting   %s/%s";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.ac_local_folder);
		Intent intent = getIntent();
		Bundle bl = intent.getExtras();
		mDirectroy = (bl == null) ? Const.TAGS.TAG_CALSS_GRIL : bl.getString(DATA_LOAD_DIR);
		LOG.i(this, String.format(MESSAGE_GET_TAG, mDirectroy));
		
		final ViewGroup editBar = (ViewGroup)findViewById(R.id.panel_edit);
		final ViewGroup normalBar = (ViewGroup)findViewById(R.id.panel_normal);
		ImageButton edit = (ImageButton)normalBar.findViewById(R.id.button_edit);
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				normalBar.setVisibility(View.GONE);
				editBar.setVisibility(View.VISIBLE);
				isSelectedMode = true;
			}
		});
		final ImageButton delete = (ImageButton)editBar.findViewById(R.id.button_delete);
		delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final PopupWindow window = PopDialog.popup(LocalFolderActivity.this, delete, R.layout.pop_muilt_delete);
				View muiltDelete = window.getContentView().findViewById(R.id.delete);
				muiltDelete.setOnClickListener(new OnClickListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onClick(View v) {
						window.dismiss();
						
						if(selectedImages.size() == 0) return;
						ProgressDialog dialog = new ProgressDialog(LocalFolderActivity.this, "Delete", 
								String.format(DELETEING_IMAGE_DIALOG, 0, selectedImages.size()));
						dialog.setMax(selectedImages.size());
						dialog.setCancelable(false);
						DeleteTask deleteTask = new DeleteTask(dialog, new Handler());
						HashMap<String, Integer> images = getSelectedImages();
						deleteTask.execute(images);
					}
				});
			}
		});
		ImageButton allSelect = (ImageButton)editBar.findViewById(R.id.button_all_choose);
		allSelect.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(images.size() == selectedImages.size()){
					selectedImages.clear();
				}else{
					int size = images.size();
					for (int i = 0; i < size; i++) {
						selectedImages.add(i);
					}
				}
				adapter.notifyDataSetInvalidated();
			}
		});
		ImageButton allSelectCancel = (ImageButton)editBar.findViewById(R.id.button_all_choose_cancel);
		allSelectCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				normalBar.setVisibility(View.VISIBLE);
				editBar.setVisibility(View.GONE);
				isSelectedMode = false;
				selectedImages.clear();
				adapter.notifyDataSetInvalidated();
			}
		});
		
		GridView grid = (GridView) findViewById(R.id.grid_view);
		grid.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(isSelectedMode){
					ImageView imageSelected = (ImageView) arg1.findViewById(R.id.image_selected);
					if(selectedImages.contains(arg2)) {
						imageSelected.setVisibility(View.GONE);
						selectedImages.remove(arg2);
					}
					else {
						imageSelected.setVisibility(View.VISIBLE);
						selectedImages.add(arg2);
					}
				}
			}
		});
		grid.setOnScrollListener(new SpeedDetectorOnScrollListener());
		try{
			if(mDirectroy != null && !"".equals(mDirectroy)){
				String[] imgs = getImages(mDirectroy);
				images.addAll(Arrays.asList(imgs));
			}
		}catch(Exception ex){
			LOG.e(this, ex);
		}
		adapter = new ClassAdapter();
		grid.setAdapter(adapter);
	}
	
	private  String[] getImages(String dir){
		File directroy = new File(dir);
		if(directroy.exists()){
			return directroy.list();
		}
		return null;
	}
	
	private String generateAbsolute(String dir, String file, boolean isPrefix){
		if(isPrefix) return String.format("file://%s%s%s", dir, "//", file);
		else return String.format("//%s%s%s", dir, "//", file);
	}
	
	private class ClassAdapter extends BaseAdapter {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
			    .showStubImage(R.drawable.ic_launcher)
			    .showImageForEmptyUri(R.drawable.ic_launcher)
			    .bitmapConfig(Bitmap.Config.RGB_565)
				.cacheInMemory().cacheOnDisc().build();
		
		private static final int GRID_COLUME_NUMBER = 3;
		private int mItemWidth = DISPLAY_SIZE.getWidth() / GRID_COLUME_NUMBER - (int)Utils.Densitys.dip2px(LocalFolderActivity.this, 5);
		private int mItemHeight = mItemWidth;
		
		@Override
		public int getCount() {
			return images.size();
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
				item = (View) getLayoutInflater().inflate(R.layout.item_image_local_folder, parent, false);
				item.setLayoutParams(new AbsListView.LayoutParams(mItemWidth, mItemHeight));
			} else {
				item = (View) convertView;
			}
			final ImageView image = (ImageView) item.findViewById(R.id.image);
			ImageView imageSelected = (ImageView) item.findViewById(R.id.image_selected);
			if(selectedImages.contains(position)) {
				imageSelected.setVisibility(View.VISIBLE);
			}
			else {
				imageSelected.setVisibility(View.GONE);
			}
			
			String img = images.get(position);
			ImageSize loadSize = Utils.Images.getImageSizeScaleTo(image);
			String uri = generateAbsolute(mDirectroy, img, true);
			LOG.i(LocalFolderActivity.this, String.format(MESSAGE_LOAD_IMAGE, uri, loadSize.getWidth(), loadSize.getHeight()));
			imageLoader.displayImage(uri, image, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(Bitmap loadedImage) {
					Animation anim = AnimationUtils.loadAnimation(LocalFolderActivity.this, R.anim.fade_in);
					image.setAnimation(anim);
					anim.start();
				}
			}, null, loadSize);
			return item;
		}
	}
	
	private HashMap<String, Integer> getSelectedImages(){
		HashMap<String, Integer> values = new LinkedHashMap<String, Integer>();
		Iterator<Integer> it = selectedImages.iterator();
		int i = 0;
		int index;
		while (it.hasNext()) {
			index = it.next();
			values.put(generateAbsolute(mDirectroy, images.get(index), false), index);
			i++;
		}
		return values;
	}
	
	private class DeleteTask extends AsyncTask<HashMap<String, Integer>, Integer, Integer>{
		ProgressDialog dialog;
		Handler handler;

		public DeleteTask(ProgressDialog dialog, Handler handler) {
			super();
			this.dialog = dialog;
			this.handler = handler;
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			dialog.dismiss();
			selectedImages.clear();
			adapter.notifyDataSetInvalidated();
		}

		@Override
		protected Integer doInBackground(HashMap<String, Integer>... params) {
			HashMap<String, Integer> deletes = params[0];
			File file = null;
			int i = 0;
			if (deletes != null) {
				Iterator<String> it = deletes.keySet().iterator();
				String temp;
				
				while(it.hasNext()){
					temp = it.next();
					file = new File(temp);
					if (file.exists()) {
						file.delete();
						LOG.i(this, String.format(DELETEING_IMAGE, i, temp));
					}
					String where = MediaStore.Images.Media.DATA + "='" + file.getAbsolutePath() + "'";
					LocalFolderActivity.this.getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, where, null);
					
					this.onProgressUpdate(i);
					i++;
				}
			}
			LOG.i(this, String.format(DELETE_IMAGES, i));
			
			String[] imgs = getImages(mDirectroy);
			try{
				images.clear();
				images.addAll(Arrays.asList(imgs));
			}catch(Exception ex){
				LOG.e(this, ex);
			}
			return i;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			final int value = values[0];
			handler.post(new Runnable() {
				@Override
				public void run() {
					dialog.setProgress(value);
					dialog.updateContent(String.format(DELETEING_IMAGE_DIALOG, value, selectedImages.size()));
				}
			});
		}
		
	};
}
