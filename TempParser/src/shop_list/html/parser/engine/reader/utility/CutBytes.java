package shop_list.html.parser.engine.reader.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CutBytes {
/*	public byte[] getBytes() throws IOException {
		URLConnection connection=(new URL("file:///d:/temp.html")).openConnection();
		connection.setDoInput(true);
		//connection.setDoOutput(true);
		connection.connect();
		InputStream inputStream=connection.getInputStream();
		ByteArrayOutputStream output=new ByteArrayOutputStream();
		
		// this.copyFromInputToOutput(inputStream, output);
		this.copyFromInputToOutput(inputStream, 
								   output,
								   new ArrayList<String>(){
										   {
											   this.add("<script");
										   }
								   },
								   new ArrayList<String>(){
										   {
											   this.add("</script>");
											   this.add("/>");
										   }
								   }
								   );
		return output.toByteArray();
	}
*/

	@SuppressWarnings("unused")
	private void copyFromInputToOutput(InputStream inputStream, OutputStream outputStream) throws IOException{
		byte[] buffer=new byte[400];
		int byteCount=0;
		while((byteCount=inputStream.read(buffer))!=(-1)){
			outputStream.write(buffer,0,byteCount);
		}
	}
	
	/** �������� �� ������ ����� ��������������� ������ �� ����������� ������� � ������������� ��� � ������ ���� */
	private ArrayList<byte[]> getSortByLengthList(ArrayList<String> list){
		Collections.sort(list, new Comparator<String>(){
			@Override
			public int compare(String o1, String o2) {
				if(o1==null&&o2==null)return 0;
				if(o1==null&&o2!=null)return -1;
				if(o1!=null&&o2==null)return 1;
				if(o1.length()==o2.length())return 0;
				if(o1.length()>o2.length())return -1;
				return 1;
			}
		});
		ArrayList<byte[]> returnValue=new ArrayList<byte[]>(list.size());
		for(int index=0;index<list.size();index++){
			 returnValue.add(list.get(index).getBytes());
		}
		return returnValue;
	}
	
	/**
	 * ����������� �� ��������� � ��������
	 * @param inputStream - �������� ������
	 * @param outputStream - �������� ������ 
	 * @param removeBegin - ������ �� �����, ������� ������������� � ������ �����, ������� �� ������ ������� � �������
	 * @param removeEnd - ������ �� �����, ������� ������������� �� ��������� �����, ������� �� ������ ������� � ������� 
	 * @throws IOException
	 */
	public void copyFromInputToOutput(InputStream inputStream, 
									   OutputStream outputStream, 
									   ArrayList<String> removeBegin, 
									   ArrayList<String> removeEnd) throws IOException{
		ArrayList<byte[]> beginBytes=this.getSortByLengthList(removeBegin);
		ArrayList<byte[]> endBytes=this.getSortByLengthList(removeEnd);
		
		/** ����, ������� ������� � ��� ��� ������ ������������ */
		boolean flagWrite=true;
		byte[] buffer=new byte[4096];
		int readCount=0;
		while((readCount=inputStream.read(buffer))!=(-1)){
			// System.out.println(new String(buffer));
			while(true){
				try{
					if(flagWrite==true){
						// find removeBegin
						if(detectBeginIntoArray(buffer, 0, readCount, beginBytes)){
							FindElementPosition position=getIndexOfSearchByteArray(buffer, readCount, beginBytes);
							if(position==null){
								outputStream.write(buffer, 0, readCount);
								break;
							}
							// �������� ����� ��������� ������ ��� ������ � OutputStream
							outputStream.write(buffer, 0, position.getStartPosition());
							outputStream.flush();
							// �������� ����� �� ��������� ������ 
							this.shiftLeftBytes(buffer, readCount, position.getStartPosition()+beginBytes.get(position.getIndexOfElement()).length);
							// �������� ��������� �� ��������� ���� 
							readCount=readCount-(beginBytes.get(position.getIndexOfElement()).length+position.getStartPosition());
							flagWrite=false;
							continue;
						}else{
							// ������ ������ �������� ���������� ���� �� ������
							outputStream.write(buffer,0,readCount);
							outputStream.flush();
							break;
						}
					}else{
						// find removeEnd
						if(detectBeginIntoArray(buffer, 0, readCount, endBytes)){
							FindElementPosition position=getIndexOfSearchByteArray(buffer, readCount, endBytes);
							if(position==null)break;
							// �������� ������ �� �����, �.�. ����� ���� "�� ����������"
							// outputStream.write(buffer, 0, position.getIndexOfElement());
							// �������� ����� �� ��������� ������ 
							this.shiftLeftBytes(buffer, readCount, position.getStartPosition()+endBytes.get(position.getIndexOfElement()).length);
							// �������� ��������� �� ��������� ���� 
							readCount=readCount-(endBytes.get(position.getIndexOfElement()).length+position.getStartPosition());
							flagWrite=true;
							continue;
						}else{
							// ������ ��������� �������� ���������� ���� �� ������
							// outputStream.write(buffer,0,readCount);
							// outputStream.flush();
							break;
						}
						
					}
				}catch(NeedDataException ex){
					BooleanWrap flagDone=new BooleanWrap(false);
					buffer=addDataToBuffer(buffer, readCount, inputStream, ex.getNeedDataForRead(),flagDone);
					if(flagDone.getValue()){
						readCount=flagDone.getSize();
					}else{
						if(flagWrite){
							outputStream.write(buffer);
							outputStream.flush();
						}
					}
				}
			}
		}
	}
	
	/** �������� ������ � �����, ���� ��� ������ - ��������� Exception  */
	private byte[] addDataToBuffer(byte[] buffer, int readCount,InputStream inputStream, int needDataForRead, BooleanWrap flagDone){
		// System.out.println("#addDataToBuffer before:"+new String(buffer));
		if(buffer.length>=(readCount+needDataForRead)){
			// ����� ����������� �� ����� 
			try{
				int readedBytes=inputStream.read(buffer, readCount, needDataForRead);
				if(readedBytes<needDataForRead){
					flagDone.setSize(readedBytes+readCount);
					flagDone.setValue(false);
				}else{
					flagDone.setSize(readedBytes+readCount);
					flagDone.setValue(true);
				}
			}catch(IOException ex){
				flagDone.setSize(readCount);
				flagDone.setValue(false);
			}
			// System.out.println("#addDataToBuffer after:"+new String(buffer));
			return buffer;
		}else{
			// ����� ����� ���������
			byte[] newBuffer=new byte[buffer.length+needDataForRead];
			for(int index=0;index<buffer.length;index++)newBuffer[index]=buffer[index];
			try{
				int readedBytes=inputStream.read(newBuffer, buffer.length, needDataForRead);
				if(readedBytes<needDataForRead){
					flagDone.setSize(buffer.length+readedBytes);
					flagDone.setValue(false);
				}else{
					flagDone.setSize(newBuffer.length);
					flagDone.setValue(true);
				}
			}catch(IOException ex){
				flagDone.setSize(readCount);
				flagDone.setValue(false);
			}
			// System.out.println("#addDataToBuffer after:"+new String(newBuffer));
			return newBuffer;
		}
	}


	/**   
	 * ����������� ��� ������ �� ���������� ������ 
	 * @param buffer - ��������� �����
	 * @param readIndex - ������ ������ � ������
	 * @param readCount - ������ ��������� � ������
	 * @param shiftCount - �������� 
	 * 
	 */
	private void shiftLeftBytes(byte[] buffer, int readCount, int shiftCount) {
		// System.out.println("shiftLeftBytes before:"+new String(buffer));
		for(int index=shiftCount;index<readCount;index++){
			buffer[index-shiftCount]=buffer[index];
		}
		// System.out.println("shiftLeftBytes after:"+new String(buffer));
	}


	/** 
	 * �������� �� ������� ���� ( ������������ ��������� ) ������ � ��������� ��������� ������ �� ���������� �������� � ������ 
	 * @param buffer - ����� ����
	 * @param readIndex - ������ ������
	 * @param readCount - ������ ���������
	 * @param beginBytes - ������ ������� ��������
	 * @return null ���� �� ���� �� ������������������� �� �������
	 */
	private FindElementPosition getIndexOfSearchByteArray(byte[] buffer, int limit, ArrayList<byte[]> beginBytes)  throws NeedDataException{
		// System.out.println("#getIndexOfSearchByteArray source:"+new String(buffer));
		FindElementPosition returnValue=null;
		for(int counter=0;counter<beginBytes.size();counter++){
			int startPosition=this.getFirstIndexOfArray(buffer, limit, beginBytes.get(counter));
			if(startPosition>=0){
				returnValue=new FindElementPosition(counter, startPosition);
				break;
			}
		}
		return returnValue;
	}
	
	/**
	 * @param buffer ������� 
	 * @param limit
	 * @param findArray
	 * @return
	 */
	private int getFirstIndexOfArray(byte[] buffer, int limit, byte[] findArray) throws NeedDataException{
		// System.out.println("#getFirstIndexOfArray buffer:"+new String(buffer)+"\n find: "+new String(findArray));
		int position=-1;
		while( (position=getFirstIndexOfElement(buffer,limit,position+1, findArray[0]))>=0){
			int counter=0;
			for(counter=0;counter<findArray.length;counter++){
				try{
					if(buffer[position+counter]!=findArray[counter]){
						break;
					}else{
						
					}
				}catch(IndexOutOfBoundsException ex){
					throw new NeedDataException(position-counter);
				}
			}
			if(counter==findArray.length){
				// System.out.println("#getFirstIndexOfArray return:"+position);
				return position;
			}
		}
		// System.out.println("#getFirstIndexOfArray return: -1");
		return -1;
	}
	
	/** �������� ������� ������� ���������� �������� */
	private int getFirstIndexOfElement(byte[] buffer, int limit, int startPosition, byte element){
		int returnValue=(-1);
		for(int index=startPosition; index<limit;index++){
			if(buffer[index]==element){
				returnValue=index;
				break;
			}
		}
		return returnValue;
	}


	/** ���������������� � ���������� � ������� ���� ���������� ����� �� ������  */
	private boolean detectBeginIntoArray(byte[] buffer, int indexBegin, int count, ArrayList<byte[]> beginBytes) {
		// System.out.println(new String(buffer));
		boolean returnValue=false;
		for(int index=0;index<beginBytes.size();index++){
			if(getFirstIndexOfElement(buffer,
								   count,
								   indexBegin, 
								   beginBytes.get(index)[0])>=0)return true;
		}
		return returnValue;
	}


	public static void main(String[] args){
		try{
			CutBytes cutBytes=new CutBytes();
			FileInputStream input=new FileInputStream(new File("d:\\temp.html"));
			FileOutputStream output=new FileOutputStream(new File("d:\\test.html"));
			cutBytes.copyFromInputToOutput(input, 
										   output, 
										   new ArrayList<String>(){
											   private final static long serialVersionUID=1L;
											   {
												   this.add("<script");
											   }
										   },
										   new ArrayList<String>(){
											   private final static long serialVersionUID=1L;
												   {
													   this.add("</script>");
													   this.add("/>");
												   }
										   }
										   );		
			/*ArrayList<byte[]> listOfByte=cutBytes.getSortByLengthList(list);
			for(int index=0;index<listOfByte.size();index++){
				System.out.println(index+" : "+getStringFromArrayListOfByte(listOfByte.get(index)));
			}*/
			
		}catch(Exception ex){
			System.err.println("Exception:"+ex.getMessage());
		}
	}


	/*private static String getStringFromArrayListOfByte(byte[] bs) {
		StringBuffer returnValue=new StringBuffer();
		for(int index=0;index<bs.length;index++){
			returnValue.append(bs[index]);
			returnValue.append(" ");
		}
		return returnValue.toString();
	}*/
}

class BooleanWrap{
	private boolean flag=false;
	private int size=0;
	
	public BooleanWrap(boolean flag){
		this.flag=flag;
	}
	
	public boolean getValue(){
		return this.flag;
	}
	
	public void setValue(boolean flag){
		this.flag=flag;
	}
	
	public int getSize(){
		return this.size;
	}
	
	public void setSize(int size){
		this.size=size;
	}
}

/** �����, ������� �������������� ������� � ������ ������� � ������ ���� */
class FindElementPosition{
	private int indexOfElement;
	private int startPosition;

	public FindElementPosition(int indexOfElement, int startPosition){
		this.indexOfElement=indexOfElement;
		this.startPosition=startPosition;
	}
	
	int getIndexOfElement(){
		return this.indexOfElement;
	}
	
	int getStartPosition(){
		return this.startPosition;
	}
}

class NeedDataException extends Exception{
	private final static long serialVersionUID=1L;
	private int needDataForRead;
	public NeedDataException(int needDataForRead){
		this.needDataForRead=needDataForRead;
	}
	
	/** ���-�� ������, ������� ����� ��������  */
	public int getNeedDataForRead(){
		return this.needDataForRead;
	}
}