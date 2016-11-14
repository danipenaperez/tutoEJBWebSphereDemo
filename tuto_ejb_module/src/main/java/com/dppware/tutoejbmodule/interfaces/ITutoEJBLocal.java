package com.dppware.tutoejbmodule.interfaces;

import javax.ejb.Local;
/**
 * Ejb interface for Local access.
 * @author Daniel
 *
 */
@Local
public interface ITutoEJBLocal {
	/**
	 * Main simple testMethod
	 * @param args
	 * @return
	 */
	public String testMethod(String args);
	
}
