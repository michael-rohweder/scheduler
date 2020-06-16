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

public class UserDAO implements DAO<User> {
    private ObservableList<User> users = FXCollections.observableArrayList();
    private DataBase db = new DataBase();
    private final Statement stmt;
    User currentUser = LogInScreenController.getCurrentUser();
    
    
    public UserDAO() throws SQLException, IOException {
        this.stmt = db.createConnection();
    }
    
    @Override
    public ObservableList<User> getAll(){
        
        try {
            users.clear();
            String query = "SELECT * from user WHERE active=1;";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                users.add(new User(rs.getInt("userId"), rs.getString("userName"), rs.getString("password")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    @Override
    public void add(User t, User u) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try {
            String statement = String.format("INSERT IGNORE INTO user (userName, password, active, createDate, createdBy, lastUpdate, lastUpdateBy) values ('%1$s', %2$s, %3$s, '%4$s', %5$s, '%6$s', %7$s)",
                    t.getUserName(),
                    t.getPassword(),
                    1,
                    timestamp.toString(),
                    u.getUserId(),
                    timestamp.toString(),
                    u.getUserId());
            stmt.executeUpdate(statement);
                        
            String logString = "User ID: " + currentUser.getUserId() + "(" + currentUser.getUserName() + ") created a new user\n"
                    + "User Name: "+ t.getUserName() + ")\n";
            new LogFile(logString);
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void update(User t) {
        
        String query;
            query = ""
                + "UPDATE user"
                + " SET userName='" + t.getUserName()
                + "', password='" + t.getPassword()
                + "' WHERE userId" + t.getUserId() + ";";
        try {
            stmt.executeUpdate(query);
            String logString = "User ID: " + currentUser.getUserId() + "(" + currentUser.getUserName() + ") updated a user\n"
                + "User ID: " + t.getUserId() + "(" + t.getUserName()+ ")\n";
            new LogFile(logString);
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(User t) {
        String query = "DELETE FROM user where userId=" + t.getUserId();
        try {
            stmt.executeUpdate(query);
            String logString = "User ID: " + currentUser.getUserId() + "(" + currentUser.getUserName() + ") deleted a user\n"
                + "User ID: " + t.getUserId() + "(" + t.getUserName() + ")\n";
            new LogFile(logString);
        } catch (SQLException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public User get(int Id) {
        User returnedUser=null;
        try {
            String query = "SELECT * from user WHERE active=1 AND userId=" + Id + ";";
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                returnedUser = new User(rs.getInt("userId"), rs.getString("userName"), rs.getString("password")); 
            }

        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnedUser;
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