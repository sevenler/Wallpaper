package com.wallpaper.utils;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;

/**
 * Names image file as image URI {@linkplain String#hashCode() hashcode}
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class MyHashCodeFileNameGenerator implements FileNameGenerator {
	
	private static final String MESSAGE_LOAD_URL = "genarate name is %s";
	
	@Override
	public String generate(String imageUri) {
		if(imageUri.contains("src=")){
			imageUri = imageUri.split("src=")[1];
			
			if(imageUri.contains("u=")){
				imageUri = imageUri.split("u=")[1];
				imageUri = imageUri.split("&")[0];
			}
		}
		if (LOG.isLoggindAble) LOG.i(this, String.format(MESSAGE_LOAD_URL, imageUri));
		return String.valueOf(imageUri.hashCode());
	}
}
