package UI;

import LOGIC.Course;
import LOGIC.GradingSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.*;

public class InActiveCoursesController {
    Scene active;

    public void setActive(Scene active) {
        this.active = active;
    }

    @FXML
    Button Inactive;
    @FXML
    MenuButton menuButton;
    @FXML
    FlowPane flowPane;

    GradingSystem gs;

    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }

    public void initial(List<String> menu) {
        List<Course> inactive = gs.getInactive();
        menuButton.setText(menu.get(0));
        for (int i = 1; i < menu.size(); i++) {
            MenuItem mi = new MenuItem(menu.get(i));
            int finalI = i;
            mi.setOnAction(actionEvent -> {
                try {
                    modify(finalI, menu);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            menuButton.getItems().add(mi);
        }
        String tab = menuButton.getText();
        for (Course cou: inactive) {
            String info = cou.getYear() + " " + cou.getSemester();
            if (tab.equals(info)) {
                Button course = new Button();
                course.setFont(new Font(18));
                course.setPrefHeight(120);
                course.setPrefWidth(160);
                course.setMnemonicParsing(false);
                course.setText(cou.getName() + "\n" + cou.getYear() + "\n" + cou.getSemester());
                course.setStyle("-fx-background-color: white; -fx-border-color: black;");
                course.setOnAction(actionEvent -> {
                    try {
                        chooseCourse(cou.getName() + "\n" + cou.getYear() + "\n" + cou.getSemester());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                flowPane.getChildren().add(course);
            }
        }
    }

    public void modify(int index, List<String> menu) throws IOException {
        FXMLLoader inactiveCourse = new FXMLLoader(getClass().getResource("InActiveCourses.fxml"));
        Parent active_fxml = inactiveCourse.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        InActiveCoursesController inactiveCoursesController = inactiveCourse.getController();
        inactiveCoursesController.setGs(gs);
        inactiveCoursesController.setActive(this.active);
        List<String> temp = new ArrayList<>();
        for (int i = index; i < menu.size(); i++)
            temp.add(menu.get(i));
        for (int i = 0; i < index; i++)
            temp.add(menu.get(i));
        inactiveCoursesController.initial(temp);
        Stage window = (Stage) Inactive.getScene().getWindow();
        window.setScene(active);
    }

    public void chooseCourse(String name) throws IOException {
        String[] info = name.split("\n");
        gs.chooseCourse(info[0], info[1], info[2]);
        FXMLLoader courseMenu = new FXMLLoader(getClass().getResource("CourseMenu.fxml"));
        Parent active_fxml = courseMenu.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        CourseMenuController courseMenuController = courseMenu.getController();
        courseMenuController.setGs(gs);
        courseMenuController.setParent(this.active);
        courseMenuController.initializer();
        Stage window = (Stage) Inactive.getScene().getWindow();
        window.setScene(active);
    }

    public void active() throws IOException {
        Stage window = (Stage) Inactive.getScene().getWindow();
        window.setScene(active);
    }
}
