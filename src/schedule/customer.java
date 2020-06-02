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
public class customer {
    private String fName, lName, address, city, state, phone;
    private int zip;
    
    public customer(String fName, String lName, String address, String city, String state, int zip, String phone){
        this.fName=fName;
        this.lName=lName;
        this.address=address;
        this.city=city;
        this.state=state;
        this.zip=zip;
        this.phone=phone;
    }

    public String getFName() {
        return fName;
    }

    public String getLName() {
        return lName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPhone() {
        return phone;
    }

    public int getZip() {
        return zip;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }
    
 
}
