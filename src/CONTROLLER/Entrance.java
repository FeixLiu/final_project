package CONTROLLER;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import LOGIC.*;

public class Entrance extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        GradingSystem gs = new GradingSystem();

        FXMLLoader login_loader = new FXMLLoader(getClass().getResource("/UI/Login.fxml"));
        Parent login_fxml = login_loader.load();
        Scene login = new Scene(login_fxml, 1024, 768);
        LoginController login_control = (LoginController) login_loader.getController();

        login_control.setGs(gs);

        primaryStage.setTitle("Grading System Welcome");
        primaryStage.setScene(login);
        primaryStage.setWidth(1024.0);
        primaryStage.setHeight(780);
        primaryStage.show();
    }
}
