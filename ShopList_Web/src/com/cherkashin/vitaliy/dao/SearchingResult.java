package com.cherkashin.vitaliy.dao;

import java.io.Serializable;

/** результат поиска в базе данных */
public class SearchingResult implements Serializable{
	private final static long serialVersionUID=1L;
	/** имя магазина */
	private String shopName;
	/** имя магазина */
	private String name;
	/** цена  */
	private float price;
	/** цена в USD */
	private float priceUsd;
	/** уникальный идентификатор записи  */
	private int uniqueRecordId;
	/** подсчитанная сумма в гривнах на основании USD  */
	private float calculatedHrnByUsd;
	private float calculatedUsdByHrn;

	/**
	 * @param uniqueRecordId - уникальный код записи 
	 * @param shopName - код магазин 
	 * @param name - наименование
	 * @param price - цена Грн
	 * @param calculatedHrnByUse - подсчитанная цена на основании USD
	 * @param priceUsd - цена USD 
	 * @param calculatedUsdByHrn - подсчитанная цена на основании Грн
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
	
	/** подсчитанная сумма в гривнах на основании USD  */
	public float getCalculatedHrn(){
		return this.calculatedHrnByUsd;
	}

	/** подсчитанная сумма в USD на основании грн */
	public float getCalculatedUsd(){
		return this.calculatedUsdByHrn;
	}
	
	/** получить имя магазина  */
	public String getShopName() {
		return shopName;
	}
	
	/** наименование */
	public String getName() {
		return name;
	}
	
	public float getPrice() {
		return price;
	}
	
	public float getPriceUsd() {
		return priceUsd;
	}

	/** получить уникальный номер записи  */
	public int getUniqueRecordId(){
		return this.uniqueRecordId;
	}
	
}
