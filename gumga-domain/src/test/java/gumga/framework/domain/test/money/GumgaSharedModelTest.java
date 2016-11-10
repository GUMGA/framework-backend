/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.test.money;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 *
 * @author munif
 */
@RunWith(JUnit4.class)
public class GumgaSharedModelTest {

    @Test
    public void testAddUser() {
        TestEntitySharedModel shared = new TestEntitySharedModel();
        shared.addUser("gumga@gumga.com.br");
        assertEquals(",gumga@gumga.com.br,", shared.getGumgaUsers());
    }
    
    @Test
    public void testAddUserWithTwoValues() {
        TestEntitySharedModel shared = new TestEntitySharedModel();
        shared.addUser("gumga@gumga.com.br");
        shared.addUser("suporte@gumga.com.br");
        assertEquals(",gumga@gumga.com.br,suporte@gumga.com.br,", shared.getGumgaUsers());
    }    
    
    @Test
    public void testRemoveUser() {
        TestEntitySharedModel shared = new TestEntitySharedModel();
        shared.addUser("gumga@gumga.com.br");
        assertEquals(",gumga@gumga.com.br,", shared.getGumgaUsers());
        shared.removeUser("gumga@gumga.com.br");
        assertEquals(",", shared.getGumgaOrganizations());
    }    
    
    @Test
    public void testRemoveAllUser() {
        TestEntitySharedModel shared = new TestEntitySharedModel();
        shared.addUser("gumga@gumga.com.br");
        assertEquals(",gumga@gumga.com.br,", shared.getGumgaUsers());
        shared.removeAllUser();
        assertEquals(",", shared.getGumgaUsers());
    }    

    @Test
    public void testAddOganization() {
        TestEntitySharedModel shared = new TestEntitySharedModel();
        shared.addOrganization("41.");
        assertEquals(",41.,", shared.getGumgaOrganizations());
    }

    @Test
    public void testAddOganizationWithTwoValues() {
        TestEntitySharedModel shared = new TestEntitySharedModel();
        shared.addOrganization("41.");
        shared.addOrganization("41.54.");
        assertEquals(",41.,41.54.,", shared.getGumgaOrganizations());
    }

    @Test
    public void testRemoveOganization() {
        TestEntitySharedModel shared = new TestEntitySharedModel();
        shared.addOrganization("41.");
        assertEquals(",41.,", shared.getGumgaOrganizations());
        shared.removeOrganization("41.");
        assertEquals(",", shared.getGumgaOrganizations());
    }

    @Test
    public void testRemoveOganizationWithTwoValues() {
        TestEntitySharedModel shared = new TestEntitySharedModel();
        shared.addOrganization("41.");
        shared.addOrganization("41.54.");
        assertEquals(",41.,41.54.,", shared.getGumgaOrganizations());
        shared.removeOrganization("41.");
        assertEquals(",41.54.,", shared.getGumgaOrganizations());
    }

    @Test
    public void testRemoveAllOganization() {
        TestEntitySharedModel shared = new TestEntitySharedModel();
        shared.addOrganization("41.");
        shared.addOrganization("41.54.");
        assertEquals(",41.,41.54.,", shared.getGumgaOrganizations());
        shared.removeAllOrganization();
        assertEquals(",", shared.getGumgaOrganizations());
    }
}
