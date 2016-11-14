package com.dppware.tutoejbmodule.impl;

import javax.ejb.Stateless;

import com.dppware.tutoejbmodule.interfaces.ITutoEJBLocal;
import com.dppware.tutoejbmodule.interfaces.ITutoEJBRemote;
/**
 * Enterprise Java Bean test
 * @author 51946690
 *
 */
@Stateless
public class TutoEJBImpl implements ITutoEJBLocal, ITutoEJBRemote{

	@Override
	public String testMethod(String args) {
		return "Succesfully invoked tutoEJB with args: "+ args;
	}

}
