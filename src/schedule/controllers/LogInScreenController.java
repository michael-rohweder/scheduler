
package schedule.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import schedule.DataBase;
import schedule.LogFile;
import schedule.Schedule;
import schedule.User;

/**
 * FXML Controller class
 *
 * @author micha
 */
public class LogInScreenController implements Initializable {

    @FXML
    private Button logInButton;
    @FXML
    private TextField uNameTF;
    @FXML
    private TextField passwordTF;
    @FXML
    private Label messageBannerLabel;
    @FXML
    private Label employeeLoginLabel;
    
    Stage currentStage; 
    Locale currentLocale;
    String errorString;
    private Stage primaryStage;
    private static Optional<User> currentUser=null;
    private static List<User> authorizedUsers;
    private boolean validLogin = false;
    private DataBase db;
    private Statement stmt;
    private LogFile logFile;
    private Logger logger;
    
    
    public static User getCurrentUser(){
        return currentUser.get();
    }

    
    public void handleLogInButton(ActionEvent event) {
        
        if (!authorizedUsers.isEmpty()) {
            validLogin = authorizedUsers.stream()
                                        .anyMatch(s -> s.getUserName().equals(uNameTF.getText()) && s.getPassword().equals(passwordTF.getText()));
        } else {
            validLogin = false;
        }

        if (validLogin) {
            
            String logFile = "LOG IN SUCCESSFUL:\n"
                + "UserName: " + uNameTF.getText() + "\n";
            logger.info(logFile);  
            
            currentUser = authorizedUsers.stream()
                                         .filter(s -> s.getUserName().equals(uNameTF.getText()))
                                         .findFirst();
            messageBannerLabel.setVisible(false);
            loadScene("main.fxml");
            this.logFile.closeLog();
        } else {
            messageBannerLabel.setVisible(true);
            String logFile = "INVALID LOG IN ATTEMPT:\n"
                + "UserName: " + uNameTF.getText() + "\n"
                + "Password: " + passwordTF.getText() + "\n";
            logger.warning(logFile);
        } 
    }
    
    public void loadScene(String sceneName){
        sceneName = "schedule/views/" + sceneName;
         try {
            Parent mainParent = FXMLLoader.load(getClass().getClassLoader().getResource(sceneName));
            Scene mainScene = new Scene(mainParent);
            primaryStage.setScene(mainScene);
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static List getUsers (){
        return authorizedUsers;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            this.logFile = new LogFile();
        } catch (IOException ex) {
            Logger.getLogger(LogInScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        logger = logFile.getLogger();
        authorizedUsers = new ArrayList<>();
        db = new DataBase();
        stmt=null;
        try {
            stmt = db.createConnection();
        } catch (SQLException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("*********ERROR***********: " + ex);
        }
        try {
            ResultSet rs = stmt.executeQuery("SELECT userId, userName, password FROM user");
            while (rs.next()){
                authorizedUsers.add(new User(rs.getInt("userId"), rs.getString("userName"), rs.getString("password")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(LogInScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        currentLocale = Locale.getDefault();
        //currentLocale = new Locale("fr", "FR");  //UNCOMMENT FOR FRENCH
        currentStage = Schedule.getPrimaryStage();
        ResourceBundle bundle = ResourceBundle.getBundle("schedule/lang", currentLocale);
        
        uNameTF.setPromptText(bundle.getString("login"));
        passwordTF.setPromptText(bundle.getString("password"));
        logInButton.setText(bundle.getString("buttonText"));
        
        employeeLoginLabel.setText(bundle.getString("employeeText"));
        
        errorString = bundle.getString("errorMessage");
        messageBannerLabel.setText(errorString);
        messageBannerLabel.setStyle("-fx-border-color:red; -fx-background-color: white;");
        messageBannerLabel.setVisible(false);
        
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        Platform.runLater(() -> {
            primaryStage = (Stage)messageBannerLabel.getScene().getWindow();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);  
            primaryStage.setResizable(false);
            });
    }    
}
