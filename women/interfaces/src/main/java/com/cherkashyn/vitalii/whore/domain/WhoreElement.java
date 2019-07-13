package com.cherkashyn.vitalii.whore.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WhoreElement {
	private int sessionId;
	private List<String> images=new ArrayList<String>();
	private String url;
	private String name;
	private List<String> phones=new ArrayList<String>();
	/** per hour, per 2 hour, per night ( 12 hour )*/
	private Map<Integer, String> prices=new LinkedHashMap<Integer, String>();
	/** city, age, body sizes ...  */
	private Map<String, String> descriptions=new LinkedHashMap<String, String>();
	private List<String> services=new ArrayList<String>();
	
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getPhones() {
		return phones;
	}
	public void setPhones(List<String> phones) {
		this.phones = phones;
	}
	public Map<Integer, String> getPrices() {
		return prices;
	}
	public void setPrices(Map<Integer, String> prices) {
		this.prices = prices;
	}
	public Map<String, String> getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(Map<String, String> descriptions) {
		this.descriptions = descriptions;
	}
	public List<String> getServices() {
		return services;
	}
	public void setServices(List<String> services) {
		this.services = services;
	}
	
	public int getSessionId() {
		return sessionId;
	}
	public void setSessionId(int sessionId) {
		this.sessionId = sessionId;
	}
	@Override
	public String toString() {
		return "WhoreElement [sessionId=" + sessionId + ", images=" + images
				+ ", url=" + url + ", name=" + name + ", phones=" + phones
				+ ", prices=" + prices + ", descriptions=" + descriptions
				+ ", services=" + services + "]";
	}
	
}
