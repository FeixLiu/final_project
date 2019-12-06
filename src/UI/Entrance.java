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
        gs.addCourse("640", criteria, "Fall", "2018");
        gs.archiveCourse("640", "2018", "Fall");
        gs.addCourse("591", criteria, "Fall", "2017");
        gs.archiveCourse("591", "2017", "Fall");
        gs.addCourse("591", criteria, "Spring", "2016");
        gs.archiveCourse("591", "2016", "Spring");
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
        gs.chooseCourse("591", "2019", "Fall");
        gs.addStudentsFromFile("./src/student.csv");
        gs.addSingleAssignment("First", "LOGIC.Assignment", "2019", "12", "21", 20, 70);
        gs.addSingleAssignment("Second", "LOGIC.Assignment", "2019", "12", "21", 20, 50);
        List<Double> part = new ArrayList<>(); part.add(60.0); part.add(60.0);
        gs.addMultiAssignment("Midterm", "Exam", part, "2019", "12", "30", 50, 90);
        gs.addMultiAssignment("Final", "Exam", part, "2019", "12", "30", 50, 80);
        gs.returnMain();


        FXMLLoader login_loader = new FXMLLoader(getClass().getResource("Login.fxml"));
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
