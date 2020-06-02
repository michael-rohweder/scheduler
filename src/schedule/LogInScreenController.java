
package schedule;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

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
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;
    Stage currentStage; 
    Locale currentLocale;
    String errorString;
    public Stage primaryStage;

    
    public void handleLogInButton(ActionEvent event) {
        if (uNameTF.getText().equals("test") && passwordTF.getText().equals("test")){
            messageBannerLabel.setVisible(false);
            try {
                Parent mainParent = FXMLLoader.load(getClass().getResource("main.fxml"));
                Scene mainScene = new Scene(mainParent);
                Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                mainStage.setScene(mainScene);
                mainStage.show();
            } catch (IOException ex) {
                Logger.getLogger(LogInScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            messageBannerLabel.setVisible(true);
        } 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        currentLocale = Locale.getDefault();
        //currentLocale = new Locale("fr", "FR");  //UNCOMMENT FOR FRENCH
        currentStage = Schedule.getPrimaryStage();
        double layoutX;
        ResourceBundle bundle = ResourceBundle.getBundle("schedule/lang", currentLocale);
        
        userNameLabel.setText(bundle.getString("login"));
        passwordLabel.setText(bundle.getString("password"));
        logInButton.setText(bundle.getString("buttonText"));
        
        layoutX = userNameLabel.getLayoutX();
        uNameTF.setLayoutX(layoutX);
        passwordTF.setLayoutX(layoutX);
        passwordLabel.setLayoutX(layoutX);
        errorString = bundle.getString("errorMessage");
        messageBannerLabel.setText(errorString);
        messageBannerLabel.setStyle("-fx-border-color:red; -fx-background-color: white;");
        messageBannerLabel.setVisible(false);
        
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        Platform.runLater(() -> {
            primaryStage = (Stage)messageBannerLabel.getScene().getWindow();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);    
            });
    }    
}
