package com.wallpaper.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore.Images.Media;
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
		
		public static final int BUFFER_SIZE = 8192;
		
		public static boolean saveImageOnDisc(Bitmap bmp, File targetFile, Context ctx) throws IOException, URISyntaxException {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(targetFile), BUFFER_SIZE);
			boolean compressedSuccessfully = bmp.compress(Bitmap.CompressFormat.JPEG, 100, os);
			
			//保存到media数据库
			ContentValues values = new ContentValues();
			values.put(Media.DATA, targetFile.getAbsolutePath());
			values.put(Media.MIME_TYPE, "image/jpg");
			values.put(Media.DATE_TAKEN, System.currentTimeMillis());
			ctx.getContentResolver().insert(Media.EXTERNAL_CONTENT_URI, values);
			
			return compressedSuccessfully;
		}
	}
	
	public static class Densitys {

		/**
		 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
		 */
		public static float dip2px(Context context, float dpValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return dpValue * scale + 0.5f;
		}
		
		/**
		 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
		 */
		public static float dip2px(float scale, float dpValue) {
			return dpValue * scale + 0.5f;
		}

		/**
		 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
		 */
		public static float px2dip(Context context, float pxValue) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return pxValue / scale + 0.5f;
		}
	}
	
}
