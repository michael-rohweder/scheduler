/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

/**
 *
 * @author micha
 */
public class User {
    
    private String userName, password;
    private int userId, active;
    
    public int getUserId(){
        return this.userId;
    }
    public String getUserName() {
        return this.userName;
    }
    public String getPassword(){
        return this.password;
    }
    public int getActive(){
        return this.active;
    }
    public void setActive(int active){
        this.active=active;
    }
    public User(int userId, String userName, String password, int active) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.active = active;
    }
}
