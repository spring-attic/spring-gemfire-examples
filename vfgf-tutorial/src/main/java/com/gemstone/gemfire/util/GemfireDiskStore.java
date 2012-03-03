package com.gemstone.gemfire.util;

import java.io.File;

import com.gemstone.gemfire.cache.Cache;
import com.gemstone.gemfire.cache.DiskStore;

public class GemfireDiskStore {
	private Cache cache;
	private String diskStoreName;
	private File baseDir;
	private DiskStore store;
	
	public void init() {
		store = cache.createDiskStoreFactory().setDiskDirs(new File[] {baseDir}).create(getDiskStoreName());
	}
	
	public Cache getCache() {
		return cache;
	}
	public void setCache(Cache cache) {
		this.cache = cache;
	}
	public String getDiskStoreName() {
		return diskStoreName;
	}
	public void setDiskStoreName(String diskStoreName) {
		this.diskStoreName = diskStoreName;
	}
	public File getBaseDir() {
		return baseDir;
	}
	public void setBaseDir(File baseDir) {
		this.baseDir = baseDir;
	}

	public DiskStore getStore() {
		return store;
	}

	public void setStore(DiskStore store) {
		this.store = store;
	}
	
	
	
	
}
