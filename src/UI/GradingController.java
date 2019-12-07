package UI;

import LOGIC.Assignment;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
    @FXML
    MenuButton criteria;
    @FXML
    MenuButton assignment;
    @FXML
    MenuButton student;
    @FXML
    MenuButton filter;
    public void initial() {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        List<Criteria> criteria = gs.getCourseCriteria();
        HBox criteriaInfo = new HBox();
        criteriaInfo.setPrefHeight(25);
        Text NAME = new Text("Name");
        NAME.setFont(new Font(15));
        NAME.setWrappingWidth(140);
        NAME.setTextAlignment(TextAlignment.CENTER);
        criteriaInfo.getChildren().add(NAME);
        for (Criteria cri: criteria) {
            Text temp = new Text(cri.getLabel());
            temp.setFont(new Font(15));
            temp.setWrappingWidth(140);
            temp.setTextAlignment(TextAlignment.CENTER);
            criteriaInfo.getChildren().add(temp);
        }
        Text OVERALL = new Text("Overall");
        OVERALL.setFont(new Font(15));
        OVERALL.setWrappingWidth(140);
        OVERALL.setTextAlignment(TextAlignment.CENTER);
        criteriaInfo.getChildren().add(OVERALL);
        score.getChildren().add(criteriaInfo);
        HashMap<String, Double> overall = gs.getStudentOverall(Config.ALL, 0);
        HashMap<String, HashMap<String, Double>> allGrad = gs.grabAllGrad();
        for (String stuName: overall.keySet()) {
            HBox info = new HBox();
            info.setPrefHeight(25);
            Label name = new Label(stuName);
            name.setFont(new Font(14));
            name.setPrefHeight(25);
            name.setPrefWidth(140);
            name.setMnemonicParsing(false);
            name.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            name.setAlignment(Pos.CENTER);
            info.getChildren().add(name);
            HashMap<String, Double> stuGrade = allGrad.get(stuName);
            for (Criteria cri: criteria) {
                Label score = new Label(String.valueOf(stuGrade.get(cri.getLabel())));
                score.setFont(new Font(14));
                score.setPrefHeight(25);
                score.setPrefWidth(140);
                score.setMnemonicParsing(false);
                score.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
                score.setAlignment(Pos.CENTER);
                info.getChildren().add(score);
            }
            Label all = new Label(String.valueOf(overall.get(stuName)));
            all.setFont(new Font(14));
            all.setPrefHeight(25);
            all.setPrefWidth(140);
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

    public void initial(String assignment, String criteria, String name, String sorting) throws IOException {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        HashMap<String, HashMap<String, Double>> grade = null;
        HashMap<Assignment, List<Double>> assignments = gs.getAllAssignment();
        if (!criteria.equals("N"))
            grade = gs.grabByCriteria(criteria);
        if (!assignment.equals("N"))
            grade = gs.grabByCriteriaAndName(criteria, assignment);
        if (criteria.equals("N") && assignment.equals("N"))
            grade = gs.grabAllGrad();
        if (grade == null) {
            goBack();
            return;
        }
        HashMap<String, Double> temp = null;
        for (String a: grade.keySet()) {
            temp = grade.get(a);
            break;
        }
        if (temp == null) {
            goBack();
            return;
        }
        List<String> title = new ArrayList<>(temp.keySet());
        Collections.sort(title);
        if (title.contains("Overall")) {
            title.remove("Overall");
            title.add("Overall");
        }
        HBox criteriaInfo = new HBox();
        criteriaInfo.setPrefHeight(25);
        Text NAME = new Text("Name");
        NAME.setFont(new Font(15));
        NAME.setWrappingWidth(140);
        NAME.setTextAlignment(TextAlignment.CENTER);
        criteriaInfo.getChildren().add(NAME);
        for (String cri: title) {
            Text cur = new Text(cri);
            cur.setFont(new Font(15));
            cur.setWrappingWidth(140);
            cur.setTextAlignment(TextAlignment.CENTER);
            criteriaInfo.getChildren().add(cur);
        }
        score.getChildren().add(criteriaInfo);
        for (String stuName: grade.keySet()) {
            if (!name.equals("N") && !stuName.equals(name))
                continue;
            HBox info = new HBox();
            info.setPrefHeight(25);
            Label thename = new Label(stuName);
            thename.setFont(new Font(14));
            thename.setPrefHeight(25);
            thename.setPrefWidth(140);
            thename.setMnemonicParsing(false);
            thename.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            thename.setAlignment(Pos.CENTER);
            info.getChildren().add(thename);
            HashMap<String, Double> stuGrade = grade.get(stuName);
            for (String cri: title) {
                if (assignment.equals("N") || criteria.equals("N")) {
                    Label score = new Label(String.valueOf(stuGrade.get(cri)));
                    score.setFont(new Font(14));
                    score.setPrefHeight(25);
                    score.setPrefWidth(140);
                    score.setMnemonicParsing(false);
                    score.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
                    score.setAlignment(Pos.CENTER);
                    info.getChildren().add(score);
                }
                else {
                    TextField score = new TextField();
                    if (cri.contains("percentage"))
                        score.setText(String.valueOf(stuGrade.get(cri)));
                    else {
                        for (Assignment ass: assignments.keySet()) {
                            if (ass.getName().equals(assignment)) {
                                score.setText(String.valueOf(stuGrade.get(cri) - ass.getTotal()));
                                break;
                            }
                        }
                    }
                    score.setFont(new Font(12));
                    score.setPrefHeight(30);
                    score.setPrefWidth(140);
                    score.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
                    score.setAlignment(Pos.CENTER);
                    info.getChildren().add(score);
                }
            }
            score.getChildren().add(info);
        }
    }

    public void save() {

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
