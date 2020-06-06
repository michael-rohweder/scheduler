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
    private String name, address, address2, city, country, phone;
    private int zip, Id;
    
    public customer(int Id, String name, String address, String address2, String city, String country, int zip, String phone){
        this.Id=Id;
        this.name=name;
        this.address=address;
        this.address2=address2;
        this.city=city;
        this.country=country;
        this.zip=zip;
        this.phone=phone;
    }

    public int getId(){
        return this.Id;
    }
    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }
    
    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPhone() {
        return phone;
    }

    public int getZip() {
        return zip;
    }
    
    public void setId(int id){
        this.Id=id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setAddress2(String address) {
        this.address2 = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }
    
 
}
