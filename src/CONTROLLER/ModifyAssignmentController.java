package CONTROLLER;

import LOGIC.Assignment;
import LOGIC.GradingSystem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModifyAssignmentController {
    Scene parent;
    public void setParent(Scene parent) {
        this.parent = parent;
    }
    GradingSystem gs;
    public void setGS(GradingSystem gs) {
        this.gs = gs;
    }

    @FXML
    Label name;
    @FXML
    Label criteria;
    @FXML
    Button save;
    @FXML
    Button kind;
    @FXML
    FlowPane parts;
    @FXML
    TextField percentage;
    @FXML
    Label point;
    Assignment assignment;

    public void initial(Assignment assignment) {
        this.assignment = assignment;
        if (assignment.getChildren().size() == 0) {
            kind.setText("Single");
            parts.setVisible(false);
        }
        else {
            kind.setText("Multi Parts");
            parts.setVisible(true);
        }
        name.setText(assignment.getName());
        criteria.setText(assignment.getCriteria().getLabel());
        point.setText(String.valueOf(assignment.getTotal()));
        percentage.setText(String.valueOf(assignment.getPercentage()));
        List<Assignment> assignments = assignment.getChildren();
        for (Assignment value : assignments) {
            Label cri = new Label();
            cri.setText(value.getName());
            cri.setPrefHeight(40);
            cri.setPrefWidth(250);
            cri.setFont(new Font(18));
            cri.setAlignment(Pos.CENTER);
            TextField percentage = new TextField(String.valueOf(value.getPercentage()));
            percentage.setPrefHeight(40);
            percentage.setPrefWidth(250);
            percentage.setFont(new Font(18));
            percentage.setAlignment(Pos.CENTER_LEFT);
            parts.getChildren().add(cri);
            parts.getChildren().add(percentage);
        }
    }

    public void save() throws IOException {
        double percent;
        try {
            percent = Double.parseDouble(this.percentage.getText());
        }
        catch (Exception e) {
            goBack();
            return;
        }
        if (percent != assignment.getPercentage())
            gs.modifyAssignmentPercentage(assignment.getName(), percent, assignment.getCriteria().getLabel());
        if (assignment.getChildren().size() != 0) {
            ObservableList<Node> nodes = parts.getChildren();
            List<Double> share = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i = i + 2) {
                try {
                    share.add(Double.parseDouble(((TextField)nodes.get(i + 1)).getText()));
                }
                catch (Exception e) {
                    goBack();
                    return;
                }
            }
            gs.modifySubAssignmentPercentage(assignment.getName(), share, assignment.getCriteria().getLabel());
        }
        goBack();
    }

    public void goBack() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/Assignments.fxml"));
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
