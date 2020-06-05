package schedule.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.ResourceBundle;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import schedule.DAO.CustomerDAO;
import schedule.DataBase;
import schedule.User;
import schedule.customer;

public class AddCustomerController implements Initializable {

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField fNameTF;
    @FXML
    private TextField lNameTF;
    @FXML
    private TextField addressTF;
    @FXML
    private TextField address2TF;
    @FXML
    private TextField cityTF;
    @FXML
    private TextField countryTF;
    @FXML
    private TextField zipTF;
    @FXML
    private TextField phoneTF;
  
    public Stage primaryStage;
    private User currentUser = LogInScreenController.getCurrentUser();
    private CustomerDAO customerDao;

    public AddCustomerController() throws SQLException {
        this.customerDao = new CustomerDAO();
    }

    public void handleSaveButton(ActionEvent event) throws SQLException{
        if (validateInput()){
            
            String customerName = fNameTF.getText() + " " + lNameTF.getText();
            customerDao.add(new customer(0,customerName, addressTF.getText(), address2TF.getText(), cityTF.getText(), countryTF.getText(),Integer.parseInt(zipTF.getText()), phoneTF.getText()), currentUser);
            
            try {
                    Parent mainParent = FXMLLoader.load(getClass().getClassLoader().getResource("schedule/views/main.fxml"));
                    Scene mainScene = new Scene(mainParent);
                    Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    mainStage.setScene(mainScene);
                    mainStage.show();
                } catch (IOException ex) {
                    Logger.getLogger(LogInScreenController.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
    
    public void handleCancelButton(ActionEvent event){
        try {
                Parent mainParent = FXMLLoader.load(getClass().getClassLoader().getResource("schedule/views/main.fxml"));
                Scene mainScene = new Scene(mainParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScene);
                mainStage.show();
            } catch (IOException ex) {
                Logger.getLogger(LogInScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        Platform.runLater(() -> {
            primaryStage = (Stage)zipTF.getScene().getWindow();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);    
            });    
    }    
    
    private boolean validateInput(){
        boolean valid = false;
        
        if (!fNameTF.getText().isEmpty() && !lNameTF.getText().isEmpty() && !addressTF.getText().isEmpty() && !cityTF.getText().isEmpty() && !countryTF.getText().isEmpty() && !zipTF.getText().isEmpty() && !phoneTF.getText().isEmpty())
        {
            if (isInt(zipTF.getText())){
                valid=true;
            } else {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setContentText("ZIP MUST BE AN INTEGER");
                alert.show();
                System.err.append("ZIP MUST BE INT.");
            }
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setContentText("All fields must be populated");
            alert.show();
            System.err.append("Fields can not be empty");
        }
        return valid;
    }
    
    private boolean isInt(String toTest){
        boolean valid=false;
        try {
            int test = Integer.parseInt(toTest);
            valid=true;
        } catch (Exception e){
            
        }
        return valid;
    }
}