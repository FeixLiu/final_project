package UI;

import LOGIC.GradingSystem;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class CourseMenuController {
    @FXML
    Button courseName;

    Scene parent;

    GradingSystem gs;

    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }

    public void setParent(Scene parent) {
        this.parent = parent;
    }

    public void initializer() {
        courseName.setText(gs.getCurrentName());
    }

    public void goBack() throws IOException {
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(parent);
    }
}
