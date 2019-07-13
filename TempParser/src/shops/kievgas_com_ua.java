package shops;

import jp.ne.so_net.ga2.no_ji.jcom.excel8.ExcelRange;

import jp.ne.so_net.ga2.no_ji.jcom.excel8.ExcelWorksheet;
import shop_list.html.parser.engine.file_parser.EFileTypes;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous4;
import shop_list.html.parser.engine.record.Record;

public class kievgas_com_ua extends ExcelContinuous4{
	
	@Override
	protected EFileTypes getFileType() {
		return EFileTypes.zip_xls;
	}
	
	@Override
	protected String getCellValue(ExcelWorksheet sheet, int columnNumber){
		if(columnNumber==5){
			ExcelRange cell=this.getCell(sheet, this.rowCounter, columnNumber);
			String returnValue=this.getHLinkFromCell(cell);
			try{
				return (returnValue==null)?cell.Text():returnValue;
			}catch(Exception ex){
				return null;
			}
		}else{
			return super.getCellValue(sheet, columnNumber);
		}
		
	}
	
	@Override
	protected int getStartRow() {
		return 6;
	}

	@Override
	protected Record getSaveRecord(String[] record) {
		try{
			String name=record[1];
			String url=this.removeStartPage(record[5]);
			Float price=this.getFloatFromString(record[2]);
			if(price.floatValue()==0)return null;
			return new Record(name, null, url, price, null, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[3];
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://kievgas.com.ua/files/price.zip"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://kievgas.com.ua";
	}

	@Override
	protected int getMaxColumnCount() {
		return 6;
	}

	@Override
	protected int getColumnControlNumber() {
		return 2;
	}
}
