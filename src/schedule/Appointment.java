/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import schedule.DAO.CustomerDAO;
import schedule.DAO.UserDAO;

/**
 *
 * @author micha
 */
public class Appointment {
    private int appointmentId, customerId, userId, contact;
    private String title, description, location, type, url;
    private LocalDateTime start, end;
    
    private final CustomerDAO customerDao;
    private final UserDAO userDAO;

    public int getAppointmentId() {
        return appointmentId;
    }

    public int getCustomerId() {
        return customerId;
    }
    
    public String getCustomerName(){
        return customerDao.get(customerId).getName();
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setContact(int contact) {
        this.contact = contact;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public int getContact() {
        return contact;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public LocalDateTime getStart() {
        return start;
    }
    public String getStartTime(){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
        return timeFormatter.format(start); 
    }
    public String getStartDate(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy", Locale.ENGLISH);
        return dateFormatter.format(start);
    }
    public String getEndTime(){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH);
        return timeFormatter.format(end); 
    }

    public String getContactName(){
        return userDAO.get(contact).getUserName();
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public Appointment(int appointmentId, int customerId, int userId, String title, String description, String location, int contact, String type, String url, LocalDateTime start, LocalDateTime end) throws SQLException, IOException {
        this.customerDao = new CustomerDAO();
        this.userDAO = new UserDAO();
        this.appointmentId = appointmentId;
        this.customerId = customerId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.url = url;
        this.start = start;
        this.end = end;
    }
    
    
}
