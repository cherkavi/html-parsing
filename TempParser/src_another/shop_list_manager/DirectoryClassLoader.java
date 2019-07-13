package shop_list_manager;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import shop_list.html.parser.engine.IManager;

/** объект, который просматривает указанный каталог на наличие  в нем классов, потомков {@link IManager} */
public class DirectoryClassLoader {
	private String pathToDirectory;
	private String directorySeparator=System.getProperty("file.separator");
	
	/** объект, который просматривает указанный каталог на наличие  в нем классов, потомков {@link IManager} 
	 * @param pathToDirectory - полный путь к каталогу, в котором наход€тс€ классы-парсеры
	 * */
	public DirectoryClassLoader(String pathToDirectory){
		if(pathToDirectory!=null){
			this.pathToDirectory=pathToDirectory.trim();
		}else{
			this.pathToDirectory="";
		}
		
		if(!this.pathToDirectory.endsWith(directorySeparator)){
			this.pathToDirectory=this.pathToDirectory+directorySeparator;
		}
	}
	
	/** получить все парсеры из указанного каталога,
	 * <br> парсеры отсортированы в алфавитном пор€дке  */
	public ArrayList<IManager> getAllParsers(){
		ArrayList<IManager> returnValue=new ArrayList<IManager>();
		// получить все файлы каталога
		ArrayList<File> files=this.getFilesIntoDirectory();
		// получить загрузчик дл€ файлов-классов 
		ClassLoader currentClassLoader=this.getClassLoader();
		// каждый файл загрузить и проверить на наличие(возможность приведени€ ) интерфейса IManager
		for(int counter=0;counter<files.size();counter++){
			IManager currentParser=this.getIManagerFromFile(currentClassLoader, files.get(counter));
			if(currentParser!=null){
				returnValue.add(currentParser);
			}
		}
		Collections.sort(returnValue, new FileComparator());
		return returnValue;
	}
	
	/** получить загрузчик дл€ файлов-классов  */
	private ClassLoader getClassLoader(){
		try{
			// File file=new File(path);
			// URL[] urls=new URL[]{file.toURL()};
			URL[] urls=new URL[]{new URL("file:/"+this.pathToDirectory.replaceAll("\\\\","/"))};
			return new URLClassLoader(urls);
		}catch(Exception ex){
			System.err.println("getObject Exception:"+ex.getMessage());
			return null;
		}
		
	}
	
	/** загрузить файл в виде класса в текущий процесс и попытатьс€ преобразовать его к типу IManager 
	 * @param classLoader - загрузчик классов дл€ каталога {@link #pathToDirectory} 
	 * @param file - файл, который нужно загрузить
	 * @return - 
	 * <ul>
	 * 	<li>управл€ющий парсер</li>
	 * 	<li>null, если парсер не определен</li>
	 * </ul>
	 */
	private IManager getIManagerFromFile(ClassLoader classLoader, File file) {
		IManager returnValue=null;
		try{
			String className=file.getName();
			int dotPosition=className.indexOf(".class");
			className=className.substring(0,dotPosition);
			ArrayList<String> additionClassNames=getInnerClasses(classLoader, file);
			for(int counter=0;counter<additionClassNames.size();counter++){
				try{
					classLoader.loadClass("shops."+additionClassNames.get(counter));
				}catch(Exception ex){
					System.err.println("DirectoryClassLoader#getIManagerFromFile: Load AdditionClass "+additionClassNames.get(counter)+"  Exception: "+ex.getMessage());
				}
			}
			Class<?> clazz=classLoader.loadClass("shops."+className);
			returnValue=(IManager)clazz.newInstance();
		}catch(Exception ex){
			System.err.println("DirectoryClassLoader#getIManagerFromFile: "+ex.getMessage());
		}
		return returnValue;
	}

