package UI;

import LOGIC.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class StatisticsController {

    Scene parent;
    GradingSystem gs;

    public void setParent(Scene parent) {
        this.parent = parent;
    }
    public void setGs(GradingSystem gs) {
        this.gs = gs;
    }

    @FXML
    Button courseName;

    @FXML
    private TableView<TableData> table;

    @FXML
    public TableColumn<TableData, String> firstName;

    @FXML
    public TableColumn<TableData, String> lastName;

    @FXML
    public TableColumn<TableData, Double> score;

    @FXML
    public TableColumn<TableData, CheckBox> checkbox;

    @FXML
    public Label std_dev;
    @FXML
    public Label avg;
    @FXML
    public Label median;

    @FXML
    public MenuButton statsMenu;
    @FXML
    public MenuItem ugrad;
    @FXML
    public MenuItem grad;
    @FXML
    public MenuItem all;

    @FXML
    public CheckBox selectAll;

    private ObservableList<TableData> allPersons = FXCollections.observableArrayList();

    public void initializer() {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        table.setEditable(false);
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        checkbox.setCellValueFactory(new PropertyValueFactory<>("select"));
        checkbox.setSortable(false);
        // Add Name and score information to the table
        List<Student> students = gs.getAllStudent();

        for (Student student: students) {
            TableData entry = new TableData(student, 90.0);
            allPersons.add(entry);
        }
        table.setItems(allPersons);
        EventHandler<ActionEvent> filter = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                String selected = ((MenuItem)e.getSource()).getText();
                statsMenu.setText(selected);

                // Deselect all students when changing the filter
                for (TableData person: allPersons) {
                    person.getSelect().setSelected(false);
                }
                selectAll.setSelected(false);
                // Alter the displayed students
                ObservableList<TableData> filteredPersons = FXCollections.observableArrayList();
                switch (selected) {
                    case "Undergraduate":
                        for (TableData person: allPersons) {
                            if(person.getKind().equals(Config.UNDERGRADUATE)) {
                                filteredPersons.add(person);
                            }
                        }
                        table.setItems(filteredPersons);
                        break;
                    case "Graduate":
                        for (TableData person: allPersons) {
                            if(person.getKind().equals(Config.GRADUATE)) {
                                filteredPersons.add(person);
                            }
                        }
                        table.setItems(filteredPersons);
                        break;
                    case "All":
                        table.setItems(allPersons);
                        break;
                }
            }
        };

        ugrad.setOnAction(filter);
        grad.setOnAction(filter);
        all.setOnAction(filter);

        EventHandler<ActionEvent> toggleAll = new EventHandler<ActionEvent>() {

            public void handle(ActionEvent e)
            {
                toggleAll();
            }

        };
        selectAll.setOnAction(toggleAll);
    }

    public void goBack() {
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(parent);
    }

    public void getStatistics() {
        HashMap<String, Double> students = new HashMap<>();

        ObservableList<TableData> displayedPersons = table.getItems();
        for (TableData person: displayedPersons) {
            if (person.getSelect().isSelected()) {
                students.put(person.getFullName(), person.getScore());
            }
        }
        if(students.size() != 0) {
            Double avgVal = Statistical.average(students);
            Double medVal = Statistical.median(students);
            Double stdDevVal = Statistical.standardDev(students);

            avg.setText(Double.toString(avgVal));
            median.setText(Double.toString(medVal));
            std_dev.setText(Double.toString(stdDevVal));
        } else {
            avg.setText(Double.toString(0.0));
            median.setText(Double.toString(0.0));
            std_dev.setText(Double.toString(0.0));
        }
    }

    public void toggleAll() {
        ObservableList<TableData> displayedPersons = table.getItems();
        if (selectAll.isSelected()) {
            for (TableData person: displayedPersons) {
                person.getSelect().setSelected(true);
            }
        } else {
            for (TableData person: displayedPersons) {
                person.getSelect().setSelected(false);
            }
        }
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

    public void goCourseMenu() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("CourseMenu.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        CourseMenuController courseMenuController = modify.getController();
        courseMenuController.setGs(gs);
        courseMenuController.setParent(parent);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }
}
