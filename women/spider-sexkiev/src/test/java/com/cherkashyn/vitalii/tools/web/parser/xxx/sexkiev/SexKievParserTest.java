package com.cherkashyn.vitalii.tools.web.parser.xxx.sexkiev;

import java.io.File;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.cherkashyn.vitalii.tools.web.parser.action.exception.ChangedStructureException;
import com.cherkashyn.vitalii.tools.web.parser.action.exception.ReadRemoteDataException;
import com.cherkashyn.vitalii.tools.web.parser.action.page.Parser;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.GroupBlock;
import com.cherkashyn.vitalii.tools.web.parser.domain.element.Page;
import com.cherkashyn.vitalii.whore.exceptions.StorageException;
import com.cherkashyn.vitalii.whore.interfaces.imagesaver.ImageSaver;

public class SexKievParserTest {

	@Ignore
	@Test
	public void testParser() throws ReadRemoteDataException, ChangedStructureException{
		// given
		Parser parser=new SexKievParser();
		Page page=new Page(new SexKievNextUrl());
		// move to first page 
		page.getUrlNext();
		ImageSaver imageStorage=new ImageSaver(){

			@Override
			public String saveFile(File file) throws StorageException {
				System.out.println(file.getAbsolutePath());
				return file.getAbsolutePath();
			}

			@Override
			public String saveFromHttp(String url) throws StorageException {
				System.out.println(url);
				return url;
			}
			
		};
		// when
		GroupBlock block=parser.readData(page, imageStorage);
		
		// then
		Assert.assertNotNull(block);
	}

	@Test
	public void testParserNotExisting() throws ReadRemoteDataException, ChangedStructureException{
		// given
		Parser parser=new SexKievParser();
		Page page=new Page(new SexKievNextUrl());
		// move to first page 
		for(int index=0;index<100;index++){
			page.getUrlNext();
		}
		ImageSaver imageStorage=new ImageSaver(){

			@Override
			public String saveFile(File file) throws StorageException {
				System.out.println(file.getAbsolutePath());
				return file.getAbsolutePath();
			}

			@Override
			public String saveFromHttp(String url) throws StorageException {
				System.out.println(url);
				return url;
			}
			
		};
		// when
		GroupBlock block=parser.readData(page, imageStorage);
		
		// then
		Assert.assertNotNull(block);
	}

}
