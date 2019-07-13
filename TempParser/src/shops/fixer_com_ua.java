package shops;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.csvreader.CsvReader;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.MultiFileParseManager;
import shop_list.html.parser.engine.record.Record;

public class fixer_com_ua extends MultiFileParseManager<CsvReader, String[]>{

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_csv;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://fixer.com.ua/load_pricelist/"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://fixer.com.ua";
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
		if( isStringEmpty(record[0])&&(!isStringEmpty(record[1]))&&isStringEmpty(record[2]) ){
			return record[1];
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
		if((record!=null)&&(record.length>4)){
			String name=record[1];
			String description=record[5];
			Float price=this.getFloatFromString(record[2]);
			if((price==null)||(price.floatValue()==0)){
				return null;
			}
			Float priceUsd=this.getFloatFromString(record[3]);
			return new Record(name,
							  description, 
							  null, 
							  price, 
							  priceUsd, 
							  null);
		}else{
			// запись не может быть создана 
			return null;
		}
	}
	
}
