package shop_list.html.parser.engine.file_parser.extractor;

import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;


import shop_list.html.parser.engine.file_parser.IFileNameGenerator;

/** класс, который умеет открывать архивы  */
public class Extractor {
	
	/* разархивировать указанный файл методом Zip  
	 * @param pathToTemp - полный путь к временному каталогу 
	 * @param fileZip - полное имя файла 
	 * @return список файлов, который получены после разархивирования
	public String[] extractZip(String pathToTemp, String fileZip, IFileNameGenerator fileNameGenerator){
		ArrayList<String> list=new ArrayList<String>();
		try {
		    // Open the ZIP file
		    java.util.zip.ZipInputStream in = new java.util.zip.ZipInputStream(new FileInputStream(fileZip));
		    // walk in entry
		    byte[] buf = new byte[1024];
		    @SuppressWarnings("unused")
			java.util.zip.ZipEntry entry =null;
		    while( (entry=in.getNextEntry())!=null ){
			    // Open the output file
			    String outFilename = fileNameGenerator.generateUniqueFileName();
			    list.add(pathToTemp+outFilename);
			    OutputStream out = new FileOutputStream(pathToTemp+outFilename);
			    // Transfer bytes from the ZIP file to the output file
			    int len;
			    while ((len = in.read(buf)) > 0) {
			        out.write(buf, 0, len);
			    }
			    // Close the streams
			    out.close();
		    }
		    in.close();
		} catch (IOException e) {
		
		}
		return list.toArray(new String[]{});
	}
	*/
	
	@SuppressWarnings("unchecked")
	public String[] extractZip(String pathToTemp, String fileZip, IFileNameGenerator fileNameGenerator){
		ArrayList<String> list=new ArrayList<String>();
		try {
		    // Open the ZIP file
		    ZipFile zip = new ZipFile(fileZip, "UTF-8");
		    // walk in entry
		    byte[] buf = new byte[1024];
		    @SuppressWarnings("unused")
			ZipEntry entry =null;
		    Enumeration enumeration=zip.getEntries();
		    while(enumeration.hasMoreElements()){
		    	InputStream in=zip.getInputStream(((ZipEntry)enumeration.nextElement()));
			    String outFilename = fileNameGenerator.generateUniqueFileName();
			    list.add(pathToTemp+outFilename);
			    OutputStream out = new FileOutputStream(pathToTemp+outFilename);
			    // Transfer bytes from the ZIP file to the output file
			    int len;
			    while ((len = in.read(buf)) > 0) {
			        out.write(buf, 0, len);
			    }
		    	out.close();
		    	in.close();
		    }
		} catch (IOException e) {
		
		}
		return list.toArray(new String[]{});
	}
	
}
