package com.cherkashyn.vitalii.whore.interfaces.spider;

public interface Spider {
	public static enum Status{
		/** ready for working, still not started, maybe just installed, can be executed */
		READY,
		/** working right now, can be terminated */
		WORKING,
		/** was terminated by user */
		TERMINATED,
		/** was terminated by user */
		BROKEN,
		/** parse process was done, maybe with some errors */
		FINISHED
	}
	
	/** execute process, it can finish normally, or terminated */
	void execute();
	
	/** terminate */
	void terminate();
	
	/**
	 * unique Id for spider ( name of site )
	 * @return
	 */
	String getId();
	
	/**
	 * @return - current status of the Spider
	 */
	Status getCurrentStatus();
}
