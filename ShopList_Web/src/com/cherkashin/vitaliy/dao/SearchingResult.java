package com.cherkashin.vitaliy.dao;

import java.io.Serializable;

/** ��������� ������ � ���� ������ */
public class SearchingResult implements Serializable{
	private final static long serialVersionUID=1L;
	/** ��� �������� */
	private String shopName;
	/** ��� �������� */
	private String name;
	/** ����  */
	private float price;
	/** ���� � USD */
	private float priceUsd;
	/** ���������� ������������� ������  */
	private int uniqueRecordId;
	/** ������������ ����� � ������� �� ��������� USD  */
	private float calculatedHrnByUsd;
	private float calculatedUsdByHrn;

	/**
	 * @param uniqueRecordId - ���������� ��� ������ 
	 * @param shopName - ��� ������� 
	 * @param name - ������������
	 * @param price - ���� ���
	 * @param calculatedHrnByUse - ������������ ���� �� ��������� USD
	 * @param priceUsd - ���� USD 
	 * @param calculatedUsdByHrn - ������������ ���� �� ��������� ���
	 */
	public SearchingResult(int uniqueRecordId, String shopName, String name, float price, float calculatedHrnByUse, float priceUsd, float calculatedUsdByHrn){
		this.uniqueRecordId=uniqueRecordId;
		this.shopName=shopName;
		this.name=name;
		this.price=price;
		this.calculatedHrnByUsd=calculatedHrnByUse;
		this.priceUsd=priceUsd;
		this.calculatedUsdByHrn=calculatedUsdByHrn;
	}
	
	/** ������������ ����� � ������� �� ��������� USD  */
	public float getCalculatedHrn(){
		return this.calculatedHrnByUsd;
	}

	/** ������������ ����� � USD �� ��������� ��� */
	public float getCalculatedUsd(){
		return this.calculatedUsdByHrn;
	}
	
	/** �������� ��� ��������  */
	public String getShopName() {
		return shopName;
	}
	
	/** ������������ */
	public String getName() {
		return name;
	}
	
	public float getPrice() {
		return price;
	}
	
	public float getPriceUsd() {
		return priceUsd;
	}

	/** �������� ���������� ����� ������  */
	public int getUniqueRecordId(){
		return this.uniqueRecordId;
	}
	
}
