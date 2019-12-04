package UI;

import LOGIC.GradingSystem;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;

public class InActiveCoursesController {
    Scene active;

    public void setActive(Scene active) {
        this.active = active;
    }

    @FXML
    Button Inactive;

    GradingSystem gs;

    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }

    public void active() throws IOException {
        Stage window = (Stage) Inactive.getScene().getWindow();
        window.setScene(active);
    }
}
