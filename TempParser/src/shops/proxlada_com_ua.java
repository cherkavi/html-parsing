package shops;

import jxl.Cell;
import jxl.Sheet;
import shop_list.html.parser.engine.exception.EParseException;
import shop_list.html.parser.engine.file_parser.narrow_xls.ExcelContinuous;
import shop_list.html.parser.engine.record.Record;

public class proxlada_com_ua extends ExcelContinuous{

	@Override
	protected int getMaxColumnCount() {
		return 11;
	}

	@Override
	protected int getStartRow() {
		return 3;
	}

	@Override
	protected Record getSaveRecord(String[] record) throws EParseException {
		try{
			String name=record[2]+" "+record[3];
			String description=record[4];
			Float priceUsd=this.getFloatFromString(record[6]);
			String url=this.removeStartPage(record[10]);
			if(!isStringEqualsAny(record[9], "склад"))return null;
			return new Record(name, description, url, null, priceUsd, null);
		}catch(Exception ex){
			return null;
		}
	}

	@Override
	protected String getCellValue(Sheet sheet, int columnNumber) {
		if(columnNumber==10){
			Cell cell=sheet.getCell(columnNumber, this.rowCounter);
			return this.getHLinkFromCell(cell);
		}else if(columnNumber==3){
			Cell cell=sheet.getCell(columnNumber, this.rowCounter);
			if(this.isHLink(cell)){
				return this.getHLinkCaption(cell);
			}else{
				return super.getCellValue(sheet, columnNumber);
			}
		}else{
			return super.getCellValue(sheet, columnNumber);
		}
	}
	@Override
	protected int getColumnControlNumber() {
		return 0;
	}
	
	@Override
	protected String getSectionNameFromRecord(String[] record) {
		return record[1];
	}

	@Override
	protected String[] getShopPriceFileUrl() {
		return new String[]{"http://proxlada.com.ua/images/price.xls"};
	}

	@Override
	public String getShopUrlStartPage() {
		return "http://proxlada.com.ua";
	}

}
