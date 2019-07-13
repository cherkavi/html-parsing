package shops;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.csvreader.CsvReader;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.MultiFileParseManager;
import shop_list.html.parser.engine.record.Record;

public class _2000_com_ua extends MultiFileParseManager<CsvReader, String[]>{

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.csv;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.2000.com.ua/uploads/temp/price.csv"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.2000.com.ua";
	}

	@Override
	protected CsvReader openFile(String string) throws IOException{
		CsvReader reader=new CsvReader(new FileInputStream(new File(string)), this.getFileCharset());
		reader.setDelimiter(';');
		return reader;
 	}

	@Override
	protected Charset getFileCharset(){
		return Charset.forName("windows-1251");
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		if((record!=null)&&(record.length>0)){
			return record[0];
		}else{
			return null;
		}
	}

	@Override
	protected String[] getNextRecord(CsvReader reader) {
		try{
			return reader.getValues();
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected boolean fileHasRecord(CsvReader reader) {
		try{
			return reader.readRecord();
		}catch(Exception ex){
			return false;
		}
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		if((record!=null)&&(record.length>5)){
			String name=record[1]+" "+record[2];
			String description=record[3];
			Float priceUsd=this.getFloatFromString(record[4]);
			if((priceUsd==null)||(priceUsd.floatValue()==0)){
				return null;
			}
			String url=record[8];
			return new Record(name,
							  description, 
							  removeStartPage(url), 
							  null, 
							  priceUsd, 
							  null);
		}else{
			// запись не может быть создана 
			return null;
		}
	}
	
}
