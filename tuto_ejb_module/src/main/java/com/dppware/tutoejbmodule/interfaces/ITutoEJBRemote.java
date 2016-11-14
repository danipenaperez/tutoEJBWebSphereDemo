package com.dppware.tutoejbmodule.interfaces;

import javax.ejb.Remote;

/**
 * Ejb interface for Remote access.
 * @author Daniel
 *
 */
@Remote
public interface ITutoEJBRemote {
	/**
	 * Main simple testMethod
	 * @param args
	 * @return
	 */
	public String testMethod(String args);
}
