package com.wallpaper.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Covers {
	private List<Cover> list = new LinkedList<Cover>();
	
	public void add(Cover cover) {
		list.add(cover);
	}

	public void add(Covers covers) {
		list.addAll(covers.getAll());
	}

	public Cover get(int index) {
		return list.get(index);
	}

	public List<Cover> getAll() {
		return list;
	}

	public int size() {
		return list.size();
	}
	
	public Cover[] sort(){
		Cover[] result = new Cover[list.size()];
		list.toArray(result);
		Arrays.sort(result);
		return result;
	}

	@Override
	public String toString() {
		return Arrays.toString(list.toArray());
	}
}
