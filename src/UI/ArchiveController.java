package UI;

import LOGIC.Config;
import LOGIC.Course;
import LOGIC.GradingSystem;
import LOGIC.Student;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArchiveController {
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
    Button cancel;
    @FXML
    Button archive;
    @FXML
    Button save;
    @FXML
    Button add;
    @FXML
    TextField curve;
    @FXML
    VBox finalGrade;


    public void initial(double curve) {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        String status = gs.getCurrent().getStatus();
        if (status.equals(Config.INACTIVE_COURSE)) {
            archive.setVisible(false);
            archive.setOnAction(actionEvent -> {});
        }
        this.curve.setText(String.valueOf(curve));
        HashMap<String, Double> grade = gs.getStudentOverall(Config.ALL, curve);
        HashMap<String, Double> bouns = gs.getBonus();
        HashMap<String, String> comments = gs.getComment();
        HashMap<String, Character> letterGrade = gs.getFinalGrade();
        List<Student> students = gs.getAllStudent();
        students.sort((o1, o2) -> {
            String s1;
            String s2;
            s1 = o1.getId().getId();
            s2 = o2.getId().getId();
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
        });
        for (Student s: students) {
            HBox info = new HBox();
            info.setPrefHeight(30);
            Label buid = new Label(s.getId().getId());
            buid.setFont(new Font(14));
            buid.setPrefHeight(30);
            buid.setPrefWidth(160);
            buid.setMnemonicParsing(false);
            buid.setAlignment(Pos.CENTER);
            buid.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button fullname = new Button(s.getName().getName());
            fullname.setFont(new Font(14));
            fullname.setPrefHeight(30);
            fullname.setPrefWidth(160);
            fullname.setMnemonicParsing(false);
            fullname.setAlignment(Pos.CENTER);
            if (comments.get(s.getName().getName()).length() == 0)
                fullname.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            else {
                fullname.setStyle("-fx-background-color: #b9ffff; -fx-border-color: black; -fx-border-width: 1 1 1 1");
                fullname.setOnAction(actionEvent -> {
                    showComment(comments.get(s.getName().getName()), s.getName().getName());
                });
            }
            Label rawScore = new Label(String.valueOf(grade.get(s.getName().getName())));
            rawScore.setFont(new Font(14));
            rawScore.setPrefHeight(30);
            rawScore.setPrefWidth(160);
            rawScore.setMnemonicParsing(false);
            rawScore.setAlignment(Pos.CENTER);
            rawScore.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Label bonus = new Label(String.valueOf(bouns.get(s.getName().getName())));
            bonus.setFont(new Font(14));
            bonus.setPrefHeight(30);
            bonus.setPrefWidth(160);
            bonus.setMnemonicParsing(false);
            bonus.setAlignment(Pos.CENTER);
            bonus.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            MenuButton letter = new MenuButton();
            letter.setFont(new Font(14));
            letter.setPrefHeight(25);
            letter.setPrefWidth(160);
            letter.setId(s.getName().getName());
            letter.setAlignment(Pos.CENTER);
            letter.setText(String.valueOf(letterGrade.get(s.getName().getName())));
            List<String> letters = new ArrayList<>();
            letters.add("A");
            letters.add("B");
            letters.add("C");
            letters.add("D");
            letters.add("P");
            letters.add("F");
            for (String l: letters) {
                MenuItem mi = new MenuItem(l);
                mi.setOnAction(actionEvent -> {
                    chooseGrade(l, letter);
                });
                letter.getItems().add(mi);
            }
            info.getChildren().add(buid);
            info.getChildren().add(fullname);
            info.getChildren().add(rawScore);
            info.getChildren().add(bonus);
            info.getChildren().add(letter);
            finalGrade.getChildren().add(info);
        }
    }

    public void chooseGrade(String l, MenuButton letter) {
        letter.setText(l);
    }

    public void showComment(String s, String name) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Comments for " + name);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();
    }

    public void addCurve() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Archive.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        ArchiveController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        double curve = 0;
        try {
            curve = Double.parseDouble(this.curve.getText());
        }
        catch (Exception ignored) {
        }
        modifyCriteriaController.initial(curve);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void save() throws IOException {
        HashMap<String, Character> finalGrade = new HashMap<>();
        ObservableList<Node> nodes = this.finalGrade.getChildren();
        for (Node node : nodes) {
            HBox info = (HBox) node;
            ObservableList<Node> temp = info.getChildren();
            String name = temp.get(4).getId();
            String grade = ((MenuButton) temp.get(4)).getText();
            char letter = 'P';
            if (grade.length() != 0)
                letter = grade.charAt(0);
            finalGrade.put(name, letter);
        }
        gs.giveFinalGrade(finalGrade);
        exportAsFile();
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

    public void exportAsFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose csv file.");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("csv files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null)
            gs.exportAsFile(file.getPath());
    }

    public void archive() throws IOException {
        Course current = gs.getCurrent();
        gs.archiveCourse(current.getName(), current.getYear(), current.getSemester());
        FXMLLoader courseMenu = new FXMLLoader(getClass().getResource("ActiveCourses.fxml"));
        Parent active_fxml = courseMenu.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        ActiveCoursesController courseMenuController = courseMenu.getController();
        courseMenuController.setGs(gs);
        courseMenuController.initial();
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void goBack() throws IOException {
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(parent);
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
}
