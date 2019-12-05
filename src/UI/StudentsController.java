package UI;

import LOGIC.GradingSystem;
import LOGIC.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

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
    FlowPane mainPane;

    public void initializer() {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        List<Student> students = gs.getAllStudent();
        students.sort(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                String s1 = o1.getId().getId();
                String s2 = o2.getId().getId();
                char[] chars1 = s1.toCharArray();
                char[] chars2 = s2.toCharArray();
                int i=0;
                while(i<chars1.length && i<chars2.length){
                    if(chars1[i]>chars2[i]){
                        return 1;
                    }else if(chars1[i]<chars2[i]){
                        return -1;
                    }else{
                        i++;
                    }
                }
                if(i==chars1.length)
                    return -1;
                if(i== chars2.length)
                    return 1;
                return 0;
            }
        });
        for (Student s: students) {
            Button buid = new Button(s.getId().getId());
            buid.setFont(new Font(18));
            buid.setPrefHeight(25);
            buid.setPrefWidth(120);
            buid.setMnemonicParsing(false);
            buid.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            mainPane.getChildren().add(buid);
        }
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
