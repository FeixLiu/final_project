package UI;

import LOGIC.Assignment;
import LOGIC.Criteria;
import LOGIC.GradingSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class AssignmentsController {
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
    VBox assignments;
    @FXML
    Button add;
    @FXML
    MenuButton filter;

    public void initial() {
        this.initial("ALL");
    }

    public void initial(String filter) {
        this.filter.getItems().clear();
        this.filter.setText(filter);
        if (!filter.equals("ALL")) {
            MenuItem mi = new MenuItem("ALL");
            mi.setOnAction(actionEvent -> {
                try {
                    reload("ALL");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.filter.getItems().add(mi);
        }
        List<Criteria> allcriteria = gs.getCourseCriteria();
        for (Criteria cri: allcriteria) {
            if (cri.getLabel().equals(filter))
                continue;
            MenuItem mi = new MenuItem(cri.getLabel());
            mi.setOnAction(actionEvent -> {
                try {
                    reload(cri.getLabel());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.filter.getItems().add(mi);
        }
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        HashMap<Assignment, List<Double>> assignments = gs.getAllAssignment();
        List<Map.Entry<Assignment, List<Double>>> temp = new ArrayList<>(assignments.entrySet());
        temp.sort(((o1, o2) -> {
            Assignment a1 = o1.getKey();
            Assignment a2 = o2.getKey();
            String s1 = a1.getCriteria().getLabel();
            String s2 = a2.getCriteria().getLabel();
            char[] chars1 = s1.toCharArray();
            char[] chars2 = s2.toCharArray();
            int i = 0;
            while (i < chars1.length && i < chars2.length) {
                if (chars1[i] > chars2[i]) {
                    return 1;
                } else if (chars1[i] < chars2[i]) {
                    return -1;
                } else {
                    i++;
                }
            }
            if (i == chars1.length)
                return -1;
            if (i == chars2.length)
                return 1;
            return 0;
        }));
        for (Map.Entry<Assignment, List<Double>> cur: temp) {
            if (!filter.equals("ALL")) {
                if (!cur.getKey().getCriteria().getLabel().equals(filter))
                    continue;
            }
            Assignment assignment = cur.getKey();
            HBox info = new HBox();
            Button name = new Button(assignment.getName());
            name.setOnAction(actionEvent -> {
                try {
                    clickOnName(assignment);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            name.setFont(new Font(14));
            name.setPrefHeight(25);
            name.setPrefWidth(150);
            name.setMnemonicParsing(false);
            name.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button criteria = new Button(assignment.getCriteria().getLabel());
            criteria.setFont(new Font(14));
            criteria.setPrefHeight(25);
            criteria.setPrefWidth(150);
            criteria.setMnemonicParsing(false);
            criteria.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button assigned = new Button(assignment.getAssigned().getDate());
            assigned.setFont(new Font(14));
            assigned.setPrefHeight(25);
            assigned.setPrefWidth(130);
            assigned.setMnemonicParsing(false);
            assigned.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button average = new Button(String.valueOf(cur.getValue().get(0)));
            average.setFont(new Font(14));
            average.setPrefHeight(25);
            average.setPrefWidth(110);
            average.setMnemonicParsing(false);
            average.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button mid = new Button(String.valueOf(cur.getValue().get(1)));
            mid.setFont(new Font(14));
            mid.setPrefHeight(25);
            mid.setPrefWidth(110);
            mid.setMnemonicParsing(false);
            mid.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button standard = new Button(String.valueOf(cur.getValue().get(2)));
            standard.setFont(new Font(14));
            standard.setPrefHeight(25);
            standard.setPrefWidth(110);
            standard.setMnemonicParsing(false);
            standard.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button edit = new Button();
            edit.setOnAction(actionEvent -> {
                try {
                    edit(assignment);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            edit.setStyle("-fx-background-color: transparent; -fx-border-width: 1 1 1 1;");
            Image im = new Image("UI/Icon/baseline_create_black_17dp.png");
            edit.setPrefHeight(25);
            edit.setPrefWidth(50);
            edit.setGraphic(new ImageView(im));
            info.getChildren().add(name);
            info.getChildren().add(criteria);
            info.getChildren().add(assigned);
            info.getChildren().add(average);
            info.getChildren().add(mid);
            info.getChildren().add(standard);
            info.getChildren().add(edit);
            this.assignments.getChildren().add(info);
        }
    }

    public void clickOnName(Assignment assignment) throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Grading.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        GradingController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial(assignment.getName(), assignment.getCriteria().getLabel(), "N", "N");
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void reload(String filter) throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Assignments.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        AssignmentsController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial(filter);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void edit(Assignment assignment) throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("ModifyAssignment.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        ModifyAssignmentController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial(assignment);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void add() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("AddAssignment.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        AddAssignmentController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        List<String> temp = new ArrayList<>();
        temp.add("");
        temp.add("");
        temp.add("");
        temp.add("");
        List<String> cri = new ArrayList<>();
        List<Criteria> criteria = gs.getCourseCriteria();
        for (Criteria c: criteria)
            cri.add(c.getLabel());
        modifyCriteriaController.initial(true, temp, cri);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }


    public void goBack() throws IOException {
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(parent);
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
}
