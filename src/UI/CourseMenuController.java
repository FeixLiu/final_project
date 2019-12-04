package UI;

import LOGIC.Criteria;
import LOGIC.GradingSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CourseMenuController {
    @FXML
    Button courseName;
    @FXML
    FlowPane criteriaPane;
    @FXML
    Label courseStatus;
    @FXML
    Label students;
    @FXML
    Label under;
    @FXML
    Label graduate;

    Scene parent;

    GradingSystem gs;

    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }

    public void setParent(Scene parent) {
        this.parent = parent;
    }

    public void modifyCriteria() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("ModifyCriteira.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        ModifyCriteriaController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGs(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initializer();
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void initializer() {
        courseName.setText(gs.getCurrent().getName());
        courseStatus.setText(gs.getCurrent().getStatus());
        List<Integer> info = gs.getCourseStudentsNumber();
        students.setText(String.valueOf(info.get(0)));
        under.setText(String.valueOf(info.get(1)));
        graduate.setText(String.valueOf(info.get(2)));
        List<Criteria> allCriteria = gs.getCourseCriteria();
        for (Criteria criteria: allCriteria) {
            Label cri = new Label();
            cri.setText(criteria.getLabel() + ": ");
            cri.setPrefHeight(40);
            cri.setPrefWidth(150);
            cri.setFont(new Font(25));
            Label percentage = new Label();
            percentage.setText(String.valueOf(criteria.getPercentage()));
            percentage.setPrefHeight(40);
            percentage.setPrefWidth(150);
            percentage.setFont(new Font(25));
            criteriaPane.getChildren().add(cri);
            criteriaPane.getChildren().add(percentage);
        }
    }

    public void goBack() throws IOException {
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(parent);
    }
}
