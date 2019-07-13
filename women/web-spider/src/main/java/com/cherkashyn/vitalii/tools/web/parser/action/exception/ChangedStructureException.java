package com.cherkashyn.vitalii.tools.web.parser.action.exception;

public class ChangedStructureException extends GeneralException{

	private static final long serialVersionUID = 1L;

	public ChangedStructureException() {
		super();
	}

	public ChangedStructureException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ChangedStructureException(String message, Throwable cause) {
		super(message, cause);
	}

	public ChangedStructureException(String message) {
		super(message);
	}

	public ChangedStructureException(Throwable cause) {
		super(cause);
	}

}
