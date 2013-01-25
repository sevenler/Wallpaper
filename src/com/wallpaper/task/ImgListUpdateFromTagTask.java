package com.wallpaper.task;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.wallpaper.Const;
import com.wallpaper.model.Image;
import com.wallpaper.model.Images;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.MyURLConnectionImageDownloader;

public class ImgListUpdateFromTagTask extends Thread {
	private int skip, limit;
	private String tag;
	private ImageSize size;

	private Images images = new Images();
	private static final String MESSAGE_LOAD_URL = "update class from %s";
	private static final String MESSAGE_GET_IMAGES = "get %s images {%s}";
	
	private static final String DATE_STRING = new SimpleDateFormat("yyyyMMdd060440").format(new Date());

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
			LOG.e(this, e);
		} finally {
			onProgresssListenner.onFinish(images);
		}
	}

	private JSONArray getCategory() throws JSONException, IOException {
		String url = String.format(Const.FORMAT.CLASSES_ARGUMENT, tag, skip, size.getWidth(), size.getHeight(), limit, DATE_STRING);
		LOG.i(this, String.format(MESSAGE_LOAD_URL, url));
		MyURLConnectionImageDownloader downloader = new MyURLConnectionImageDownloader();
		String result = downloader.getStringFromNetwork(URI.create(url));
		if (result == null)
			return null;
		JSONObject jsonObject = new JSONObject(result);
		return jsonObject.getJSONArray("data");
	}

	private void archiveCategory(JSONArray categoryJSONArray) throws JSONException {
		int length = categoryJSONArray.length();
		JSONObject image;
		String source, thumb;
		int width, height, size, tag;
		for (int i = 0; i < length; i++) {
			image = categoryJSONArray.getJSONObject(i);
			source = image.optString("objurl");
			thumb = image.optString("smallImageUrl");
			width = image.optInt("width", -1);
			height = image.optInt("height", -1);
			size = image.optInt("filesize", -1);
			tag = image.optInt("tag", -1);

			images.add(new Image(tag, source, thumb, width, height, size));
		}
		LOG.i(this, String.format(MESSAGE_GET_IMAGES, length, images.toString()));
	}

	public ImgListUpdateFromTagTask setOnProgressListenner(OnProgressListenner onProgresssListenner) {
		this.onProgresssListenner = onProgresssListenner;
		return this;
	}

	private OnProgressListenner onProgresssListenner = new OnProgressListenner() {

		/**
		 * 处理结束回调函数
		 * 
		 * @param result
		 *            [0] Images
		 */
		@Override
		public void onFinish(Object... result) {
		}
	};

}
