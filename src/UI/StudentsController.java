package UI;

import LOGIC.Config;
import LOGIC.GradingSystem;
import LOGIC.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class StudentsController {
    Scene parent;
    public void setParent(Scene parent) {
        this.parent = parent;
    }
    GradingSystem gs;
    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }


    @FXML
    Button courseName;
    @FXML
    VBox mainPane;
    @FXML
    MenuButton menuButton;
    @FXML
    MenuItem text1;
    @FXML
    MenuItem text2;
    @FXML
    AnchorPane modifyPane;
    @FXML
    Label buid;
    @FXML
    TextField name;
    @FXML
    TextField email;
    @FXML
    MenuButton year;
    @FXML
    MenuButton enroll;
    @FXML
    Button save;
    @FXML
    Button delete;
    @FXML
    Button cancel;


    public void initializer(String[] order) {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        menuButton.setText(order[0]);
        text1.setText(order[1]);
        text2.setText(order[2]);
        List<Student> students = gs.getAllStudent();
        students.sort((o1, o2) -> {
            String s1;
            String s2;
            if (order[0].equals("BUID")) {
                s1 = o1.getId().getId();
                s2 = o2.getId().getId();
            } else if (order[0].equals("Email")) {
                s1 = o1.getEmail().getEmail();
                s2 = o2.getEmail().getEmail();
            } else {
                s1 = o1.getName().getName();
                s2 = o2.getName().getName();
            }
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
            Button buid = new Button(s.getId().getId());
            buid.setFont(new Font(14));
            buid.setPrefHeight(25);
            buid.setPrefWidth(120);
            buid.setMnemonicParsing(false);
            buid.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button fullname = new Button(s.getName().getName());
            fullname.setFont(new Font(14));
            fullname.setPrefHeight(25);
            fullname.setPrefWidth(130);
            fullname.setMnemonicParsing(false);
            fullname.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button email = new Button(s.getEmail().getEmail());
            email.setFont(new Font(14));
            email.setPrefHeight(25);
            email.setPrefWidth(188);
            email.setMnemonicParsing(false);
            email.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button year = new Button(s.getKind());
            year.setFont(new Font(14));
            year.setPrefHeight(25);
            year.setPrefWidth(175);
            year.setMnemonicParsing(false);
            year.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button status = new Button(s.getStatus());
            status.setFont(new Font(14));
            status.setPrefHeight(25);
            status.setPrefWidth(117);
            status.setMnemonicParsing(false);
            status.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            Button edit = new Button();
            edit.setStyle("-fx-background-color: transparent; -fx-border-width: 1 1 1 1;");
            Image im = new Image("UI/Icon/baseline_create_black_17dp.png");
            edit.setPrefHeight(25);
            edit.setPrefWidth(40);
            edit.setGraphic(new ImageView(im));
            edit.setOnAction(actionEvent -> {
                this.edit(s.getId().getId());
            });
            info.getChildren().add(buid);
            info.getChildren().add(fullname);
            info.getChildren().add(email);
            info.getChildren().add(year);
            info.getChildren().add(status);
            info.getChildren().add(edit);
            mainPane.getChildren().add(info);
        }
        year.getItems().clear();
        MenuItem graduate = new MenuItem(Config.GRADUATE);
        graduate.setOnAction(actionEvent -> {
            yearChoose(Config.GRADUATE);
        });
        MenuItem under = new MenuItem(Config.UNDERGRADUATE);
        under.setOnAction(actionEvent -> {
            yearChoose(Config.UNDERGRADUATE);
        });
        year.getItems().add(graduate);
        year.getItems().add(under);
        enroll.getItems().clear();
        MenuItem normal = new MenuItem(Config.NORMAL_STUDENT);
        normal.setOnAction(actionEvent -> {
            enrollChoose(Config.NORMAL_STUDENT);
        });
        MenuItem with = new MenuItem(Config.WITHDRAW_STUDENT);
        with.setOnAction(actionEvent -> {
            enrollChoose(Config.WITHDRAW_STUDENT);
        });
        MenuItem drop = new MenuItem(Config.DROP_STUDENT);
        drop.setOnAction(actionEvent -> {
            enrollChoose(Config.DROP_STUDENT);
        });
        enroll.getItems().add(normal);
        enroll.getItems().add(with);
        enroll.getItems().add(drop);
    }

    public void yearChoose(String year) {
        this.year.setText(year);
        this.year.getItems().clear();
        if (year.equals(Config.GRADUATE)) {
            MenuItem under = new MenuItem(Config.UNDERGRADUATE);
            under.setOnAction(actionEvent -> {
                yearChoose(Config.UNDERGRADUATE);
            });
            this.year.getItems().add(under);
        }
        else {
            MenuItem graduate = new MenuItem(Config.GRADUATE);
            graduate.setOnAction(actionEvent -> {
                yearChoose(Config.GRADUATE);
            });
            this.year.getItems().add(graduate);
        }
    }

    public void enrollChoose(String enroll) {
        this.enroll.setText(enroll);
        this.enroll.getItems().clear();
        if (enroll.equals(Config.NORMAL_STUDENT)) {
            MenuItem with = new MenuItem(Config.WITHDRAW_STUDENT);
            with.setOnAction(actionEvent -> {
                enrollChoose(Config.WITHDRAW_STUDENT);
            });
            MenuItem drop = new MenuItem(Config.DROP_STUDENT);
            drop.setOnAction(actionEvent -> {
                enrollChoose(Config.DROP_STUDENT);
            });
            this.enroll.getItems().add(with);
            this.enroll.getItems().add(drop);
        }
        else if (enroll.equals(Config.WITHDRAW_STUDENT)){
            MenuItem normal = new MenuItem(Config.NORMAL_STUDENT);
            normal.setOnAction(actionEvent -> {
                enrollChoose(Config.NORMAL_STUDENT);
            });
            MenuItem drop = new MenuItem(Config.DROP_STUDENT);
            drop.setOnAction(actionEvent -> {
                enrollChoose(Config.DROP_STUDENT);
            });
            this.enroll.getItems().add(normal);
            this.enroll.getItems().add(drop);
        }
        else {
            MenuItem normal = new MenuItem(Config.NORMAL_STUDENT);
            normal.setOnAction(actionEvent -> {
                enrollChoose(Config.NORMAL_STUDENT);
            });
            MenuItem with = new MenuItem(Config.WITHDRAW_STUDENT);
            with.setOnAction(actionEvent -> {
                enrollChoose(Config.WITHDRAW_STUDENT);
            });
            this.enroll.getItems().add(normal);
            this.enroll.getItems().add(with);
        }
    }

    public void edit(String id) {
        buid.setText(id);
        List<Student> students = gs.getAllStudent();
        Student temp = null;
        for (Student student: students) {
            if (student.getId().getId().equals(id))
                temp = student;
        }
        if (temp == null)
            return;
        name.setText(temp.getName().getName());
        email.setText(temp.getEmail().getEmail());
        modifyPane.setVisible(true);
        delete.setVisible(true);
        cancel.setVisible(true);
        save.setVisible(true);
    }

    public void deleteStudent() throws IOException {
        String id = buid.getText();
        gs.deleteStudent(id);
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Students.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        StudentsController studentsController = modify.getController();
        studentsController.setGs(gs);
        studentsController.setParent(parent);
        String[] order = new String[3];
        order[0] = menuButton.getText();
        order[1] = text1.getText();
        order[2] = text2.getText();
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

    public void save() throws IOException {
        String id = buid.getText();
        String name = this.name.getText();
        String status = enroll.getText();
        String email = this.email.getText();
        String year = this.year.getText();
        if (id.length() == 0 || name.length() == 0 || status.length() == 0 || email.length() == 0 || year.length() == 0) {
            this.cancel();
            return;
        }
        if (!status.equals(Config.NORMAL_STUDENT) && !status.equals(Config.WITHDRAW_STUDENT) && !status.equals(Config.DROP_STUDENT)) {
            this.cancel();
            return;
        }
        if (!year.equals(Config.GRADUATE) && !year.equals(Config.UNDERGRADUATE)) {
            this.cancel();
            return;
        }
        List<Student> students = gs.getAllStudent();
        Student temp = null;
        for (Student student: students) {
            if (student.getId().getId().equals(id))
                temp = student;
        }
        if (temp == null) {
            this.cancel();
            return;
        }
        if (!temp.getName().getName().equals(name))
            gs.modifyStudentName(id, name);
        if (!temp.getEmail().getEmail().equals(email))
            gs.modifyStudentEmail(id, email);
        if (year.equals(Config.GRADUATE) && !temp.getKind().equals(Config.GRADUATE))
                gs.modifyStudentYear(id, Config.GRADUATE);
        if (year.equals(Config.UNDERGRADUATE) && !temp.getKind().equals(Config.UNDERGRADUATE))
                gs.modifyStudentYear(id, Config.UNDERGRADUATE);
        if (status.equals(Config.DROP_STUDENT) && !temp.getKind().equals(Config.DROP_STUDENT))
                gs.modifyStudentStatus(id, Config.DROP_STUDENT);
        if (status.equals(Config.WITHDRAW_STUDENT) && !temp.getKind().equals(Config.WITHDRAW_STUDENT))
                gs.modifyStudentStatus(id, Config.WITHDRAW_STUDENT);
        if (status.equals(Config.NORMAL_STUDENT) && !temp.getKind().equals(Config.NORMAL_STUDENT))
                gs.modifyStudentStatus(id, Config.NORMAL_STUDENT);
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Students.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        StudentsController studentsController = modify.getController();
        studentsController.setGs(gs);
        studentsController.setParent(parent);
        String[] order = new String[3];
        order[0] = menuButton.getText();
        order[1] = text1.getText();
        order[2] = text2.getText();
        studentsController.initializer(order);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void cancel() {
        buid.setText("");
        name.setText("");
        email.setText("");
        year.setText("Choose year");
        enroll.setText("Choose enrollment");
        modifyPane.setVisible(false);
        delete.setVisible(false);
        cancel.setVisible(false);
        save.setVisible(false);
    }

    public void addOne() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("addOneStudent.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        addOneStudentsController addOneStudentsController = modify.getController();
        addOneStudentsController.setGs(gs);
        addOneStudentsController.setParent(menuButton.getScene());
        addOneStudentsController.setAllCourse(parent);
        String[] order = new String[3];
        order[0] = menuButton.getText();
        order[1] = text1.getText();
        order[2] = text2.getText();
        addOneStudentsController.initializer(order);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void importFromFile() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose csv file.");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("csv files (*.csv)", "*.csv");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Stage stage = new Stage();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            gs.addStudentsFromFile(file.getPath());
            FXMLLoader modify = new FXMLLoader(getClass().getResource("Students.fxml"));
            Parent active_fxml = modify.load();
            Scene active = new Scene(active_fxml, 1024, 768);
            StudentsController studentsController = modify.getController();
            studentsController.setGs(gs);
            studentsController.setParent(parent);
            String[] order = new String[3];
            order[0] = menuButton.getText();
            order[1] = text1.getText();
            order[2] = text2.getText();
            studentsController.initializer(order);
            Stage window = (Stage) courseName.getScene().getWindow();
            window.setScene(active);
        }
    }

    public void clickOne() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Students.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        StudentsController studentsController = modify.getController();
        studentsController.setGs(gs);
        studentsController.setParent(parent);
        String[] order = new String[3];
        order[0] = text1.getText();
        order[1] = text2.getText();
        order[2] = menuButton.getText();
        studentsController.initializer(order);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void clickTwo() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Students.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        StudentsController studentsController = modify.getController();
        studentsController.setGs(gs);
        studentsController.setParent(parent);
        String[] order = new String[3];
        order[0] = text2.getText();
        order[1] = menuButton.getText();
        order[2] = text1.getText();
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

    public void goBack() {
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(parent);
    }
}
