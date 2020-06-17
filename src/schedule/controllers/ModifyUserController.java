/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import schedule.DAO.UserDAO;
import schedule.User;

/**
 * FXML Controller class
 *
 * @author micha
 */
public class ModifyUserController implements Initializable {

    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField userNameTF;
    @FXML
    private PasswordField passwordTF;
    @FXML
    private PasswordField confirmPassWordTF;
    private UserDAO userDao;
    private User currentUser;
    private Stage primaryStage;
    private User selectedUser;
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedUser = MainController.getSelectedUser();
        
        currentUser = LogInScreenController.getCurrentUser();
        try {
            userDao = new UserDAO();
        } catch (SQLException ex) {
            Logger.getLogger(ModifyUserController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ModifyUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        userNameTF.setText(selectedUser.getUserName());
        
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        Platform.runLater(() -> {
            primaryStage = (Stage)saveButton.getScene().getWindow();
            primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
            primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);  
            primaryStage.setResizable(false);
        });
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
    
    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (!userNameTF.getText().isEmpty()){
            if (!passwordTF.getText().isEmpty()) {
                if (!confirmPassWordTF.getText().isEmpty()) {
                    if (passwordTF.getText().equals(confirmPassWordTF.getText())) {
                        User newUser = new User (selectedUser.getUserId(), userNameTF.getText(), passwordTF.getText(), selectedUser.getActive());
                        userDao.update(newUser);
                        loadScene("main.fxml");
                    }
                }
            }
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        loadScene("main.fxml");
    }
    
}
