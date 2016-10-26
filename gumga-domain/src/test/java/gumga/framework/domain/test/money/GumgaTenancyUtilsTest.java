/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.test.money;

import gumga.framework.core.GumgaThreadScope;
import gumga.framework.domain.GumgaTenancyUtils;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author willian
 */
@RunWith(JUnit4.class)
public class GumgaTenancyUtilsTest {
    @Test
    public void testChangeOi() {
        GumgaThreadScope.organizationCode.set("1.2.");
        TestEntity testEntity = new TestEntity();
        assertEquals("1.2.",testEntity.getOi().getValue());
        GumgaTenancyUtils.changeOi("1.3.", testEntity);
        assertEquals("1.3.",testEntity.getOi().getValue());
    }
}
