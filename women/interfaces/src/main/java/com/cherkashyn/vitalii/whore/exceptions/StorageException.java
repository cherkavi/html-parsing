package com.cherkashyn.vitalii.whore.exceptions;

/**
 * can't save the data ( file ), can't read the data ( from file or from remote url ), can't create unique ID
 */
public class StorageException extends GeneralException{

	private static final long serialVersionUID = 1L;

	public StorageException() {
		super();
	}

	public StorageException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}

	public StorageException(String message) {
		super(message);
	}

	public StorageException(Throwable cause) {
		super(cause);
	}
	
}
