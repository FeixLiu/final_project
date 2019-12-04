package UI;

import LOGIC.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    GradingSystem gs;

    @FXML
    Button login;

    @FXML
    TextField pinText;

    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }

    public void reset() {
        pinText.setText("");
    }

    public void loginClicked() throws IOException {
        if(gs.logIn(pinText.getText())) {
            FXMLLoader activeCourse = new FXMLLoader(getClass().getResource("ActiveCourses.fxml"));
            Parent active_fxml = activeCourse.load();
            Scene active = new Scene(active_fxml, 1024, 768);
            ActiveCoursesController activeCoursesController = activeCourse.getController();
            activeCoursesController.setGs(gs);
            Stage window = (Stage) login.getScene().getWindow();
            window.setScene(active);
        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Wrong Password");
            alert.setHeaderText(null);
            alert.setContentText("Wrong Password\n\nPlease try again.");
            alert.showAndWait();
            reset();
        }
    }
}
