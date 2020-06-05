/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author micha
 */
public class DataBase {
    
    public Statement createConnection() throws SQLException{
        String URL = "jdbc:mysql://3.227.166.251/U05pbN";
        String dbUName = "U05pbN";
        String dbPass = "53688570724";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con =  DriverManager.getConnection(URL,dbUName,dbPass);
            return con.createStatement();
        } catch (Exception e) { System.err.append("*****ERROR******" + ":  " + e); return null;}
    }
}