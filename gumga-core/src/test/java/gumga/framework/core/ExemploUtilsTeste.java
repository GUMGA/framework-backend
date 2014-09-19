package gumga.framework.core;


import gumga.framework.core.utils.ExemploUtils;

import org.junit.Assert;
import org.junit.Test;

public class ExemploUtilsTeste {
	
	@Test 
	public void testaEhPar(){
		Assert.assertTrue(ExemploUtils.ehPar(2));
		Assert.assertFalse(ExemploUtils.ehPar(3));
		Assert.assertTrue(ExemploUtils.ehPar(4));
		Assert.assertFalse(ExemploUtils.ehPar(5));
		Assert.assertTrue(ExemploUtils.ehPar(6));
	}
	

}
