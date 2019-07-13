package shop_list.html.parser.engine.file_parser;

import java.io.File;

import java.io.IOException;
import java.nio.charset.Charset;

import com.csvreader.CsvReader;

import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.exception.EParseExceptionEmptyRecord;
import shop_list.html.parser.engine.exception.EParseExceptionNotInStore;
import shop_list.html.parser.engine.file_parser.FileParserManager;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.record.Record;

public abstract class MultiFileParseManager<FileType, RecordType> extends FileParserManager{

	/** 
	 * ��������� �� ������������ ������������� �������� ������ �� ������������� 
	 * @param original - ������������ ��������
	 * @param controlValues - ��������, ������� ����� ��������� �� ������������ 
	 * */
	protected boolean isStringEqualsAny(String original, String ... controlValues) {
		if(original==null)return false;
		if((controlValues==null)||(controlValues.length==0))return true;
		boolean returnValue=false;
		String newValue=original.trim();
		for(int counter=0;counter<controlValues.length;counter++){
			if(controlValues[counter]!=null)
			if(newValue.equalsIgnoreCase(controlValues[counter].trim())){
				returnValue=true;
				break;
			}
		}
		return returnValue;
	}
	

	/** �������� �� ������ ������ */
	protected boolean isStringEmpty(String value){
		return (value==null)||(value.trim().equals(""));
	}

	
	@Override
	protected boolean parseFiles(Integer sessionId, 
								 String[] fullPathToFile,
								 EFileTypes priceFileType) throws Exception {
		for(int counter=0;counter<fullPathToFile.length;counter++){
			if(fullPathToFile[counter]!=null){
				FileType object=openFile(fullPathToFile[counter]);
				// ����������� �� ���� ������� �����
				while(fileHasRecord(object)){
					// �������� ��������� ������
					RecordType record=getNextRecord(object);
					// ���������� ��������� ������
					if(processNextRecord(record)==false)break;
				}
				// ������� �����
				closeFile(object);
				// ������� ����
				deleteFile(fullPathToFile[counter]);
				// Record record=new Record(name, null, url, null, priceUSD, null);
				// this.saver.saveRecord(sessionId, currentSection, record);
			}else{
				this.logger.error(this, "null path to local file ("+counter+"/"+fullPathToFile.length+")");
			}
		}
		return true;
	}

	/** �������� �� ������� ��������� ������ ��� ������� � ���������� ( ������ ������ ������������, �.�. ��� ������ ����� {@link #fileHasRecord(Object)} */
	protected abstract RecordType getNextRecord(FileType object);

	/** ������� ��������� ����  
	 * @param ������ ���� � ����� 
	 * */
	protected void deleteFile(String pathToFile) {
		try{
			File file=new File(pathToFile);
			if(file.delete()==false){
				file.deleteOnExit();
			}
		}catch(Exception ex){};
	}

	/** ������ �� ����� ��������� - ������� ���� 
	 * @return 
	 * <ul>
	 * 	<li><b>true</b> - ���� ������� ������</li>
	 * 	<li><b>false</b> - ������ �������� �����</li>
	 * </ul>
	 * */
	protected boolean closeFile(FileType object) throws Exception{
		while(true){
			if(object instanceof CsvReader){
				try{
					((CsvReader)object).close();
					return true;
				}catch(Exception ex){
					return false;
				}
			}
			throw new Exception("check close File ");
		}
	}

	/** ��������� ������, ���������� �� ����� */
	protected void saveRecord(Integer currentSectionKod, Record record) throws Exception{
		if(this.saver.saveRecord(this.getSessionId(),currentSectionKod, record)==false){
			logger.error(this,"Record Save Exception:"+record.toString());
		}
	}

	/** �������� �����������-���������� ���-�� "������" �������, ����� ������� ������� ����� ����� ����������
	 * <ul>
	 * 	<li><b>0</b> - default value </li>
	 * 	<li><b>>0</b> - ���-�� ��������� ����� ������� ������ ����������� </li>
	 * </ul>
	 *  */
	protected int getMaxEmptyRecord(){
		return 0;
	}
	
	private int errorSaveCounter=0;
	
	/** ���������, ����������� �� ����� ������ � ����� ������  */
	protected boolean processNextRecord(RecordType record){
		try{
			// ��������� �� ������� ����� ������ 
			this.checkForNewSection(record);
			Record saveRecord=null;
			try{
				saveRecord=this.getSaveRecord(record);
				if(this.getMaxEmptyRecord()>0)errorSaveCounter=0;
			}catch(EParseExceptionEmptyRecord empty){
				logger.debug(this, "��������� ������ ��������� ������");
				if(this.getMaxEmptyRecord()>0){
					errorSaveCounter++;
					if(errorSaveCounter>this.getMaxEmptyRecord()){
						return false;
					}
				}
			}catch(EParseExceptionNotInStore notInStore){
			}catch(EParseException eParse){
			}
			
			if(saveRecord==null){
				logger.debug(this, "��� ������ ��� ����������");
			}else{
				// ��������� ������
				try{
					if((saveRecord.getPrice()==null)&&(saveRecord.getPriceUsd()==null)&&(saveRecord.getPriceEuro()==null)){
						// ������ �� ����� ���� ��������� - ��� ���� �� �����
					}else{
						this.saveRecord(currentSectionKod, saveRecord);
					}
				}catch(Exception ex){
					logger.error(this, "#processNextRecord Save record Exception:"+ex.getMessage());
				}
			}
			return true;
		}catch(Exception ex){
			logger.error(this, "#processNextRecord Exception:"+ex.getMessage());
			return false;
		}
	}

