package UI;

import LOGIC.Config;
import LOGIC.Criteria;
import LOGIC.GradingSystem;
import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GradingController {
    Scene parent;
    public void setParent(Scene parent) {
        this.parent = parent;
    }
    GradingSystem gs;
    public void setGS(GradingSystem gs) {
        this.gs = gs;
    }
    @FXML
    Button courseName;
    @FXML
    VBox score;
    public void initial() {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        List<Criteria> criteria = gs.getCourseCriteria();
        HBox criteriaInfo = new HBox();
        criteriaInfo.setPrefHeight(25);
        Text NAME = new Text("Name");
        NAME.setFont(new Font(15));
        NAME.setWrappingWidth(100);
        NAME.setTextAlignment(TextAlignment.CENTER);
        criteriaInfo.getChildren().add(NAME);
        for (Criteria cri: criteria) {
            Text temp = new Text(cri.getLabel());
            temp.setFont(new Font(15));
            temp.setWrappingWidth(100);
            temp.setTextAlignment(TextAlignment.CENTER);
            criteriaInfo.getChildren().add(temp);
        }
        Text OVERALL = new Text("Overall");
        OVERALL.setFont(new Font(15));
        OVERALL.setWrappingWidth(100);
        OVERALL.setTextAlignment(TextAlignment.CENTER);
        criteriaInfo.getChildren().add(OVERALL);
        score.getChildren().add(criteriaInfo);
        HashMap<String, Double> overall = gs.getStudentOverall(Config.ALL, 0);
        for (String stuName: overall.keySet()) {
            HBox info = new HBox();
            info.setPrefHeight(25);
            Label name = new Label(stuName);
            name.setFont(new Font(14));
            name.setPrefHeight(25);
            name.setPrefWidth(100);
            name.setMnemonicParsing(false);
            name.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            name.setAlignment(Pos.CENTER);
            info.getChildren().add(name);
            for (Criteria cri: criteria) {
                HashMap<String, String> a = gs.grabCriteriaOverall(cri.getLabel());
                Label score = new Label(a.get(stuName));
                score.setFont(new Font(14));
                score.setPrefHeight(25);
                score.setPrefWidth(100);
                score.setMnemonicParsing(false);
                score.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
                score.setAlignment(Pos.CENTER);
                info.getChildren().add(score);
            }
            Label all = new Label(String.valueOf(overall.get(stuName)));
            all.setFont(new Font(14));
            all.setPrefHeight(25);
            all.setPrefWidth(100);
            all.setMnemonicParsing(false);
            all.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            all.setAlignment(Pos.CENTER);
            info.getChildren().add(all);
            score.getChildren().add(info);
        }
    }

    public void goBack() throws IOException {
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(parent);
    }

    public void initial(String assignment, String criteria, String name, String filter) {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
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

    public void courseMenu() throws IOException {
        FXMLLoader courseMenu = new FXMLLoader(getClass().getResource("CourseMenu.fxml"));
        Parent active_fxml = courseMenu.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        CourseMenuController courseMenuController = courseMenu.getController();
        courseMenuController.setGs(gs);
        courseMenuController.setParent(parent);
        courseMenuController.initializer();
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
}
