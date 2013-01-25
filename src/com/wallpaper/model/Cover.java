package com.wallpaper.model;

public class Cover implements Comparable<Cover>{
	public static final String TYPE_BANNER = "banner";
	public static final String TYPE_COVER = "cover";
	
	private String type;
	private String source;
	
	private String name;
	private int tag;
	
	private int width;
	private int height;
	private int index;
	@Override
	public String toString() {
		return "Cover [type=" + type + ", source=" + source + ", name=" + name + ", tag=" + tag + ", width=" + width + ", height=" + height + ", index=" + index + ", color=" + color + ", size="
				+ size + ", status=" + status + "]";
	}

	private int color;
	private int size;
	private boolean status;
	
	public Cover() {
		super();
	}
	
	public Cover(String type, String source, int width, int height, int tag, String name, int index, int color, int size, boolean status) {
		super();
		this.type = type;
		this.source = source;
		this.width = width;
		this.height = height;
		this.tag = tag;
		this.name = name;
		this.index = index;
		this.color = color;
		this.size = size;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public int compareTo(Cover another) {
		return this.index - another.index;
	}
}
