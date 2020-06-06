package schedule.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Screen;
import javafx.stage.Stage;
import schedule.DAO.CustomerDAO;
import schedule.DataBase;
import schedule.User;
import schedule.customer;

public class MainController implements Initializable {

    @FXML
    private TextField customerSearchTF;
    @FXML
    private Button customerSearchButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button modifyCustomerButton;
    @FXML
    private Button deleteCustomerButton;
    @FXML
    private TableColumn<customer, String> nameCol;
    @FXML
    private TableColumn<customer, String> addressCol;
    @FXML
    private TableColumn<customer, String> address2Col;
    @FXML
    private TableColumn<customer, String> cityCol;
    @FXML
    private TableColumn<customer, String> countryCol;
    @FXML
    private TableColumn<customer, Integer> zipCol;
    @FXML
    private TableColumn<customer, String> phoneCol;
    @FXML
    private TableView customerTV;
    @FXML
    private Stage primaryStage;
    private User currentUser = LogInScreenController.getCurrentUser();
    private final CustomerDAO customerDao;
    private static customer selectedCustomer;
    
    
    public static ObservableList<customer> customerList = FXCollections.observableArrayList();

    public MainController() throws SQLException, IOException {
        this.customerDao = new CustomerDAO();
        
    }

    public static customer getSelectedCustomer(){
        return selectedCustomer;
    }
    
    public void handleCustomerDeleteButton(ActionEvent event) throws SQLException{
        selectedCustomer = (customer) customerTV.getSelectionModel().getSelectedItem();
        
        
        customerDao.delete(selectedCustomer);
        customerList.remove(selectedCustomer);
    }
    
    public void handleCustomerModifyButton(ActionEvent event){
        selectedCustomer = (customer) customerTV.getSelectionModel().getSelectedItem();
        try {
            Parent mainParent = FXMLLoader.load(getClass().getClassLoader().getResource("schedule/views/modifyCustomer.fxml"));
            Scene mainScene = new Scene(mainParent);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScene);
            mainStage.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void handleCustomerSearchButton(ActionEvent event){
        if (customerSearchButton.getText().equals("Search")) {
            customerSearchButton.setText("Clear Search");
            customerSearchTF.editableProperty().set(false);
            String searchString = customerSearchTF.getText();
            
            //Stream & Lambda to filter the customer list based on search terms
            ObservableList<customer> filteredList = customerList.stream()
                    .filter(s -> s.getName().toLowerCase().contains(searchString.toLowerCase()) || 
                                 s.getAddress().toLowerCase().contains(searchString.toLowerCase()) ||
                                 s.getCity().toLowerCase().contains(searchString.toLowerCase()) ||
                                 s.getCountry().toLowerCase().contains(searchString.toLowerCase()) ||
                                 String.valueOf(s.getZip()).contains(searchString) ||
                                 s.getPhone().contains(searchString))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            customerTV.setItems(filteredList);
        } else {
            customerTV.setItems(customerList);
            customerSearchTF.setText("");
            customerSearchTF.editableProperty().set(true);
            customerSearchButton.setText("Search");
        }
    }
    
    public void handleAddCustomerButton(ActionEvent event){
        try {
            Parent mainParent = FXMLLoader.load(getClass().getClassLoader().getResource("schedule/views/addCustomer.fxml"));
            Scene mainScene = new Scene(mainParent);
            Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            mainStage.setScene(mainScene);
            mainStage.show();
        } catch (IOException ex) {
            Logger.getLogger(LogInScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void handleExitButton(ActionEvent event) {
        System.exit(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        customerList = customerDao.getAll();
        
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        address2Col.setCellValueFactory(new PropertyValueFactory<>("address2"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));
        zipCol.setCellValueFactory(new PropertyValueFactory<>("zip"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerTV.setItems(customerList);
        
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        
        //delay execution until after initialized - centers window in the screen.  Using lambda to make code more readable and not require anon inner class
        Platform.runLater(() -> {
            primaryStage = (Stage)addCustomerButton.getScene().getWindow();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);    
            });
    }
}