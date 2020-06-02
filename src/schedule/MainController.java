package schedule;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private TableColumn<customer, String> lNameCol;
    @FXML
    private TableColumn<customer, String> fNameCol;
    @FXML
    private TableColumn<customer, String> addressCol;
    @FXML
    private TableColumn<customer, String> cityCol;
    @FXML
    private TableColumn<customer, String> stateCol;
    @FXML
    private TableColumn<customer, Integer> zipCol;
    @FXML
    private TableColumn<customer, String> phoneCol;
    @FXML
    private TableView customerTV;
    @FXML
    private Button exitButton;
    public Stage primaryStage;
    
    public static ObservableList<customer> customerList = FXCollections.observableArrayList();

    public void handleCustomerDeleteButton(ActionEvent event){
        customer selectedCustomer = (customer) customerTV.getSelectionModel().getSelectedItem();
        customerList.remove(selectedCustomer);
    }
    public void handleCustomerSearchButton(ActionEvent event){
        if (customerSearchButton.getText().equals("Search")) {
            customerSearchButton.setText("Clear Search");
            customerSearchTF.editableProperty().set(false);
            String searchString = customerSearchTF.getText();
            //Stream & Lambda to filter the customer list based on search terms
            ObservableList<customer> filteredList = customerList.stream()
                    .filter(s -> s.getFName().toLowerCase().contains(searchString.toLowerCase()) || 
                                 s.getLName().toLowerCase().contains(searchString.toLowerCase()) || 
                                 s.getAddress().toLowerCase().contains(searchString.toLowerCase()) ||
                                 s.getCity().toLowerCase().contains(searchString.toLowerCase()) ||
                                 s.getState().toLowerCase().contains(searchString.toLowerCase()) ||
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
                Parent mainParent = FXMLLoader.load(getClass().getResource("addCustomer.fxml"));
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
        
        lNameCol.setCellValueFactory(new PropertyValueFactory<>("lName"));
        fNameCol.setCellValueFactory(new PropertyValueFactory<>("fName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        stateCol.setCellValueFactory(new PropertyValueFactory<>("state"));
        zipCol.setCellValueFactory(new PropertyValueFactory<>("zip"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customerTV.setItems(customerList);
        
        
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        //delay execution until after initialized - centers window in the screen.  Using lambda to make code more readable and not requre anon inner class
        Platform.runLater(() -> {
            primaryStage = (Stage)addCustomerButton.getScene().getWindow();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);    
            });
    }
}