/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.application.password;

import gumga.framework.application.AbstractTest;
import gumga.framework.core.service.GumgaPasswordService;
import org.junit.After;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author gyowannyqueiroz
 */
public class GumgaPasswordServiceTest extends AbstractTest{

    @Autowired
    private GumgaPasswordService passwordService;

    public GumgaPasswordServiceTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEncryptPassword() throws Exception {
        assertNotNull("Password service has not been set", passwordService);
        String password = "StringPwd012_";
        String encryptedPassword = passwordService.encryptPassword(password);
        System.out.println(String.format("### Password: %s - Encrypted password: %s", password, encryptedPassword));
        assertFalse("The encrypted password is empty", encryptedPassword == null || encryptedPassword.isEmpty());
        assertTrue("Password doesn't match", passwordService.isPasswordCorrect(password, encryptedPassword));
    }
}
