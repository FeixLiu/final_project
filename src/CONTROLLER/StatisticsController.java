package CONTROLLER;

import LOGIC.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler.*;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
    public void setGS(GradingSystem gs) {
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

    HashMap<Student, Double> overall;

    private ObservableList<TableData> allPersons = FXCollections.observableArrayList();

    public void initial() {
        selectAll.setSelected(true);
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        table.setEditable(false);
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        score.setCellValueFactory(new PropertyValueFactory<>("score"));
        checkbox.setCellValueFactory(new PropertyValueFactory<>("select"));
        checkbox.setSortable(false);
        // Add Name and score information to the table
        overall = gs.grabAllOverall();

        for (Student student: overall.keySet()) {
            TableData entry = new TableData(student, overall.get(student), this);
            allPersons.add(entry);
        }
        table.setItems(allPersons);
        table.setOnMouseClicked(mouseEvent -> {
            int index = table.getSelectionModel().getSelectedIndex();
            String name = table.getItems().get(index).getFullName();
            try {
                clickOnName(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        getStatistics();
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

        selectAll.setOnAction(actionEvent -> {
            toggleAll();
        });
    }

    public void clickOnName(String name) throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/Grading.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        GradingController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial("N", "N", name, "N");
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void update() {
        HashMap<String, Double> students = new HashMap<>();
        for (Student student: overall.keySet())
            students.put(student.getName().getName(), overall.get(student));
        List<String> unselect = new ArrayList<>();
        for (TableData td: allPersons) {
            if (!td.getSelect().isSelected())
                unselect.add(td.getFullName());
        }
        List<Double> statistical = gs.getStudentStatistical(unselect, students);
        avg.setText(Double.toString(statistical.get(0)));
        median.setText(Double.toString(statistical.get(1)));
        std_dev.setText(Double.toString(statistical.get(2)));
    }

    public void goBack() {
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(parent);
    }

    public void getStatistics() {
        HashMap<String, Double> students = new HashMap<>();
        for (Student student: overall.keySet())
            students.put(student.getName().getName(), overall.get(student));

        List<Double> statistical = gs.getStudentStatistical(new ArrayList<>(), students);
        avg.setText(Double.toString(statistical.get(0)));
        median.setText(Double.toString(statistical.get(1)));
        std_dev.setText(Double.toString(statistical.get(2)));
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
        update();
    }

    public void goStudent() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/Students.fxml"));
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
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/CourseMenu.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        CourseMenuController courseMenuController = modify.getController();
        courseMenuController.setGs(gs);
        courseMenuController.setParent(parent);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void goAssignment() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/Assignments.fxml"));
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
        FXMLLoader modify = new FXMLLoader(getClass().getResource("/UI/Grading.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        GradingController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial();
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }
}