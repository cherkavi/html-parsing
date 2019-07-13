package com.cherkashyn.vitalii.whore.interfaces.parsercontroller;

import com.cherkashyn.vitalii.whore.exceptions.StorageException;

public interface ParserController {
	/** 
	 * generate unique number for session 
	 * @param sprider
	 * @return
	 * @throws StorageException
	 */
	int createSessionNumber(String uniqueId) throws StorageException;

	/** 
	 * notification about start of parsing process
	 * @param sessionId
	 */
	void notifyBegin(int sessionId) throws StorageException;

	/**
	 * notification about not normal ending of parsing process 
	 * @param sessionId
	 * @param message - error message 
	 */
	void notifyTerminate(int sessionId, String message) throws StorageException;

	/**
	 * notification about normal ( successfull ) end of parsing process 
	 * @param sessionId
	 */
	void notifyEnd(int sessionId) throws StorageException;
}
