package com.cherkashin.vitaliy.session;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

/** Http ������ ����������  */
public class ShopListSession extends WebSession{
	private final static long serialVersionUID=1L;
	
	/** Http ������ ����������  */
	public ShopListSession(Request request) {
		super(request);
	}
}
