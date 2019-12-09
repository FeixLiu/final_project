package CONTROLLER;

import LOGIC.Config;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatisticsController {
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
    MenuButton menuButton;
    @FXML
    MenuItem item1;
    @FXML
    MenuItem item2;
    @FXML
    VBox overall;
    @FXML
    Button average;
    @FXML
    Button mid;
    @FXML
    Button std;
    public void initial() {
        List<String> order = new ArrayList<>();
        order.add(Config.ALL);
        order.add(Config.GRADUATE);
        order.add(Config.UNDERGRADUATE);
        initial(Config.ALL, order);
    }

    HashMap<String, Double> all;
    List<String> unselect = new ArrayList<>();

    public void initial(String kind, List<String> order) {
        menuButton.setText(order.get(0));
        for (int i = 1; i < order.size(); i++) {
            MenuItem mi = new MenuItem(order.get(i));
            int finalI = i;
            mi.setOnAction(actionEvent -> {
                try {
                    click(order.get(finalI));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            menuButton.getItems().add(mi);
        }
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        all = gs.getStudentOverall(kind, 0);
        for (String name: all.keySet()) {
            HBox info = new HBox();
            info.setPrefHeight(25);
            info.setPrefWidth(440);
            Button edit = new Button();
            edit.setStyle("-fx-background-color: transparent; -fx-border-width: 1 1 1 1;");
            Image im = new Image("UI/Icon/baseline_check_box_black_18dp.png");
            edit.setPrefHeight(25);
            edit.setPrefWidth(100);
            edit.setGraphic(new ImageView(im));
            edit.setOnAction(actionEvent -> {
                this.unselect(info, false);
            });
            Button stuName = new Button(name);
            stuName.setOnAction(actionEvent -> {
                try {
                    clickOnName(name);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            stuName.setFont(new Font(14));
            stuName.setPrefHeight(25);
            stuName.setPrefWidth(170);
            stuName.setMnemonicParsing(false);
            stuName.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button score = new Button(String.valueOf(all.get(name)));
            score.setFont(new Font(14));
            score.setPrefHeight(25);
            score.setPrefWidth(170);
            score.setMnemonicParsing(false);
            score.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            info.getChildren().add(edit);
            info.getChildren().add(stuName);
            info.getChildren().add(score);
            overall.getChildren().add(info);
        }
        List<Double> statistical = gs.getStudentStatistical(unselect, all);
        average.setText(String.valueOf(statistical.get(0)));
        mid.setText(String.valueOf(statistical.get(1)));
        std.setText(String.valueOf(statistical.get(2)));
    }

    public void unselect(HBox name, boolean select) {
        Image im;
        if (!select) {
            im = new Image("UI/Icon/baseline_check_box_outline_blank_black_18dp.png");
            ((Button)name.getChildren().get(0)).setOnAction(actionEvent -> {
                unselect(name, true);
            });
            ((Button)name.getChildren().get(0)).setGraphic(new ImageView(im));
            String stu = ((Button)name.getChildren().get(1)).getText();
            unselect.add(stu);
            List<Double> statistical = gs.getStudentStatistical(unselect, all);
            average.setText(String.valueOf(statistical.get(0)));
            mid.setText(String.valueOf(statistical.get(1)));
            std.setText(String.valueOf(statistical.get(2)));
        }
        else {
            im = new Image("UI/Icon/baseline_check_box_black_18dp.png");
            ((Button)name.getChildren().get(0)).setOnAction(actionEvent -> {
                unselect(name, false);
            });
            ((Button)name.getChildren().get(0)).setGraphic(new ImageView(im));
            String stu = ((Button)name.getChildren().get(1)).getText();
            unselect.remove(stu);
            List<Double> statistical = gs.getStudentStatistical(unselect, all);
            average.setText(String.valueOf(statistical.get(0)));
            mid.setText(String.valueOf(statistical.get(1)));
            std.setText(String.valueOf(statistical.get(2)));
        }
    }

    public void clickOnName(String name) throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/Grading.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        GradingController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial("N", "N", name, "N");
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void goBack() throws IOException {
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(parent);
    }


    public void goStudent() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/Students.fxml"));
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

    public void click(String item) throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/Statistics.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        StatisticsController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        List<String> order = new ArrayList<>();
        order.add(item);
        for (MenuItem node: menuButton.getItems()) {
            if (node.getText().equals(item))
                continue;
            order.add(node.getText());
        }
        order.add(menuButton.getText());
        modifyCriteriaController.initial(item, order);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void courseMenu() throws IOException {
        FXMLLoader courseMenu = new FXMLLoader(getClass().getResource("/UI/CourseMenu.fxml"));
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
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/Assignments.fxml"));
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
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/Grading.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        GradingController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial();
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }
}
