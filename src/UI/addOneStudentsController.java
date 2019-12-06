package UI;

import LOGIC.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class addOneStudentsController {
    Scene parent;
    public void setParent(Scene parent) {
        this.parent = parent;
    }
    Scene allCourse;
    public void setAllCourse(Scene allCourse) {
        this.allCourse = allCourse;
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
    TextField buid;
    @FXML
    TextField name;
    @FXML
    TextField email;
    @FXML
    MenuButton year;

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
            info.getChildren().add(buid);
            info.getChildren().add(fullname);
            info.getChildren().add(email);
            info.getChildren().add(year);
            info.getChildren().add(status);
            info.getChildren().add(edit);
            mainPane.getChildren().add(info);
        }
        year.getItems().clear();
        year.setText("Choose Year!");
        MenuItem graduate = new MenuItem(Config.GRADUATE);
        graduate.setOnAction(actionEvent -> {
            choose(Config.GRADUATE);
        });
        MenuItem under = new MenuItem(Config.UNDERGRADUATE);
        under.setOnAction(actionEvent -> {
            choose(Config.UNDERGRADUATE);
        });
        year.getItems().add(graduate);
        year.getItems().add(under);
    }

    public void choose(String year) {
        this.year.setText(year);
        this.year.getItems().clear();
        if (year.equals(Config.GRADUATE)) {
            MenuItem under = new MenuItem(Config.UNDERGRADUATE);
            under.setOnAction(actionEvent -> {
                choose(Config.UNDERGRADUATE);
            });
            this.year.getItems().add(under);
        }
        else {
            MenuItem graduate = new MenuItem(Config.GRADUATE);
            graduate.setOnAction(actionEvent -> {
                choose(Config.GRADUATE);
            });
            this.year.getItems().add(graduate);
        }
    }

    public void addNewOne() throws IOException {
        String[] order = new String[3];
        order[0] = menuButton.getText();
        order[1] = text1.getText();
        order[2] = text2.getText();
        String id = buid.getText();
        String sEmail = email.getText();
        String fullName = name.getText();
        String gu = year.getText();
        if (id.length() == 0 || fullName.length() == 0 || sEmail.length() == 0 || gu.length() == 0) {
            goBack();
            return;
        }
        Email semail = new Email(sEmail);
        Id sid = new Id(id);
        String[] names = fullName.split(" ");
        Name sName;
        if (names.length == 1)
            sName = new Name("", names[0]);
        else {
            StringBuilder temp = new StringBuilder();
            for (int i = 0; i < names.length - 1; i++)
                temp.append(names[i]);
            sName = new Name(temp.toString(), names[names.length - 1]);
        }
        String kind;
        if (gu.equals(Config.GRADUATE))
            kind = Config.GRADUATE;
        else if(gu.equals(Config.UNDERGRADUATE))
            kind = Config.UNDERGRADUATE;
        else {
            goBack();
            return;
        }
        gs.addOneStudent(sName, sid, semail, kind);
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Students.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        StudentsController studentsController = modify.getController();
        studentsController.setGs(gs);
        studentsController.setParent(allCourse);
        studentsController.initializer(order);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void goBack() {
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(parent);
    }
}
