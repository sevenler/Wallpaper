package com.wallpaper.task;

import java.io.IOException;
import java.net.URI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.wallpaper.Const;
import com.wallpaper.model.Cover;
import com.wallpaper.model.Covers;
import com.wallpaper.utils.LOG;
import com.wallpaper.utils.MyURLConnectionImageDownloader;

public class CoverListUpdateTask extends Thread {
	private Covers covers = new Covers();
	private Covers banners = new Covers();

	private static final String MESSAGE_LOAD_URL = "update cover list from %s";
	private static final String MESSAGE_GET_IMAGES = "get %s covers {%s} , %s banners {%s}";

	public CoverListUpdateTask() {
		super();
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
			onProgresssListenner.onFinish(banners, covers);
		}
	}

	private JSONArray getCategory() throws JSONException, IOException {
		LOG.i(this, String.format(MESSAGE_LOAD_URL, Const.FORMAT.HOME_COVERS_LIST_URL));
		MyURLConnectionImageDownloader downloader = new MyURLConnectionImageDownloader();
		String result = downloader.getStringFromNetwork(URI.create(Const.FORMAT.HOME_COVERS_LIST_URL));
		if (result == null)
			return null;
		JSONObject jsonObject = new JSONObject(result);
		return jsonObject.getJSONArray("data");
	}

	private void archiveCategory(JSONArray categoryJSONArray) throws JSONException {
		int length = categoryJSONArray.length();
		JSONObject image;
		Cover temp;
		for (int i = 0; i < length; i++) {
			image = categoryJSONArray.getJSONObject(i);

			temp = new Cover();
			temp.setSource(image.optString("objurl"));
			if (image.optString("iscover", null) != null)
				temp.setType(Cover.TYPE_COVER);
			else if (image.optString("isbanner", null) != null)
				temp.setType(Cover.TYPE_BANNER);
			temp.setName(image.optString("tagname"));

			temp.setWidth(image.optInt("width", -1));
			temp.setHeight(image.optInt("height", -1));
			temp.setSize(image.optInt("filesize", -1));
			temp.setTag(image.optInt("tagno", -1));
			temp.setIndex(image.optInt("seq", -1));
			temp.setColor(image.optInt("color", -1));

			if (image.optInt("status", 0) == 0)
				temp.setStatus(false);
			else
				temp.setStatus(true);

			if (Cover.TYPE_BANNER.equals(temp.getType()))
				banners.add(temp);
			else if (Cover.TYPE_COVER.equals(temp.getType()))
				covers.add(temp);

		}
		LOG.i(this, String.format(MESSAGE_GET_IMAGES, covers.size(), covers.toString(), banners.size(), banners.toString()));
	}

	public CoverListUpdateTask setOnProgressListenner(OnProgressListenner onProgresssListenner) {
		this.onProgresssListenner = onProgresssListenner;
		return this;
	}

	private OnProgressListenner onProgresssListenner = new OnProgressListenner() {

		/**
		 * 处理结束回调函数
		 * 
		 * @param result
		 *            [0] Covers
		 */
		@Override
		public void onFinish(Object... result) {
		}
	};

}
