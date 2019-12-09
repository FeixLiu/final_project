package CONTROLLER;

import LOGIC.Criteria;
import LOGIC.GradingSystem;
import LOGIC.Template;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddCourseController {
    Scene parent;
    public void setParent(Scene parent) {
        this.parent = parent;
    }
    GradingSystem gs;
    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }

    @FXML
    Button cancel;
    @FXML
    TextField courseName;
    @FXML
    TextField year;
    @FXML
    TextField semester;
    @FXML
    FlowPane criteriaPane;
    @FXML
    MenuButton menuButton;
    @FXML
    Button addOne;

    public void initial(List<Criteria> toShow, String name, List<String> show) {
        if (show.size() != 0) {
            courseName.setText(show.get(0));
            year.setText(show.get(1));
            semester.setText(show.get(2));
        }
        List<Template> templates = gs.getTemplates();
        templates.sort(((o1, o2) -> {
            char[] chars1 = o1.getName().toCharArray();
            char[] chars2 = o2.getName().toCharArray();
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
        menuButton.setText(name);
        for (Template template: templates) {
            String tempName = template.getName();
            if (tempName.equals(name))
                continue;
            MenuItem im = new MenuItem(tempName);
            im.setOnAction(actionEvent -> {
                try {
                    reload(tempName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            menuButton.getItems().add(im);
        }
        toShow.sort((o1, o2) -> Double.compare(o2.getPercentage(), o1.getPercentage()));
        for (Criteria criteria: toShow) {
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
                    deleteCriteria(criteria.getLabel(), toShow);
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

    public void reload(String name) throws IOException {
        List<Template> all = gs.getTemplates();
        Template temp = null;
        for (Template template: all) {
            if (template.getName().equals(name)) {
                temp = template;
                break;
            }
        }
        List<Criteria> toshow = null;
        if (temp != null)
            toshow = temp.getCriteria();
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/AddCourse.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        AddCourseController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGs(gs);
        modifyCriteriaController.setParent(parent);
        List<String> show = new ArrayList<>();
        show.add(courseName.getText());
        show.add(year.getText());
        show.add(semester.getText());
        modifyCriteriaController.initial(toshow, name, show);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
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

    public void deleteCriteria(String dont, List<Criteria> all) throws IOException {
        Criteria temp = null;
        for (Criteria cri: all) {
            if (cri.getLabel().equals(dont))
                temp = cri;
        }
        all.remove(temp);
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/AddCourse.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        AddCourseController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGs(gs);
        modifyCriteriaController.setParent(parent);
        List<String> show = new ArrayList<>();
        show.add(courseName.getText());
        show.add(year.getText());
        show.add(semester.getText());
        modifyCriteriaController.initial(all, menuButton.getText(), show);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void save() throws IOException {
        String name = courseName.getText();
        String year = this.year.getText();
        String semester = this.semester.getText();
        if (name.length() == 0 || year.length() == 0 || semester.length() == 0) {
            goBack();
            return;
        }
        ObservableList<Node> nodes = criteriaPane.getChildren();
        HashMap<String, Double> criteria = new HashMap<>();
        List<Criteria> temp = new ArrayList<>();
        String tempName = menuButton.getText();
        List<Template> templates = gs.getTemplates();
        List<Criteria> choose = new ArrayList<>();
        for (Template template: templates)
            if (template.getName().equals(tempName))
                choose = template.getCriteria();
        for (int i = 0; i < nodes.size(); i = i + 3) {
            try {
                if (i / 3 < choose.size())
                    criteria.put(((Label) nodes.get(i)).getText(), Double.parseDouble(((TextField) nodes.get(i + 1)).getText()));
                else
                    criteria.put(((TextField) nodes.get(i)).getText(), Double.parseDouble(((TextField) nodes.get(i + 1)).getText()));
            }
            catch (Exception ignored) {
            }
        }
        for (String cri: criteria.keySet()) {
            temp.add(new Criteria(cri, criteria.get(cri)));
        }
        gs.addCourse(name, temp, semester, year);
        FXMLLoader activeCourse = new FXMLLoader(getClass().getResource("/UI/ActiveCourses.fxml"));
        Parent active_fxml = activeCourse.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        ActiveCoursesController activeCoursesController = activeCourse.getController();
        activeCoursesController.setGs(gs);
        activeCoursesController.initial();
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void goBack() {
        Stage window = (Stage) cancel.getScene().getWindow();
        window.setScene(parent);
    }
}
