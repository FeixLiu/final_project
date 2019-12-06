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
import java.util.Collections;
import java.util.Comparator;
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

    public void goStudent() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Students.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        StudentsController studentsController = modify.getController();
        studentsController.setGs(gs);
        studentsController.setParent(parent);
        String[] order = new String[3];
        order[0] = "BUID";
        order[1] = "Name";
        order[2] = "Email";
        studentsController.initializer(order);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void goAssignment() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Assignments.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        AssignmentsController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial();
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void goGrading() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Grading.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        GradingController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial();
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void goStatistic() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Statistics.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        StatisticsController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial();
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void initializer() {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        courseStatus.setText(gs.getCurrent().getStatus());
        List<Integer> info = gs.getCourseStudentsNumber();
        students.setText(String.valueOf(info.get(0)));
        under.setText(String.valueOf(info.get(1)));
        graduate.setText(String.valueOf(info.get(2)));
        List<Criteria> allCriteria = gs.getCourseCriteria();
        allCriteria.sort((o1, o2) -> Double.compare(o2.getPercentage(), o1.getPercentage()));
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
