package CONTROLLER;

import LOGIC.Course;
import LOGIC.GradingSystem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ActiveCoursesController {
    Scene courseMenu;

    @FXML
    FlowPane flowpane;

    @FXML
    Button Inactive;

    GradingSystem gs;

    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }

    public void initial(){
        List<Course> actives = gs.getActive();
        for (Course active: actives) {
            Button course = new Button();
            course.setFont(new Font(18));
            course.setPrefHeight(120);
            course.setPrefWidth(160);
            course.setMnemonicParsing(false);
            course.setText(active.getName() + "\n" + active.getYear() + "\n" + active.getSemester());
            course.setStyle("-fx-background-color: white; -fx-border-color: black;");
            course.setOnAction(actionEvent -> {
                try {
                    chooseCourse(active.getName() + "\n" + active.getYear() + "\n" + active.getSemester());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            flowpane.getChildren().add(course);
        }
    }

    public void chooseCourse(String name) throws IOException {
        String[] info = name.split("\n");
        gs.chooseCourse(info[0], info[1], info[2]);
        FXMLLoader courseMenu = new FXMLLoader(getClass().getResource("/UI/CourseMenu.fxml"));
        Parent active_fxml = courseMenu.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        CourseMenuController courseMenuController = courseMenu.getController();
        courseMenuController.setGs(gs);
        courseMenuController.setParent(Inactive.getScene());
        courseMenuController.initializer();
        Stage window = (Stage) Inactive.getScene().getWindow();
        window.setScene(active);
    }

    public void addCourse() throws IOException {
        FXMLLoader courseMenu = new FXMLLoader(getClass().getResource("/UI/AddCourse.fxml"));
        Parent active_fxml = courseMenu.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        AddCourseController courseMenuController = courseMenu.getController();
        courseMenuController.setGs(gs);
        courseMenuController.setParent(Inactive.getScene());
        courseMenuController.initial(new ArrayList<>(), "", new ArrayList<>());
        Stage window = (Stage) Inactive.getScene().getWindow();
        window.setScene(active);
    }

    public void inactive() throws IOException {
        FXMLLoader inactiveCourse = new FXMLLoader(getClass().getResource("/UI/InActiveCourses.fxml"));
        Parent active_fxml = inactiveCourse.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        InActiveCoursesController inactiveCoursesController = inactiveCourse.getController();
        inactiveCoursesController.setGs(gs);
        inactiveCoursesController.setActive(Inactive.getScene());
        List<Course> inactive = gs.getInactive();
        List<String> menu = new ArrayList<>();
        for (Course course: inactive) {
            String info = course.getYear() + " " + course.getSemester();
            if (!menu.contains(info))
                menu.add(info);
        }
        menu.sort((o1, o2) -> {
            char[] chars1 = o1.toCharArray();
            char[] chars2 = o2.toCharArray();
            int i=0;
            while(i<chars1.length && i<chars2.length){
                if(chars1[i]>chars2[i]){
                    return -1;
                }else if(chars1[i]<chars2[i]){
                    return 1;
                }else{
                    i++;
                }
            }
            if(i==chars1.length)
                return 1;
            if(i== chars2.length)
                return -1;
            return 0;
        });
        inactiveCoursesController.initial(menu);
        Stage window = (Stage) Inactive.getScene().getWindow();
        window.setScene(active);
    }
}
