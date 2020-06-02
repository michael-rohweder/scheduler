
package schedule;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
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

    
    public void handleLogInButton() {
        if (uNameTF.getText().equals("test") && passwordTF.getText().equals("test")){
            messageBannerLabel.setVisible(false);
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
    }    
}
