package schedule.DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import schedule.DataBase;
import schedule.User;
import schedule.controllers.MainController;
import schedule.customer;

public class CustomerDAO implements DAO<customer> {
    private ObservableList<customer> customers = FXCollections.observableArrayList();
    private DataBase db = new DataBase();
    private final Statement stmt;
    
    public CustomerDAO() throws SQLException {
        this.stmt = db.createConnection();
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
            int countryId = getLastID("country", t.getCountry(), "countryId");
            
            statement = String.format("INSERT IGNORE INTO city (city, countryId, createDate,createdBy,lastUpdate,lastUpdateBy) values ('%1$s', %2$s, '%3$s', '%4$s', '%5$s', '%6$s')",
                    t.getCity(),
                    String.valueOf(countryId),
                    timestamp.toString(),
                    u.getUserId(),
                    timestamp.toString(),
                    u.getUserId());
            stmt.executeUpdate(statement);
           
            int cityId = getLastID("city", t.getCity(), "cityId");
            
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
            
            int addressId = getAddressID("address", t.getAddress(), t.getAddress2(), "addressId");
            
            statement = String.format("INSERT IGNORE INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) values ('%1$s', %2$s, %3$s, '%4$s', %5$s, '%6$s', %7$s)",
                    t.getName(),
                    String.valueOf(addressId),
                    1,
                    timestamp.toString(),
                    u.getUserId(),
                    timestamp.toString(),
                    u.getUserId());
            stmt.executeUpdate(statement);
            
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(customer t, String[] params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(customer t) {
        String query = "DELETE FROM customer where customerId=" + t.getId();
        try {
            stmt.executeUpdate(query);
        } catch (SQLException ex) {
            Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public customer get(int Id) {
        return customers.get(Id);
    }
    
    public int getLastID(String table, String search, String IDname) throws SQLException{
        String query = "Select * from " + table + " where " + table + "='" + search + "';";
        ResultSet rs = stmt.executeQuery(query);
        int returnID=-1;
        while (rs.next()) {
            returnID = rs.getInt(IDname);
        }
        return returnID;
    }
    public int getAddressID(String table, String address1, String address2, String IDname) throws SQLException{
        String query = "Select * from address where address='" + address1 + "' && address2='" + address2 +"';";
        ResultSet rs = stmt.executeQuery(query);
        int returnID=-1;
        while (rs.next()) {
            returnID = rs.getInt(IDname);
        }
        return returnID;
    }
}
