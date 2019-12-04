package UI;

import LOGIC.GradingSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ActiveCoursesController {
    Scene courseMenu;

    @FXML
    FlowPane flowpane;

    @FXML
    Button Inactive;

    GradingSystem gs;

    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }

    public void test() throws IOException {
        Button test = new Button("test");
        flowpane.
    }

    public void inactive() throws IOException {
        FXMLLoader inactiveCourse = new FXMLLoader(getClass().getResource("InActiveCourses.fxml"));
        Parent active_fxml = inactiveCourse.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        InActiveCoursesController inactiveCoursesController = inactiveCourse.getController();
        inactiveCoursesController.setGs(gs);
        inactiveCoursesController.setActive(Inactive.getScene());
        Stage window = (Stage) Inactive.getScene().getWindow();
        window.setScene(active);
    }
}
