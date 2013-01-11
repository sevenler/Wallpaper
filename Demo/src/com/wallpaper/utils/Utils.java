package com.wallpaper.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.DisplayMetrics;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.wallpaper.Const;

public class Utils {
	public static final class Stream {
		public static String convertStreamToString(InputStream is) throws IOException {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		}
	}

	public static final class Http {
		public static String generateThumbImageUrl(ImageSize target, String source) {
			return String.format(Const.FORMAT.THUMB_ARGUMENT, target.getWidth(), target.getHeight(), System.currentTimeMillis(), source);
		}
	}

	public static final class Files {
		public static String[] getImagsFormDir(String dir) {
			File file = new File(dir);
			String[] imags = file.list();
			for (int i = 0; i < imags.length; i++) {
				imags[i] = String.format("file://%s/%s", dir, imags[i]);
			}
			return imags;
		}
	}
	
	public static final class Images {
		public static ImageSize getImageSizeScaleTo(ImageView imageView) {
			DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();

			LayoutParams params = imageView.getLayoutParams();
			int width = params.width;
			if (width <= 0) width = displayMetrics.widthPixels;

			int height = params.height;
			if (height <= 0) height = displayMetrics.heightPixels;

			return new ImageSize(width, height);
		}
	}
}
