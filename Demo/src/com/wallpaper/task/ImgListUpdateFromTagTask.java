package com.wallpaper.task;

import java.io.IOException;
import java.net.URI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.wallpaper.Const;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.MyURLConnectionImageDownloader;

public class ImgListUpdateFromTagTask extends Thread {
	
	private static final String MESSAGE_LOAD_URL = "update class from %s";
	
	private String[] imgUrls = null;
	private String[] imgSmallUrls = null;
	private String[] indexs = null;

	private int skip = 0;
	private int limit = 10;
	private String tag;
	private ImageSize size;

	public int getSkip() {
		return skip;
	}

	public void setSkip(int skip) {
		this.skip = skip;
	}

	public ImgListUpdateFromTagTask(String tag, ImageSize size, int skip, int limit) {
		super();
		this.tag = tag;
		this.size = size;
		this.skip = skip;
		this.limit = limit;
	}

	@Override
	public void run() {
		super.run();
		JSONArray json;
		try {
			json = getCategory();
			if (json != null) {
				archiveCategory(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			onProgresssListenner.onFinish(imgUrls, imgSmallUrls, indexs);
		}
	}

	private JSONArray getCategory() throws JSONException, IOException {
		String url = String.format(Const.FORMAT.CLASSES_ARGUMENT, tag, skip, size.getWidth(),size.getHeight(), limit, System.currentTimeMillis());
		if (LOG.isLoggindAble) LOG.i(this, String.format(MESSAGE_LOAD_URL, url));
		MyURLConnectionImageDownloader downloader = new MyURLConnectionImageDownloader();
		String result = downloader.getStringFromNetwork(URI.create(url));
		if (result == null) return null;
		JSONObject jsonObject = new JSONObject(result);
		return jsonObject.getJSONArray("data");
	}

	private void archiveCategory(JSONArray categoryJSONArray) throws JSONException  {
		int length = categoryJSONArray.length();
		imgUrls = new String[length];
		imgSmallUrls = new String[length];
		indexs = new String[length];
		JSONObject image;
		for (int i = 0; i < length; i++) {
			image = categoryJSONArray.getJSONObject(i);
			indexs[i] = skip + i + "";
			imgUrls[i] = image.optString("objurl");
			imgSmallUrls[i] = image.optString("smallImageUrl");
		}
	}

	public ImgListUpdateFromTagTask setOnProgressListenner(OnProgressListenner onProgresssListenner) {
		this.onProgresssListenner = onProgresssListenner;
		return this;
	}

	private OnProgressListenner onProgresssListenner = new OnProgressListenner() {
		
		/**
		 * 处理结束回调函数
		 * @param result[0] 图片url数组 类型：String[]
		 * @param result[1] 图片对应小图片url数组 类型：String[]
		 * @param result[2] 图片序号数组 类型：String[]
		 */
		@Override
		public void onFinish(Object... result) {
		}
	};

}
