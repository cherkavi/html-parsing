package com.cherkashyn.vitalii.whore.exceptions;

/**
 * when user can't access to some actions ( try to restart terminated spider, try to start already removed spider ... )
 */
public class LogicException extends GeneralException{

	private static final long serialVersionUID = 1L;

	public LogicException() {
		super();
	}

	public LogicException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LogicException(String message, Throwable cause) {
		super(message, cause);
	}

	public LogicException(String message) {
		super(message);
	}

	public LogicException(Throwable cause) {
		super(cause);
	}

}
