package com.wallpaper.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Images {
	private List<Image> list = new LinkedList<Image>();
	
	public void add(Image img){
		list.add(img);
	}
	
	public void add(Images imgs){
		list.addAll(imgs.getAll());
	}
	
	public Image get(int index){
		return list.get(index);
	}
	
	public List<Image> getAll(){
		return list;
	}
	
	public int size(){
		return list.size();
	}

	@Override
	public String toString() {
		return Arrays.toString(list.toArray());
	}
	
}
