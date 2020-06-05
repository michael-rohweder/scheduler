/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author micha
 */
public class Schedule extends Application {
    private static Stage PrimaryStage;
    
    private void setPrimaryStage(Stage stage){
        Schedule.PrimaryStage = stage;
    }
    
    static public Stage getPrimaryStage() {
        return Schedule.PrimaryStage;
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        setPrimaryStage(stage);
        Parent root = FXMLLoader.load(getClass().getResource("views/logInScreen.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
