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
import schedule.DataBase;
import schedule.LogFile;
import schedule.User;
import schedule.controllers.LogInScreenController;
import schedule.controllers.MainController;
import schedule.customer;

public class CustomerDAO implements DAO<customer> {
    private ObservableList<customer> customers = FXCollections.observableArrayList();
    private DataBase db = new DataBase();
    private final Statement stmt;
    private LogFile logFile;
    private Logger logger;
    User currentUser = LogInScreenController.getCurrentUser();
    
    
    public CustomerDAO() throws SQLException, IOException {
        this.stmt = db.createConnection();
        this.logFile = new LogFile();
        logger = logFile.getLogger();
    }
    
    @Override
    public ObservableList<customer> getAll(){
        
        try {
            customers.clear();
            String query = "SELECT c.customerId, c.customerName, a.address, a.address2, a.postalCode, a.phone, cty.city, ctry.country FROM customer c INNER JOIN address a ON a.addressId = c.addressId INNER JOIN city cty ON cty.cityId = a.cityId INNER JOIN country ctry ON ctry.countryId = cty.countryId WHERE active=1;";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                customers.add(new customer(rs.getInt("c.customerId"), rs.getString("c.customerName"), rs.getString("a.address"), rs.getString("a.address2"), rs.getString("cty.city"), rs.getString("ctry.country"), rs.getInt("a.postalCode"), rs.getString("a.phone")));
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customers;
    }

    @Override
    public void add(customer t, User u) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            String statement = String.format("INSERT IGNORE INTO country (country, createdBy, createDate, lastUpdateBy, lastUpdate) Values ('%1$s', '%2$s', '%3$s', '%4$s', '%5$s')", 
                    t.getCountry(), 
                    u.getUserId(), 
                    timestamp.toString(), 
                    u.getUserId(), 
                    timestamp.toString());
            stmt.executeUpdate(statement);
            int countryId = getLastID("country", t.getCountry(), "countryId", "country");
            
            statement = String.format("INSERT IGNORE INTO city (city, countryId, createDate,createdBy,lastUpdate,lastUpdateBy) values ('%1$s', %2$s, '%3$s', '%4$s', '%5$s', '%6$s')",
                    t.getCity(),
                    String.valueOf(countryId),
                    timestamp.toString(),
                    u.getUserId(),
                    timestamp.toString(),
                    u.getUserId());
            stmt.executeUpdate(statement);
           
            int cityId = getLastID("city", t.getCity(), "cityId", "city");
            
            statement = String.format("INSERT IGNORE INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) values ('%1$s', '%2$s', %3$s, %4$s, '%5$s', '%6$s', %7$s, '%8$s', %9$s)",
                    t.getAddress(),
                    t.getAddress2(),
                    String.valueOf(cityId),
                    t.getZip(),
                    t.getPhone(),
                    timestamp.toString(),
                    u.getUserId(),
                    timestamp.toString(),
                    u.getUserId());
            stmt.executeUpdate(statement);
            
            int addressId = checkAddress(t);
            
            statement = String.format("INSERT IGNORE INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) values ('%1$s', %2$s, %3$s, '%4$s', %5$s, '%6$s', %7$s)",
                    t.getName(),
                    String.valueOf(addressId),
                    1,
                    timestamp.toString(),
                    u.getUserId(),
                    timestamp.toString(),
                    u.getUserId());
            stmt.executeUpdate(statement);
            t.setId(getLastID("customer", t.getName(), "customerId", "customerName"));
                        
            String logString = "User ID: " + currentUser.getUserId() + "(" + currentUser.getUserName() + ") created a new customer\n"
                    + "Customer ID: " + t.getId() + "(" + t.getName() + ")\n";
            logger.info(logString);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
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
    public void update(customer t) {
        
        int addressID = -1;
        try {
            addressID = checkAddress(t);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(customer t) {
        String query = "DELETE FROM customer where customerId=" + t.getId();
        try {
            stmt.executeUpdate(query);
            String logString = "User ID: " + currentUser.getUserId() + "(" + currentUser.getUserName() + ") deleted a customer\n"
                + "Customer ID: " + t.getId() + "(" + t.getName() + ")\n";
            logger.info(logString);

        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public customer get(int Id) {
        customer returnedCustomer=null;
        try {
            String query = "SELECT c.customerId, c.customerName, a.address, a.address2, a.postalCode, a.phone, cty.city, ctry.country FROM customer c INNER JOIN address a ON a.addressId = c.addressId INNER JOIN city cty ON cty.cityId = a.cityId INNER JOIN country ctry ON ctry.countryId = cty.countryId WHERE active=1 AND c.customerId=" + Id + ";";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                returnedCustomer = new customer(rs.getInt("c.customerId"), rs.getString("c.customerName"), rs.getString("a.address"), rs.getString("a.address2"), rs.getString("cty.city"), rs.getString("ctry.country"), rs.getInt("a.postalCode"), rs.getString("a.phone"));
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnedCustomer;
    }
    
    public int getLastID(String table, String search, String IDname, String field) throws SQLException{
        String query = "Select * from " + table + " where " + field + "='" + search + "';";
        ResultSet rs = stmt.executeQuery(query);
        int returnID=-1;
        while (rs.next()) {
            returnID = rs.getInt(IDname);
        }
        return returnID;
    } 
    
//    public int getLastID(String table, String search, String IDname) throws SQLException{
//        String query = "Select * from " + table + " where " + table + "='" + search + "';";
//        ResultSet rs = stmt.executeQuery(query);
//        int returnID=-1;
//        while (rs.next()) {
//            returnID = rs.getInt(IDname);
//        }
//        return returnID;
//    }
    public int getAddressID(String address1, String address2) throws SQLException{
        String query = "Select * from address where address='" + address1 + "' && address2='" + address2 +"';";
        ResultSet rs = stmt.executeQuery(query);
        int returnID=-1;
        while (rs.next()) {
            returnID = rs.getInt("addressId");
        }
        return returnID;
    }
}