	/** �������� Float �� ��������� ������  */
	protected Float getFloatFromString(String value){
		if(value==null)return null;
		String tempValue=value.trim();
		try{
			return Float.parseFloat(tempValue);
		}catch(Exception ex){
			// ��������, ����� �������� ����� �� �������
			try{
				return Float.parseFloat(tempValue.replaceAll(",", "."));
			}catch(Exception exInner){
				return null;
			}
			
		}
	}
	
	/** �������� ������ ��� ���������� �� ��������� ������ �� �����
	 * ���� �� ������� null, ���� ����� ������ �� ����� ���� ������� 
	 * @throws ���������� ��� ���� � ������������� ��������� ������ ( ����� ���� ������ Exception ) 
	 * <ul>
	 * 	<il><b>EParseExceptionEmptyRecord</b> - ������ ������, ����� ���� ������������� ��� ���� ��� ����������� ���-�� � ������ ������������� ������ {@link #getMaxEmptyRecord()}</il>
	 * 	<il><b>EParseExceptionNotInStore</b> - ������ ��� �� ������ </il>
	 * </ul>
	 * */
	protected abstract Record getSaveRecord(RecordType record) throws EParseException;

	/** ������� ��� ������ */
	private Integer currentSectionKod=null;
	/** ������� ��� ������ */
	private String currentSectionName=null;
	
	/** 
	 * �� ��������� ������ ������� ��� ������ ��� null, ���� �� ������� ���������������� ������ ��� ������
	 * <br>
	 * ��� Excel ������, ������������ ������ ���������� ���������� ( equals ) ��������  
	 * */
	protected abstract String getSectionNameFromRecord(RecordType record);
	
	/** ��������� ��������� ������ �� ������� �� ��� ����� ������ */
	protected void checkForNewSection(RecordType record){
		String sectionName=null;
		try{
			sectionName=getSectionNameFromRecord(record);
		}catch(Exception ex){};
		if(sectionName!=null){
			if(currentSectionName==null){
				// ������ ��� �� ���� ���������
				currentSectionKod=this.saver.getSectionId(sectionName);
				this.currentSectionName=sectionName;
				logger.debug(this, "Section: "+sectionName);
			}else{
				if(sectionName!=null){
					if(!sectionName.equals(currentSectionName)){
						currentSectionKod=this.saver.getSectionId(sectionName);
						this.currentSectionName=sectionName;
						logger.debug(this, "Section: "+sectionName);
					}
				}
			}
		}
	}

	/** ����� �� ���� ��������� ������ ��� ���������
	 * <ul>
	 * 	<li><b>true</b> - ��, ���� ������, ������� ����� ���������� </li>
	 * 	<li><b>false</b> - ���, ������� ��� ��������� ��� </li>
	 * </ul>  
	 * */
	protected abstract boolean fileHasRecord(FileType object); 

	/** ������� �� ������ URL ������ */
	protected String removeStartPage(String url){
		String source=this.getShopUrlStartPage().replaceAll("^(http://)", "");
		source=source.replaceAll("^(www.)", "");
		int index=url.indexOf(source);
		if(index>=0){
			try{
				return url.substring(index+source.length());
			}catch(Exception ex){
				return url;
			}
		}else{
			return url;
		}
	}
	
	public static void main(String[] args){
		System.out.println("begin");
		String source="111_http://www.electrovoz.com.ua";
		source=source.replaceAll("^(http://)", "");
		source=source.replaceAll("^(www.)", "");
		System.out.println(source);
		System.out.println("end");
	}
	
	/** ������� ���� � �������� �������� ���� � �������� ������� */
	protected abstract FileType openFile(String string) throws IOException; 

	/**<ul>
	 *  <li>	KOI8-U	</li>
		<li>	ISO-8859-1	</li>
		<li>	UTF-8	</li>
		<li>	UTF-16	</li>
		<li>	windows-1250	</li>
		<li>	windows-1251	</li>
		<li>	windows-1252	</li>
		<li>	windows-1253	</li>
		<li>	windows-1254	</li>
		<li>	windows-1255	</li>
		<li>	windows-1256	</li>
		<li>	windows-1257	</li>
		<li>	windows-1258	</li>
		<li>	ISO-8859-13	</li>
		<li>	ISO-8859-15	</li>
		<li>	ISO-8859-2	</li>
		<li>	ISO-8859-3	</li>
		<li>	ISO-8859-4	</li>
		<li>	ISO-8859-5	</li>
		<li>	ISO-8859-6	</li>
		<li>	ISO-8859-7	</li>
		<li>	ISO-8859-8	</li>
		<li>	ISO-8859-9	</li>
		<li>	US-ASCII	</li>
		<li>	UTF-16BE	</li>
		<li>	UTF-16LE	</li>
		<li>	UTF-32	</li>
		<li>	UTF-32BE	</li>
		<li>	UTF-32LE	</li>
		<li>	x-MacUkraine	</li>
		<li>	x-MacCyrillic	</li>
		</ul>
	 */
	protected abstract Charset getFileCharset();
	
}
