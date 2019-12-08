package UI;

import LOGIC.GradingSystem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddAssignmentController {
    Scene parent;
    public void setParent(Scene parent) {
        this.parent = parent;
    }
    GradingSystem gs;
    public void setGS(GradingSystem gs) {
        this.gs = gs;
    }

    @FXML
    TextField name;
    @FXML
    MenuButton criteria;
    @FXML
    Button add;
    @FXML
    Button save;
    @FXML
    FlowPane parts;
    @FXML
    Button single;
    @FXML
    Button multi;
    @FXML
    TextField percentage;
    @FXML
    TextField point;
    int count = 0;
    boolean flag;
    List<String> cri;

    public void initial(boolean single, List<String> info, List<String> cri) {
        name.setText(info.get(0));
        criteria.setText(info.get(1));
        percentage.setText(info.get(2));
        point.setText(info.get(3));
        flag = single;
        this.cri = cri;
        for (String a: cri) {
            if (!a.equals(info.get(1))) {
                MenuItem mi = new MenuItem(a);
                mi.setOnAction(actionEvent -> chooseCri(a));
                criteria.getItems().add(mi);
            }
        }
        if (single) {
            parts.setVisible(false);
            add.setVisible(false);
            this.single.setStyle("-fx-background-color: white; -fx-border-width: 1 0 1 1; -fx-border-color: black; -fx-font-size: 20px; -fx-border-radius: 15 0 0 15; -fx-background-radius: 15 0 0 15; -fx-background-color: #ccc;");
            this.multi.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px; -fx-font-size: 20px; -fx-border-radius: 0 15 15 0; -fx-background-radius: 0 15 15 0;");
        }
        else {
            parts.setVisible(true);
            add.setVisible(true);
            this.multi.setStyle("-fx-background-color: white; -fx-border-width: 1 0 1 1; -fx-border-color: black; -fx-font-size: 20px; -fx-border-radius: 0 15 15 0; -fx-background-radius: 0 15 15 0; -fx-background-color: #ccc;");
            this.single.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1px; -fx-font-size: 20px; -fx-border-radius: 15 0 0 15; -fx-background-radius: 15 0 0 15;");
        }
    }

    public void chooseCri(String a) {
        criteria.setText(a);
        criteria.getItems().clear();
        for (String b: cri) {
            if (!a.equals(b)) {
                MenuItem mi = new MenuItem(b);
                mi.setOnAction(actionEvent -> chooseCri(b));
                criteria.getItems().add(mi);
            }
        }
    }

    public void add() {
        count++;
        int i = count;
        Label cri = new Label();
        cri.setText("Part" + i + " percentage");
        cri.setPrefHeight(40);
        cri.setPrefWidth(200);
        cri.setFont(new Font(18));
        cri.setAlignment(Pos.CENTER);
        TextField percentage = new TextField();
        percentage.setText("100");
        percentage.setPrefHeight(40);
        percentage.setPrefWidth(250);
        percentage.setFont(new Font(18));
        percentage.setAlignment(Pos.CENTER_LEFT);
        Button delete = new Button();
        delete.setStyle("-fx-background-color: transparent; -fx-border-width: 0 0 0 0;");
        delete.setOnAction(actionEvent -> {
            delete("Part" + i + " percentage");
        });
        Image im = new Image("UI/Icon/baseline_delete_forever_black_18dp.png");
        delete.setPrefHeight(40);
        delete.setPrefWidth(50);
        delete.setGraphic(new ImageView(im));
        parts.getChildren().add(cri);
        parts.getChildren().add(percentage);
        parts.getChildren().add(delete);
    }

    public void transfer() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("AddAssignment.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        AddAssignmentController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        String name = this.name.getText();
        String cris = this.criteria.getText();
        String percent = this.percentage.getText();
        String point = this.point.getText();
        List<String> temp = new ArrayList<>();
        temp.add(name);
        temp.add(cris);
        temp.add(percent);
        temp.add(point);
        modifyCriteriaController.initial(!flag, temp, cri);
        Stage window = (Stage) criteria.getScene().getWindow();
        window.setScene(active);
    }

    public void delete(String name) {
        ObservableList<Node> nodes = parts.getChildren();
        List<Double> share = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i = i + 3) {
            if (((Label)nodes.get(i)).getText().equals(name))
                continue;
            TextField tf = (TextField) nodes.get(i + 1);
            double part = 0;
            try {
                part = Double.parseDouble(tf.getText());
            }
            catch (Exception e) {}
            share.add(part);
        }
        this.count--;
        parts.getChildren().clear();
        for (int i = 0; i < count; i++) {
            Label cri = new Label();
            cri.setText("Part" + (i + 1) + " percentage");
            cri.setPrefHeight(40);
            cri.setPrefWidth(200);
            cri.setFont(new Font(18));
            cri.setAlignment(Pos.CENTER);
            TextField percentage = new TextField(String.valueOf(share.get(i)));
            percentage.setPrefHeight(40);
            percentage.setPrefWidth(250);
            percentage.setFont(new Font(18));
            percentage.setAlignment(Pos.CENTER_LEFT);
            Button delete = new Button();
            delete.setStyle("-fx-background-color: transparent; -fx-border-width: 0 0 0 0;");
            int finalI = i;
            delete.setOnAction(actionEvent -> {
                delete("Part" + (finalI + 1) + " percentage");
            });
            Image im = new Image("UI/Icon/baseline_delete_forever_black_18dp.png");
            delete.setPrefHeight(40);
            delete.setPrefWidth(50);
            delete.setGraphic(new ImageView(im));
            parts.getChildren().add(cri);
            parts.getChildren().add(percentage);
            parts.getChildren().add(delete);
        }
    }

    public void save() throws IOException {
        String name = this.name.getText();
        String cri = this.criteria.getText();
        String percent = this.percentage.getText();
        String point = this.point.getText();
        if (name.length() == 0 || cri.length() == 0 || percent.length() == 0 || point.length() == 0) {
            goBack();
            return;
        }
        if (flag) {
            try {
                gs.addSingleAssignment(name, cri, "N", "N", "N", Double.parseDouble(point), Double.parseDouble(percent));
            }
            catch (Exception ignored) {}
        }
        else {
            ObservableList<Node> nodes = parts.getChildren();
            if (nodes.size() == 0) {
                try {
                    gs.addSingleAssignment(name, cri, "N", "N", "N", Double.parseDouble(point), Double.parseDouble(percent));
                }
                catch (Exception ignored) {}
                goBack();
                return;
            }
            List<Double> share = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i = i + 3) {
                TextField tf = (TextField) nodes.get(i + 1);
                double part = 0;
                try {
                    part = Double.parseDouble(tf.getText());
                }
                catch (Exception e) {
                    goBack();
                    return;
                }
                share.add(part);
            }
            try {
                gs.addMultiAssignment(name, cri, share, "N", "N", "N", Double.parseDouble(point), Double.parseDouble(percent));
            }
            catch (Exception ignored) {}
        }
        goBack();
    }

    public void goBack() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Assignments.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        AssignmentsController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial();
        Stage window = (Stage) criteria.getScene().getWindow();
        window.setScene(active);
    }
}
