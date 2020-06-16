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
    User currentUser = LogInScreenController.getCurrentUser();
    
    public AppointmentDAO() throws SQLException, IOException {
        this.stmt = db.createConnection();
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
            appointments.forEach(a -> System.err.println("Appt ID: " + a.getAppointmentId() + "\n"));

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
            
            new LogFile(logString);
            
        } catch (Exception e) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    @Override
    public void update(Appointment t) {        
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        // title, description, location,contact,type,url,start,end, createDate, createdBy, lastUpdate, lastUpdateBy
           String query = ""
                + "UPDATE appointment"
                + " SET customerId=" + t.getCustomerId() + ","
                + " title='" + t.getTitle() + "',"
                + " description='" + t.getDescription() + "',"
                + " location='" + t.getLocation() + "',"
                + " contact=" + t.getContact() + ","
                + " type='" + t.getType() + "',"
                + " url='" + t.getUrl() + "',"
                + " start='" + t.getStart() + "',"
                + " end='" + t.getEnd() + "',"
                + " lastUpdate='" + timestamp + "',"
                + " lastUpdateBy=" + currentUser.getUserId()
                + " WHERE appointmentId=" + t.getAppointmentId() + ";";
           System.err.println(t.getAppointmentId());
        try {
            stmt.executeUpdate(query);
            String logString = "User ID: " + currentUser.getUserId() + "(" + currentUser.getUserName() + ") updated an appointment\n"
                + "Appointment ID: " + t.getAppointmentId()+ "(" + t.getTitle()+ ")\n";
            new LogFile(logString);
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteCustomer(customer c) throws IOException{
        String query = "DELETE FROM appointment WHERE customerId=" + c.getId() + ";";
        try {
            stmt.executeUpdate(query);
            String logString = "User ID: " + currentUser.getUserId() + "(" + currentUser.getUserName() + ") deleted all appointments associated with:\nCustomer: " + c.getId() + "(" + c.getName() + ")";
            new LogFile(logString);
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Appointment t) {
        
        String query = "DELETE FROM appointment where appointmentId=" + t.getAppointmentId();
        try {
            stmt.executeUpdate(query);
            String logString = "User ID: " + currentUser.getUserId() + "(" + currentUser.getUserName() + ") deleted an appointment\n"
                + "Appointment ID: " + t.getAppointmentId() + "(" + t.getTitle() + ")\n";
            new LogFile(logString);

        } catch (SQLException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AppointmentDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Appointment get(int Id) {
        return appointments.get(Id);
    }
}