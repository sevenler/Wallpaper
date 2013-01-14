package com.wallpaper.model;

public class Image {
	private int tag;
	private String source;
	private String thumb;
	private int width;
	private int height;
	private int size;

	public Image(int tag, String source, String thumb, int width, int height, int size) {
		super();
		this.tag = tag;
		this.source = source;
		this.thumb = thumb;
		this.width = width;
		this.height = height;
		this.size = size;
	}

	@Override
	public String toString() {
		return "Image [tag=" + tag + ", source=" + source + ", thumb=" + thumb + ", width=" + width + ", height=" + height + ", size=" + size + "]";
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

}
