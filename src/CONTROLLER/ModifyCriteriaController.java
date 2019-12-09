package CONTROLLER;

import LOGIC.Config;
import LOGIC.Criteria;
import LOGIC.GradingSystem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ModifyCriteriaController {
    Scene parent;
    public void setParent(Scene parent) {
        this.parent = parent;
    }
    @FXML
    Button courseName;
    @FXML
    FlowPane criteriaPane;
    @FXML
    Label courseStatus;
    @FXML
    Label students;
    @FXML
    Label under;
    @FXML
    Label graduate;
    @FXML
    Button archive;

    GradingSystem gs;

    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }

    public void initializer() {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        courseStatus.setText(gs.getCurrent().getStatus());
        List<Integer> info = gs.getCourseStudentsNumber();
        students.setText(String.valueOf(info.get(0)));
        under.setText(String.valueOf(info.get(1)));
        graduate.setText(String.valueOf(info.get(2)));
        String status = gs.getCurrent().getStatus();
        if (status.equals(Config.INACTIVE_COURSE))
            archive.setText("Modify Overall");
        List<Criteria> allCriteria = gs.getCourseCriteria();
        allCriteria.sort((o1, o2) -> Double.compare(o2.getPercentage(), o1.getPercentage()));
        for (Criteria criteria: allCriteria) {
            Label cri = new Label();
            cri.setText(criteria.getLabel());
            cri.setPrefHeight(40);
            cri.setPrefWidth(200);
            cri.setFont(new Font(25));
            TextField percentage = new TextField();
            percentage.setText(String.valueOf(criteria.getPercentage()));
            percentage.setPrefHeight(40);
            percentage.setPrefWidth(100);
            percentage.setFont(new Font(25));
            Button delete = new Button();
            delete.setStyle("-fx-background-color: transparent; -fx-border-width: 0 0 0 0;");
            delete.setOnAction(actionEvent -> {
                try {
                    deleteCriteria(criteria.getLabel());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Image im = new Image("UI/Icon/baseline_delete_forever_black_18dp.png");
            delete.setPrefHeight(40);
            delete.setPrefWidth(50);
            delete.setGraphic(new ImageView(im));
            criteriaPane.getChildren().add(cri);
            criteriaPane.getChildren().add(percentage);
            criteriaPane.getChildren().add(delete);
        }
    }

    public void addOne() {
        TextField cri = new TextField();
        cri.setPrefHeight(40);
        cri.setPrefWidth(200);
        cri.setFont(new Font(25));
        TextField percentage = new TextField();
        percentage.setPrefHeight(40);
        percentage.setPrefWidth(100);
        percentage.setFont(new Font(25));
        Button delete = new Button();
        delete.setStyle("-fx-background-color: transparent; -fx-border-width: 0 0 0 0;");
        delete.setPrefHeight(40);
        delete.setPrefWidth(50);
        criteriaPane.getChildren().add(cri);
        criteriaPane.getChildren().add(percentage);
        criteriaPane.getChildren().add(delete);
    }

    public void deleteCriteria(String name) throws IOException {
        gs.deleteCriteria(name);
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/ModifyCriteira.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        ModifyCriteriaController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGs(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initializer();
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void saveTemplate() throws IOException {
        String name = courseName.getText();
        name = name.replace('\n', ',');
        gs.saveAsTemplate(name);
        goBack();
    }

    public void goBack() throws IOException {
        ObservableList<Node> nodes = criteriaPane.getChildren();
        HashMap<String, Double> criteria = new HashMap<>();
        List<Criteria> temp =  gs.getCourseCriteria();
        for (int i = 0; i < nodes.size(); i = i + 3) {
            try {
                if (i / 3 < temp.size())
                    criteria.put(((Label) nodes.get(i)).getText(), Double.parseDouble(((TextField) nodes.get(i + 1)).getText()));
                else
                    criteria.put(((TextField) nodes.get(i)).getText(), Double.parseDouble(((TextField) nodes.get(i + 1)).getText()));
            }
            catch (Exception ignored) {
            }
        }
        for (String name: criteria.keySet()) {
            boolean flag = false;
            for (Criteria cri: temp) {
                if (cri.getLabel().equals(name))
                    flag = true;
                if (cri.getLabel().equals(name) && cri.getPercentage() != criteria.get(name))
                    gs.modifyCriteria(name, criteria.get(name));
            }
            if (!flag)
                gs.modifyCriteria(name, criteria.get(name));
        }
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
}
