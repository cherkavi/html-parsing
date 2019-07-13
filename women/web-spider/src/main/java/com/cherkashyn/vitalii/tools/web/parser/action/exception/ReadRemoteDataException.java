package com.cherkashyn.vitalii.tools.web.parser.action.exception;

public class ReadRemoteDataException extends GeneralException {

	private static final long serialVersionUID = 1L;

	public ReadRemoteDataException() {
		super();
	}

	public ReadRemoteDataException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ReadRemoteDataException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReadRemoteDataException(String message) {
		super(message);
	}

	public ReadRemoteDataException(Throwable cause) {
		super(cause);
	}
	
}
