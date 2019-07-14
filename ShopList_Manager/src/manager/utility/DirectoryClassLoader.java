package manager.utility;

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import shop_list.html.parser.engine.IManager;

/** объект, который просматривает указанный каталог на наличие  в нем классов, потомков {@link IManager} */
public class DirectoryClassLoader {
	private String pathToDirectory;
	private String directorySeparator=System.getProperty("file.separator");
	private String packageName;

	public static void main(String[] args) throws Exception {
		System.out.println("begin");
		DirectoryClassLoader loader=new DirectoryClassLoader("shops","D:\\eclipse_workspace\\TempParser\\bin\\");
		ArrayList<IManager> listOfClass=loader.getAllParsers();
		for(int counter=0;counter<listOfClass.size();counter++){
			System.out.println(counter+" = "+listOfClass.get(counter).getShopUrlStartPage());
		}
		System.out.println("-end-");
	}
	
	
	/** получить список всех доступных парсеров */
	public static HashMap<String,String> getAvailableShopListStartPage(String pathToDirectory){
		try{
			DirectoryClassLoader loader=new DirectoryClassLoader("shops",pathToDirectory);
			ArrayList<IManager> list=loader.getAllParsers();
			HashMap<String, String> returnValue=new HashMap<String, String>();
			for(int counter=0;counter<list.size();counter++){
				returnValue.put(list.get(counter).getClass().getName(),list.get(counter).getShopUrlStartPage());
			}
			return returnValue;
		}catch(Exception ex){
			System.err.println("getAvailableShopList Exception: "+ex.getMessage());
			return null;
		}
	}
	
	public static ArrayList<IManager> getAvailableShopListParser(String pathToDirectory){
		try{
			DirectoryClassLoader loader=new DirectoryClassLoader("shops",pathToDirectory);
			return loader.getAllParsers();
		}catch(Exception ex){
			System.err.println("getAvailableShopList Exception: "+ex.getMessage());
			return null;
		}
	}
	
	
	/** объект, который просматривает указанный каталог на наличие  в нем классов, потомков {@link IManager}
	 * @param packageName - полное им€ пакета, которому принадлежат загружаемые файлы  
	 * @param pathToDirectory - полный путь к каталогу, в котором наход€тс€ классы-парсеры
	 * */
	public DirectoryClassLoader(String packageName, String pathToDirectory){
		if(pathToDirectory!=null){
			this.pathToDirectory=pathToDirectory.trim();
		}else{
			this.pathToDirectory="";
		}
		
		if(!this.pathToDirectory.endsWith(directorySeparator)){
			this.pathToDirectory=this.pathToDirectory+directorySeparator;
		}
		this.packageName=packageName;
		
		correctDirectoryName();
	}
	
	/** проверить указанный каталог, и если им€ пакета соответствует последней части указанного каталога - вырезать его */
	private void correctDirectoryName(){
		this.pathToDirectory=this.pathToDirectory.replaceAll("\\\\", "/");
		String tempPackageName=this.packageName.replaceAll("\\.", "/");
		if(tempPackageName!=null){
			if(this.pathToDirectory.endsWith(tempPackageName+"/")){
				this.pathToDirectory=this.pathToDirectory.substring(0, this.pathToDirectory.length()-tempPackageName.length()-1);
			}
		}
	}
	
	/** получить все парсеры из указанного каталога,
	 * <br> парсеры отсортированы в алфавитном пор€дке  
	 * @throws MalformedURLException */
	public ArrayList<IManager> getAllParsers() throws MalformedURLException {
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
			// URL[] urls=new URL[]{new URL("file:///"+(this.pathToDirectory+this.directorySeparator+this.packageName.replaceAll("\\.", "\\/")+this.directorySeparator).replaceAll("\\\\","/"))};
			URL[] urls=new URL[]{new URL("file:///"+(this.pathToDirectory).replaceAll("\\\\","/"))};
			return new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
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
			if(isInnerClass(className))return null;
			/*
			ArrayList<String> additionClassNames=getInnerClasses(classLoader, file);
			for(int counter=0;counter<additionClassNames.size();counter++){
				try{
					classLoader.loadClass(this.packageName+"."+additionClassNames.get(counter));
				}catch(Exception ex){
					System.err.println("DirectoryClassLoader#getIManagerFromFile: Load AdditionClass "+additionClassNames.get(counter)+"  Exception: "+ex.getMessage());
				}
			}
			*/
			Class<?> clazz=classLoader.loadClass(this.packageName+"."+className);
			Object tempObject=clazz.newInstance();
			/* Class parentClass=clazz;
			while( (parentClass=clazz.getSuperclass())!=null ){
				System.out.println(parentClass.getName());
				Class[] interfaces=parentClass.getInterfaces();
				if(interfaces!=null){
					for(int counter=0;counter<interfaces.length;counter++){
						System.out.println("I: "+interfaces[counter].getName());
					}
				}
				clazz=parentClass;
			}*/
			if(tempObject instanceof IManager){
				returnValue=(IManager)tempObject;
			}else{
				returnValue=null;
			}
		}catch(Exception ex){
			System.err.println("DirectoryClassLoader#getIManagerFromFile: "+ex.getMessage());
		}
		return returnValue;
	}
	
	private boolean isInnerClass(String className){
		return className.indexOf('$')>0;
	}

	/** проверить каталог, которому принадлежит данный файл на наличие в нем внутренних классов, если найдены - загрузить
	 * <br>
	 * например:
	 *  _5ok_com_ua$CurrentSession.class
	 *	_5ok_com_ua.class
	 * @param 
	 * @param 
	 */
	@SuppressWarnings("unused")
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
			String searchingDirectory=this.pathToDirectory+this.packageName.replaceAll("\\.","\\/")+this.directorySeparator;
			File file=new File(searchingDirectory);
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
