package schedule.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import schedule.DAO.CustomerDAO;
import schedule.LogFile;
import schedule.User;
import schedule.customer;

public class NewAppointmentController implements Initializable {

    @FXML
    private DatePicker appointmentDatePicker;
    @FXML
    private ComboBox<String> customerComboBox;
    @FXML
    private ComboBox<String> contactComboBox;
    @FXML
    private TextField appointmentTitle;
    @FXML
    private TextArea appointmentDescription;
    @FXML
    private ComboBox<String> locationComboBox;
    @FXML
    private TextField appointmentTypeTF;
    @FXML
    private TextField appointmentURLTF;
    @FXML
    private ComboBox<String> startTimeComboBox;
    @FXML
    private ComboBox<String> endTimeComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    
    LogFile logFile;
    Logger logger;
    public Stage primaryStage;
    private User currentUser = LogInScreenController.getCurrentUser();
    private CustomerDAO customerDao;
    private Stage mainStage;
    private ObservableList<customer> customerList = MainController.getCustomerList();
    private List<User> userList = LogInScreenController.getUsers();
    
    public NewAppointmentController() throws SQLException, IOException {
        this.logFile = new LogFile();
        logger = logFile.getLogger();
        this.customerDao = new CustomerDAO();
    }
    public void handleSaveButton(ActionEvent event) {
        
    }

    public void handleCancelButton(ActionEvent event){
        loadScene("main.fxml");
    }
    
    public void loadScene(String sceneName){
        sceneName = "schedule/views/" + sceneName;
         try {
            Parent mainParent = FXMLLoader.load(getClass().getClassLoader().getResource(sceneName));
            Scene mainScene = new Scene(mainParent);
            mainStage.setScene(mainScene);
            mainStage.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        List<String> time = new ArrayList<>();
        
        for (int am = 0; am < 2; am++){
            for (int hour = 1; hour <=12; hour++){
                for (int mins = 0; mins <60; mins+=15){
                    if (mins != 0) {
                        if (am==0) {
                            time.add(hour + ":" + mins + " AM");
                        } else {
                            time.add(hour + ":" + mins + " PM");
                        }
                    } else {
                        if (am==0){
                            time.add(hour + ":" + mins + "0 AM");
                        } else {
                            time.add(hour + ":" + mins + "0 PM");
                        }
                    }
                }
            }
        }
        
        
        customerList.forEach(c -> customerComboBox.getItems().add(c.getName()));
        userList.forEach(u -> contactComboBox.getItems().add(u.getUserName()));
        time.forEach(t -> startTimeComboBox.getItems().add(t));
        time.forEach(t -> endTimeComboBox.getItems().add(t));

        //Post Load processing
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        Platform.runLater(() -> {
            primaryStage = (Stage)saveButton.getScene().getWindow();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);    
            primaryStage.setResizable(false);
            mainStage = (Stage) saveButton.getScene().getWindow();
            });    
    }    
    
}
