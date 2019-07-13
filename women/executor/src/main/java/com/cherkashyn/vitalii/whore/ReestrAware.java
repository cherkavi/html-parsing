package com.cherkashyn.vitalii.whore;

import org.apache.karaf.shell.console.OsgiCommandSupport;

import com.cherkashyn.vitalii.whore.interfaces.reestr.Reestr;

public abstract class ReestrAware extends OsgiCommandSupport{

	// --------- inversion of control via OSGi container -------------
	protected Reestr reestr;
	
	public Reestr getReestr() {
		return reestr;
	}

	public void setReestr(Reestr reestr) {
		this.reestr = reestr;
	}


}
