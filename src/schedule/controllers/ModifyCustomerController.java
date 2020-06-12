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

public class ModifyCustomerController implements Initializable {

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField customerNameTF;
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
    @FXML
    private Label customerNameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label countryLabel;
    @FXML
    private Label zipLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label address2Label;
    private customer passedCustomer;

    public ModifyCustomerController() throws SQLException, IOException {
        this.customerDao = new CustomerDAO();
    }

    @FXML
    public void handleSaveButton(ActionEvent event) throws SQLException{
        if (validateInput()){
            
            passedCustomer.setName(customerNameTF.getText());
            passedCustomer.setAddress(addressTF.getText());
            passedCustomer.setAddress2(address2TF.getText());
            passedCustomer.setCity(cityTF.getText());
            passedCustomer.setZip(Integer.parseInt(zipTF.getText()));
            passedCustomer.setCountry(countryTF.getText());
            
            customerDao.update(passedCustomer);
            
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
    
    @FXML
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
    
   
    
    private boolean validateInput(){
        boolean valid = false;
        
        if (!customerNameTF.getText().isEmpty() && !addressTF.getText().isEmpty() && !cityTF.getText().isEmpty() && !countryTF.getText().isEmpty() && !zipTF.getText().isEmpty() && !phoneTF.getText().isEmpty())
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        passedCustomer = MainController.getSelectedCustomer();
        if (passedCustomer != null) {
            customerNameTF.setText(passedCustomer.getName());
            addressTF.setText(passedCustomer.getAddress());
            address2TF.setText(passedCustomer.getAddress2());
            cityTF.setText(passedCustomer.getCity());
            countryTF.setText(passedCustomer.getCountry());
            zipTF.setText(String.valueOf(passedCustomer.getZip()));
            phoneTF.setText(passedCustomer.getPhone());
        }
        
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        Platform.runLater(() -> {
            primaryStage = (Stage)zipTF.getScene().getWindow();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);   
            primaryStage.setResizable(false);
            });    
    }    
}