	/** проверить каталог, которому принадлежит данный файл на наличие в нем внутренних классов, если найдены - загрузить
	 * <br>
	 * например:
	 *  _5ok_com_ua$CurrentSession.class
	 *	_5ok_com_ua.class
	 * @param 
	 * @param 
	 */
	private ArrayList<String> getInnerClasses(ClassLoader classLoader, File file) {
		// получить каталог
		File directory=this.getDirectoryFromFile(file);
		// получить все файлы в каталоге filename+"$"
		File[] fileList=directory.listFiles(new InnerClassFilenameFilter(file.getName()));
		// найденные - загрузить
		ArrayList<String> classNames=new ArrayList<String>();
		if((fileList!=null)&&(fileList.length>0)){
			for(int counter=0;counter<fileList.length;counter++){
				String innerClassName=cutExtension(fileList[counter].getName());
				innerClassName=innerClassName.replaceAll("\\$", ".");
				classNames.add(innerClassName);
			}
		}
		return classNames;
	}

	/** вернуть им€ файла без разрешени€  */
	private String cutExtension(String fileName){
		int dotPoint=fileName.lastIndexOf('.');
		if(dotPoint>=0){
			return fileName.substring(0,dotPoint);
		}else{
			return fileName;
		}
	}
	
	/** получить каталог дл€ указанного файла в виде File */
	private File getDirectoryFromFile(File file){
		String fullPath=file.getAbsolutePath();
		int dotIndex=fullPath.lastIndexOf(this.directorySeparator);
		if(dotIndex>0){
			return new File(fullPath.substring(0,dotIndex+1));
		}else{
			return file;
		}
	}

	/** получить все файлы из указанного каталога  */
	private ArrayList<File> getFilesIntoDirectory(){
		ArrayList<File> returnValue=new ArrayList<File>();
		try{
			File file=new File(this.pathToDirectory);
			if(file.isDirectory()){
				File[] files=file.listFiles(new ExtFilenameFilter("class"));
				if(files!=null){
					for(int counter=0;counter<files.length;counter++){
						if(files[counter]!=null){
							returnValue.add(files[counter]);
						}
					}
				}
			}else{
				System.err.println("DirectoryClassLoader#getFilesIntoDirectory Path is not directory: "+this.pathToDirectory);
			}
		}catch(Exception ex){
			System.err.println("DirectoryClassLoader#getFilesIntoDirectory Exception: "+ex.getMessage());
		}
		return returnValue;
	}
}

/** поиск файлов-классов, которые могут  */
class InnerClassFilenameFilter implements FilenameFilter{
	private String className=null;
	public InnerClassFilenameFilter(String className){
		this.className=cutExtension(className)+"$"; 
	}
	
	@Override
	public boolean accept(File dir, String filename) {
		String realName=cutExtension(filename);
		if(realName.indexOf(className)>=0){
			return true;
		}
		return false;
	}
	
	/** вернуть им€ файла без разрешени€  */
	private String cutExtension(String fileName){
		int dotPoint=fileName.lastIndexOf('.');
		if(dotPoint>=0){
			return fileName.substring(0,dotPoint);
		}else{
			return fileName;
		}
	}
	
	
}


/** фильтр поиска файлов по расширению */
class ExtFilenameFilter implements FilenameFilter{
	private String ext;
	
	public ExtFilenameFilter(String ext){
		this.ext="."+ext;
	}
	
	
	private boolean isValidName(String fileName){
		if(fileName==null){
			return false;
		}else if(fileName.endsWith(this.ext)){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public boolean accept(File dir, String name) {
		return isValidName(name);
	}
	
}

/** сравнение двух имен файлов дл€ упор€дочивани€ набора значений */
class FileComparator implements Comparator<IManager>{

	@Override
	public int compare(IManager o1, IManager o2) {
		if((o1!=null)&&(o2!=null)){
			String name1=o1.getShopUrlStartPage();
			String name2=o2.getShopUrlStartPage();
			if(name1.equals(name2)){
				return 0;
			}else{
				if(name1.compareTo(name2)>0){
					return 1;
				}else{
					return 0;
				}
			}
		}else{
			if((o1==null)&&(o2==null)){
				return 0;
			}else{
				if(o1==null){
					return -1;
				}else{
					return 0;
				}
			}
		}
	}
	
}
