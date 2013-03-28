package com.wallpaper.widget;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import android.graphics.Bitmap;
import android.net.Uri;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageDecoder;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.wallpaper.R;
import com.wallpaper.utils.MyURLConnectionImageDownloader;

public class Image implements IImage {

	private String source = null;
	private URI uri = null;

	public Image(String source) {
		super();
		this.source = source;
		uri = URI.create(Uri.fromFile(new File(source)).toString());
	}

	@Override
	public IImageList getContainer() {
		return null;
	}

	@Override
	public Bitmap fullSizeBitmap(int minSideLength, int maxNumberOfPixels) {
		return null;
	}

	@Override
	public Bitmap fullSizeBitmap(int minSideLength, int maxNumberOfPixels,
			boolean rotateAsNeeded, boolean useNative) {
		return null;
	}

	@Override
	public int getDegreesRotated() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public InputStream fullSizeImageData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri fullSizeImageUri() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDataPath() {
		// TODO Auto-generated method stub
		return source;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return source;
	}

	@Override
	public long getDateTaken() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMimeType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isReadonly() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDrm() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Bitmap thumbBitmap(boolean rotateAsNeeded) {
		Bitmap bit = null;
		try {
			bit = decodeBitmap(uri, MINITHUMB_SIZE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bit;
	}

	@Override
	public Bitmap miniThumbBitmap() {
		Bitmap bit = null;
		try {
			bit = decodeBitmap(uri, MINITHUMB_SIZE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return bit;
	}

	@Override
	public boolean rotateImageBy(int degrees) {
		return false;
	}

	private static ImageDownloader downloader = new MyURLConnectionImageDownloader();
	private static DisplayImageOptions options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory().cacheOnDisc()
			.build();

	private static ImageSize MINITHUMB_SIZE = new ImageSize(200, 200);

	private static Bitmap decodeBitmap(URI source, ImageSize targetSize)
			throws IOException {
		System.out.println("source:" + source.toString());
		ImageDecoder decoder = new ImageDecoder(source, downloader, options);
		return decoder.decode(targetSize, options.getImageScaleType());
	}
}
