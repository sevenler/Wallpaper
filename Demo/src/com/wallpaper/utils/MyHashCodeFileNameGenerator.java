package com.wallpaper.utils;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;

public class MyHashCodeFileNameGenerator implements FileNameGenerator {
	@Override
	public String generate(String imageUri, ImageSize imageSize) {
		if(imageUri.contains("src=")){
			imageUri = imageUri.split("src=")[1];
		}
		if(imageUri.contains("u=")){
			imageUri = imageUri.split("u=")[1];
			imageUri = imageUri.split("&")[0];
		}
		return MemoryCacheUtil.generateKey(String.valueOf(imageUri), imageSize);
	}
}
