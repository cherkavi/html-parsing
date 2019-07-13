package com.cherkashyn.vitalii.whore.imagesaver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.cherkashyn.vitalii.whore.exceptions.StorageException;
import com.cherkashyn.vitalii.whore.interfaces.imagesaver.ImageSaver;

public class ImageSaverDisk implements ImageSaver{

	private final String destinationFolder;
	private int index;
	
	public ImageSaverDisk(){
		this("/tmp/");
	}
	
	public ImageSaverDisk(String path){
		this(new File(path));
	}
	
	public ImageSaverDisk(File directory){
		if(!directory.exists()){
			throw new IllegalArgumentException("path should exists, check path: "+directory.getAbsolutePath());
		}
		if(!directory.isDirectory()){
			throw new IllegalArgumentException("path should be a directory: "+directory.getAbsolutePath());
		}
		
		this.destinationFolder=correctFolderPath(directory);
		File destinationFolderFile=new File(this.destinationFolder);
		index=destinationFolderFile.list().length;
	}
	
	private String correctFolderPath(File directory) {
		String returnValue=StringUtils.trimToEmpty(directory.getAbsolutePath());
		if(!StringUtils.endsWith(returnValue, File.separator)){
			returnValue=returnValue+File.separator;
		}
		return returnValue;
	}

	@Override
	public String saveFile(File fileSource) throws StorageException {
		File fileDestination=createDestinationFile(createUniqueId());
		InputStream input=null;
		OutputStream output=null;
		try{
			input=new FileInputStream(fileSource);
			output=new FileOutputStream(fileDestination);
			IOUtils.copy(input, output);
			output.flush();
		}catch(IOException ex){
			throw new StorageException(MessageFormat.format("can''t copy from file:{0} to file:{1}",fileSource.getAbsolutePath(), fileDestination.getAbsolutePath()));
		}finally{
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
		return fileDestination.getAbsolutePath();
	}
	
	/**
	 * create destination file  
	 * @param newFileName
	 * @return
	 */
	private File createDestinationFile(String newFileName){
		return new File(this.destinationFolder+newFileName);
	}
	
	/**
	 * TODO need to change stub logic 
	 * @param fileSource - remove logic to unique ID 
	 * @return
	 */
	private String createUniqueId(){
		return (++index)+"_"+Integer.toString(new Random().nextInt(1000));
	}

	private final static String EXTENSION_DELIMITER=".";
	
	private String getSuffix(String url){
		String extension=StringUtils.substringAfterLast(url, EXTENSION_DELIMITER);
		if(extension!=null && extension.length()<5){
			return EXTENSION_DELIMITER+extension;
		}
		return StringUtils.EMPTY;
	}
	
	@Override
	public String saveFromHttp(String url) throws StorageException {
		File fileDestination=createDestinationFile(createUniqueId()+getSuffix(url));
		InputStream input=null;
		OutputStream output=null;
		try{
			URL remoteResource = new URL(url);
			input = remoteResource.openStream();
			output=new FileOutputStream(fileDestination);
			IOUtils.copy(input, output);
			output.flush();
		}catch(IOException ex){
			throw new StorageException(MessageFormat.format("can''t copy from url:{0} to file:{1}",url, fileDestination.getAbsolutePath()));
		}finally{
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
		return fileDestination.getAbsolutePath();
	}

}
