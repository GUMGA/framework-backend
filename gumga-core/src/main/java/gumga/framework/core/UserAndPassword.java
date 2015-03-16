/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.core;

/**
 *
 * @author munif
 */
public class UserAndPassword {

    private String user;

    private String password;

    private String newPassword;

    public UserAndPassword() {
        this.user = "empty";
        this.password = "empty";
        this.newPassword = null;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "UserAndPassword{" + "user=" + user + ", password=" + password + '}';
    }

}
