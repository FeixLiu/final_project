package UI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import LOGIC.*;

import java.util.ArrayList;
import java.util.List;

public class Entrance extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        GradingSystem gs = new GradingSystem();
        Criteria a = new Criteria("Exam", 30);
        Criteria b = new Criteria("Assignment", 20);
        Criteria c = new Criteria("Test", 50);
        List<Criteria> criteria = new ArrayList<>();criteria.add(a); criteria.add(b); criteria.add(c);
        gs.addCourse("591", criteria, "Fall", "2018");
        gs.archiveCourse("591", "2018", "Fall");
        a = new Criteria("Exam", 30);
        b = new Criteria("Assignment", 20);
        c = new Criteria("Test", 50);
        criteria = new ArrayList<>();criteria.add(a); criteria.add(b); criteria.add(c);
        gs.addCourse("591", criteria, "Fall", "2019");
        a = new Criteria("Exam", 30);
        b = new Criteria("Assignment", 20);
        c = new Criteria("Test", 50);
        criteria = new ArrayList<>();criteria.add(a); criteria.add(b); criteria.add(c);
        gs.addCourse("640", criteria, "Fall", "2019");
        FXMLLoader login_loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent login_fxml = login_loader.load();
        Scene login = new Scene(login_fxml, 600, 400);
        LoginController login_control = (LoginController) login_loader.getController();

        login_control.setGs(gs);

        primaryStage.setTitle("Grading System Welcome");
        primaryStage.setScene(login);
        primaryStage.setWidth(1024.0);
        primaryStage.setHeight(768.0);
        primaryStage.show();
    }
}
