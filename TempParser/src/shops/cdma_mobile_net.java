package shops;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.csvreader.CsvReader;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.MultiFileParseManager;
import shop_list.html.parser.engine.record.Record;

public class cdma_mobile_net extends MultiFileParseManager<CsvReader, String[]>{

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.csv;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.cdma-mobile.net/price/full_price/"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.cdma-mobile.net";
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
			if(record[0]!=null){
				return record[0].trim();
			}
		}
		return null;
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
		if((record!=null)&&(record.length>=3)){
			String name=record[1];
			String description=null;
			Float price=this.getFloatFromString(record[2].replaceAll("[а-я .]", ""));
			if((price==null)||(price.floatValue()==0)){
				return null;
			}
			String url=null;
			if(record.length>=4){
				url=this.removeStartPage(record[3]);
			}
			return new Record(name,
							  description, 
							  removeStartPage(url), 
							  price, 
							  null, 
							  null);
		}else{
			// запись не может быть создана 
			return null;
		}
	}
	
}
