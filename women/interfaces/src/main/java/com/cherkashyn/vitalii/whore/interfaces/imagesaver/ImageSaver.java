package com.cherkashyn.vitalii.whore.interfaces.imagesaver;

import java.io.File;

import com.cherkashyn.vitalii.whore.exceptions.StorageException;

/**
 * save file to storage 
 */
public interface ImageSaver {
	/**
	 * @param file - path to file, which will use as source  
	 * @return unique id for this file 
	 */
	String saveFile(File file) throws StorageException;
	
	/**
	 * @param url - path to url, which contains this image 
	 * @return unique id of saved file 
	 */
	String saveFromHttp(String url) throws StorageException;
}
