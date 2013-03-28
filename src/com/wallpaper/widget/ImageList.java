package com.wallpaper.widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.net.Uri;

public class ImageList implements IImageList{
	
	List<IImage> mImageList;

	public ImageList(int size) {
		super();
		mImageList = new ArrayList<IImage>(size);
	}
	
	public void add(IImage image){
		mImageList.add(image);
	}

	@Override
	public HashMap<String, String> getBucketIds() {
		return null;
	}

	@Override
	public int getCount() {
		return mImageList.size();
	}

	@Override
	public boolean isEmpty() {
		return (mImageList == null) || (mImageList.size() == 0);
	}

	@Override
	public IImage getImageAt(int i) {
		return mImageList.get(i);
	}

	@Override
	public IImage getImageForUri(Uri uri) {
		return null;
	}

	@Override
	public boolean removeImage(IImage image) {
		return mImageList.remove(image);
	}

	@Override
	public boolean removeImageAt(int i) {
		if(mImageList.get(i) == null) return false;
		return mImageList.remove(mImageList.get(i));
	}

	@Override
	public int getImageIndex(IImage image) {
		return mImageList.indexOf(image);
	}

	@Override
	public void close() {
	}

}
