package schedule.DAO;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import schedule.Appointment;
import schedule.DataBase;
import schedule.LogFile;
import schedule.User;
import schedule.controllers.LogInScreenController;
import schedule.controllers.MainController;
import schedule.customer;

public class AppointmentDAO implements DAO<Appointment> {
    private ObservableList<Appointment> appointments = FXCollections.observableArrayList();
    private DataBase db = new DataBase();
    private final Statement stmt;
    private LogFile logFile;
    private Logger logger;
    User currentUser = LogInScreenController.getCurrentUser();
    
    public AppointmentDAO() throws SQLException, IOException {
        this.stmt = db.createConnection();
        this.logFile = new LogFile();
        logger = logFile.getLogger();
    }
    
    @Override
    public ObservableList<Appointment> getAll(){
        
        try {
            appointments.clear();
            
            String query = "SELECT * FROM appointment;";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getInt("appointmentId"), 
                        rs.getInt("customerId"), 
                        rs.getInt("userId"), 
                        rs.getString("title"), 
                        rs.getString("description"), 
                        rs.getString("location"), 
                        rs.getInt("contact"), 
                        rs.getString("type"), 
                        rs.getString("url"), 
                        rs.getTimestamp("start").toLocalDateTime(), 
                        rs.getTimestamp("end").toLocalDateTime()
                ));
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return appointments;
    }

    @Override
    public void add(Appointment t, User u) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            
            String query = "INSERT INTO appointment (customerId, userId, title, description, location,contact,type,url,start,end, createDate, createdBy, lastUpdate, lastUpdateBy) values ("
                    + t.getCustomerId() + "," 
                    + t.getUserId() + ","
                    + "'" + t.getTitle() + "',"
                    + "'" + t.getDescription() + "',"
                    + "'" + t.getLocation() + "',"
                    + t.getContact() + ","
                    + "'" + t.getType() + "'," 
                    + "'" + t.getUrl() + "',"
                    + "'" + t.getStart() + "',"
                    + "'" + t.getEnd() + "',"
                    + "'" + timestamp.toString() + "',"
                    + currentUser.getUserId() + ","
                    + "'" + timestamp.toString() + "',"
                    + currentUser.getUserId() + ");";
            
            stmt.executeUpdate(query);
                        
            String logString = "User ID: " + currentUser.getUserId() + "(" + currentUser.getUserName() + ") created a new appointment\n";
            logger.info(logString);
        } catch (Exception e) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public int checkAddress(customer c) throws SQLException{
        String query = "Select * from address where address='" + c.getAddress() + "' && address2='" + c.getAddress2() +"' AND phone='" + c.getPhone() + "' AND postalCode=" + c.getZip() + ";";
        ResultSet rs = stmt.executeQuery(query);
        int returnID=-1;
        while (rs.next()) {
            returnID = rs.getInt("addressId");
        }
        return returnID;
    }
    
    @Override
    public void update(Appointment t) {
        /*
        int addressID = -1;
        try {
            addressID = checkAddress(t);
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String query;
        if (addressID == -1){
            query = ""
                + "UPDATE customer, address, city, country"
                + " SET customer.customerName = '" + t.getName() + "',"
                + " address.address='" + t.getAddress() + "',"
                + " address.address2='" + t.getAddress2() + "',"
                + " address.postalCode=" + t.getZip() + ","
                + " address.phone='" + t.getPhone() + "',"
                + " city.city='" + t.getCity() + "',"
                + " country.country='" + t.getCountry() + "'"
                + " WHERE customer.customerId=" + t.getId()
                + " AND customer.addressId = address.addressId"
                + " AND address.cityId = city.cityId"
                + " AND city.countryId = country.countryId;";
        } else {
            query = ""
                + "UPDATE customer, address, city, country"
                + " SET customer.customerName = '" + t.getName() + "',"
                + " customer.addressId = " + addressID + ","
                + " address.postalCode=" + t.getZip() + ","
                + " address.phone='" + t.getPhone() + "',"
                + " city.city='" + t.getCity() + "',"
                + " country.country='" + t.getCountry() + "'"
                + " WHERE customer.customerId=" + t.getId()
                + " AND customer.addressId = address.addressId"
                + " AND address.cityId = city.cityId"
                + " AND city.countryId = country.countryId;";
        }
        try {
            stmt.executeUpdate(query);
            
            
            String logString = "User ID: " + currentUser.getUserId() + "(" + currentUser.getUserName() + ") updated a customer\n"
                + "Customer ID: " + t.getId() + "(" + t.getName() + ")\n";
            logger.info(logString);
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
    }

    @Override
    public void delete(Appointment t) {
        /*
        String query = "DELETE FROM customer where customerId=" + t.getId();
        try {
            stmt.executeUpdate(query);
            String logString = "User ID: " + currentUser.getUserId() + "(" + currentUser.getUserName() + ") deleted a customer\n"
                + "Customer ID: " + t.getId() + "(" + t.getName() + ")\n";
            logger.info(logString);

        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
*/
    }

    @Override
    public Appointment get(int Id) {
        return appointments.get(Id);
    }
}