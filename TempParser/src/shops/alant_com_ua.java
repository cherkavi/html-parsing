package shops;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import com.csvreader.CsvReader;

import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.MultiFileParseManager;
import shop_list.html.parser.engine.record.Record;

public class alant_com_ua extends MultiFileParseManager<CsvReader, String[]>{

	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.csv;
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://www.alant.com.ua/price.csv"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://www.alant.com.ua";
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
		if((record!=null)&&(record.length>6)){
			String name=record[2]+" "+record[3];
			// String description=record[3];
			Float priceUSD=this.getFloatFromString(record[5]);
			if((priceUSD==null)||(priceUSD.floatValue()==0)){
				return null;
			}
			String url=record[7];
			return new Record(name,
							  null, 
							  removeStartPage(url), 
							  null, 
							  priceUSD, 
							  null);
		}else{
			// ������ �� ����� ���� ������� 
			return null;
		}
	}
	
}
