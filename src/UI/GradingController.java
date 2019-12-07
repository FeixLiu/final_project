package UI;

import LOGIC.Assignment;
import LOGIC.Config;
import LOGIC.Criteria;
import LOGIC.GradingSystem;
import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class GradingController {
    Scene parent;
    public void setParent(Scene parent) {
        this.parent = parent;
    }
    GradingSystem gs;
    public void setGS(GradingSystem gs) {
        this.gs = gs;
    }
    @FXML
    Button courseName;
    @FXML
    VBox score;
    @FXML
    MenuButton criteria;
    @FXML
    Label assLabel;
    @FXML
    MenuButton assignment;
    @FXML
    MenuButton filter;
    @FXML
    Label filterLabel;
    @FXML
    Button save;
    public void initial() {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        List<Criteria> criteria = gs.getCourseCriteria();
        this.criteria.setText("Choose criteria");
        this.assignment.setText("N");
        this.assignment.setVisible(false);
        this.assLabel.setVisible(false);
        this.filter.setText("None");
        MenuItem mii = new MenuItem("Name");
        mii.setOnAction(actionEvent -> {
            try {
                chooseFilter("Name");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.filter.getItems().add(mii);
        HBox criteriaInfo = new HBox();
        criteriaInfo.setPrefHeight(25);
        Text NAME = new Text("Filter");
        NAME.setFont(new Font(15));
        NAME.setWrappingWidth(140);
        NAME.setTextAlignment(TextAlignment.CENTER);
        criteriaInfo.getChildren().add(NAME);
        for (Criteria cri: criteria) {
            Text temp = new Text(cri.getLabel());
            temp.setFont(new Font(15));
            temp.setWrappingWidth(140);
            temp.setTextAlignment(TextAlignment.CENTER);
            criteriaInfo.getChildren().add(temp);
            MenuItem mi = new MenuItem(cri.getLabel());
            mi.setOnAction(actionEvent -> {
                try {
                    chooseCri(cri.getLabel());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.criteria.getItems().add(mi);
            mii = new MenuItem(cri.getLabel());
            mii.setOnAction(actionEvent -> {
                try {
                    chooseFilter(cri.getLabel());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.filter.getItems().add(mii);
        }
        Text OVERALL = new Text("Overall");
        OVERALL.setFont(new Font(15));
        OVERALL.setWrappingWidth(140);
        OVERALL.setTextAlignment(TextAlignment.CENTER);
        criteriaInfo.getChildren().add(OVERALL);
        score.getChildren().add(criteriaInfo);
        HashMap<String, Double> overall = gs.getStudentOverall(Config.ALL, 0);
        HashMap<String, HashMap<String, Double>> allGrad = gs.grabAllGrad();
        for (String stuName: overall.keySet()) {
            HBox info = new HBox();
            info.setPrefHeight(25);
            Label name = new Label(stuName);
            name.setFont(new Font(14));
            name.setPrefHeight(25);
            name.setPrefWidth(140);
            name.setMnemonicParsing(false);
            name.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            name.setAlignment(Pos.CENTER);
            info.getChildren().add(name);
            HashMap<String, Double> stuGrade = allGrad.get(stuName);
            for (Criteria cri: criteria) {
                Label score = new Label(String.valueOf(stuGrade.get(cri.getLabel())));
                score.setFont(new Font(14));
                score.setPrefHeight(25);
                score.setPrefWidth(140);
                score.setMnemonicParsing(false);
                score.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
                score.setAlignment(Pos.CENTER);
                info.getChildren().add(score);
            }
            Label all = new Label(String.valueOf(overall.get(stuName)));
            all.setFont(new Font(14));
            all.setPrefHeight(25);
            all.setPrefWidth(140);
            all.setMnemonicParsing(false);
            all.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            all.setAlignment(Pos.CENTER);
            info.getChildren().add(all);
            score.getChildren().add(info);
        }
    }

    public void chooseCri(String cri) throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Grading.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        GradingController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        if (cri.equals("All"))
            cri = "N";
        modifyCriteriaController.initial("N", cri, "N", "N");
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void chooseAss(String ass) throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Grading.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        GradingController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        String cri = this.criteria.getText();
        if (cri.equals("Choose criteria") || cri.equals("All"))
            cri = "N";
        if (ass.equals("All"))
            ass = "N";
        modifyCriteriaController.initial(ass, cri, "N", "N");
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void chooseFilter(String filter) throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Grading.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        GradingController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        String cri = this.criteria.getText();
        if (cri.equals("Choose criteria") || cri.equals("All"))
            cri = "N";
        String ass = this.assignment.getText();
        if (ass.equals("Choose assignment") || ass.equals("All"))
            ass = "N";
        modifyCriteriaController.initial(ass, cri, "N", filter);
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void goBack() throws IOException {
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(parent);
    }

    public void initial(String assignment, String criteria, String name, String sorting) throws IOException {
        courseName.setText(gs.getCurrent().getName() + "\n" + gs.getCurrent().getYear() + "\n" + gs.getCurrent().getSemester());
        HashMap<String, HashMap<String, Double>> grade = null;
        HashMap<Assignment, List<Double>> assignments = gs.getAllAssignment();
        if (!criteria.equals("N"))
            grade = gs.grabByCriteria(criteria);
        if (!assignment.equals("N"))
            grade = gs.grabByCriteriaAndName(criteria, assignment);
        if (criteria.equals("N") && assignment.equals("N"))
            grade = gs.grabAllGrad();
        if (grade == null) {
            goBack();
            return;
        }
        if (!name.equals("N")) {
            this.filter.setVisible(false);
            this.filterLabel.setVisible(false);
            this.assignment.setVisible(false);
            this.assLabel.setVisible(false);
        }
        if (assignment.equals("N")) {
            this.assignment.setVisible(false);
            this.assLabel.setVisible(false);
        }
        if (!criteria.equals("N")) {
            this.assignment.setVisible(true);
            this.assLabel.setVisible(true);
        }
        if (assignment.equals("N") || criteria.equals("N"))
            this.save.setVisible(false);
        this.criteria.setText("Choose criteria");
        this.assignment.setText("Choose assignment");
        this.filter.setText("None");
        if (!assignment.equals("N"))
            this.assignment.setText(assignment);
        else
            this.assignment.setText("All");
        if (!sorting.equals(("N")))
            this.filter.setText(sorting);
        if (!criteria.equals("N"))
            this.criteria.setText(criteria);
        HashMap<String, Double> temp = null;
        for (String a: grade.keySet()) {
            temp = grade.get(a);
            break;
        }
        if (temp == null) {
            goBack();
            return;
        }
        List<String> title = new ArrayList<>(temp.keySet());
        Collections.sort(title);
        if (title.contains("Overall")) {
            title.remove("Overall");
            title.add("Overall");
        }
        HBox criteriaInfo = new HBox();
        criteriaInfo.setPrefHeight(25);
        Text NAME = new Text("Name");
        NAME.setFont(new Font(15));
        NAME.setWrappingWidth(140);
        NAME.setTextAlignment(TextAlignment.CENTER);
        criteriaInfo.getChildren().add(NAME);
        for (String cri: title) {
            if (criteria.equals("Bonus") && cri.contains("total"))
                continue;
            Text t = new Text(cri);
            t.setFont(new Font(15));
            t.setWrappingWidth(140);
            t.setTextAlignment(TextAlignment.CENTER);
            criteriaInfo.getChildren().add(t);
            if (cri.equals(sorting))
                continue;
            MenuItem mi = new MenuItem(cri);
            mi.setOnAction(actionEvent -> {
                try {
                    chooseFilter(cri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.filter.getItems().add(mi);
        }
        if (!sorting.equals("Name")) {
            MenuItem mi = new MenuItem("Name");
            mi.setOnAction(actionEvent -> {
                try {
                    chooseFilter("Name");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.filter.getItems().add(mi);
        }
        if (!assignment.equals("N") && !criteria.equals("N")) {
            Text random = new Text("Comment");
            random.setFont(new Font(15));
            random.setWrappingWidth(140);
            random.setTextAlignment(TextAlignment.CENTER);
            criteriaInfo.getChildren().add(random);
        }
        for (Criteria cri: gs.getCourseCriteria()) {
            if (cri.getLabel().equals(criteria))
                continue;
            MenuItem mi = new MenuItem(cri.getLabel());
            mi.setOnAction(actionEvent -> {
                try {
                    chooseCri(cri.getLabel());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.criteria.getItems().add(mi);
        }
        if (!criteria.equals("N")) {
            MenuItem miiiii = new MenuItem("All");
            miiiii.setOnAction(actionEvent -> {
                try {
                    chooseCri("All");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.criteria.getItems().add(miiiii);
        }
        for (Assignment ass: assignments.keySet()) {
            if (ass.getName().equals(assignment) || !ass.getCriteria().getLabel().equals(criteria))
                continue;
            MenuItem mi = new MenuItem(ass.getName());
            mi.setOnAction(actionEvent -> {
                try {
                    chooseAss(ass.getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.assignment.getItems().add(mi);
        }
        if (!assignment.equals("N")) {
            MenuItem miiiii = new MenuItem("All");
            miiiii.setOnAction(actionEvent -> {
                try {
                    chooseCri("All");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.assignment.getItems().add(miiiii);
        }
        score.getChildren().add(criteriaInfo);
        List<Map.Entry<String, HashMap<String, Double>>> sort = new ArrayList<>(grade.entrySet());
        sort.sort(((o1, o2) -> {
            String s1;
            String s2;
            if (sorting.equals("Name")) {
                s1 = o1.getKey();
                s2 = o2.getKey();
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
            }
            else {
                s1 = String.valueOf(o1.getValue().get(sorting));
                s2 = String.valueOf(o2.getValue().get(sorting));
                char[] chars1 = s1.toCharArray();
                char[] chars2 = s2.toCharArray();
                int i = 0;
                while (i < chars1.length && i < chars2.length) {
                    if (chars1[i] > chars2[i]) {
                        return -1;
                    } else if (chars1[i] < chars2[i]) {
                        return 1;
                    } else {
                        i++;
                    }
                }
                if (i == chars1.length)
                    return 1;
                if (i == chars2.length)
                    return -1;
                return 0;
            }
        }));
        for (Map.Entry<String, HashMap<String, Double>> t: sort) {
            if (!name.equals("N") && !t.getKey().equals(name))
                continue;
            HBox info = new HBox();
            info.setPrefHeight(25);
            Label thename = new Label(t.getKey());
            thename.setFont(new Font(14));
            thename.setPrefHeight(25);
            thename.setPrefWidth(140);
            thename.setMnemonicParsing(false);
            thename.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
            thename.setAlignment(Pos.CENTER);
            info.getChildren().add(thename);
            HashMap<String, Double> stuGrade = t.getValue();
            for (String cri: title) {
                if (assignment.equals("N") || criteria.equals("N")) {
                    Label score = new Label(String.valueOf(stuGrade.get(cri)));
                    score.setFont(new Font(14));
                    score.setPrefHeight(25);
                    score.setPrefWidth(140);
                    score.setMnemonicParsing(false);
                    score.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
                    score.setAlignment(Pos.CENTER);
                    info.getChildren().add(score);
                }
                else {
                    TextField score = new TextField();
                    if (cri.contains("percentage"))
                        score.setText(String.valueOf(stuGrade.get(cri)));
                    else if(criteria.equals("Bonus"))
                        continue;
                    else {
                        for (Assignment ass: assignments.keySet()) {
                            if (ass.getName().equals(assignment)) {
                                score.setText(String.valueOf(stuGrade.get(cri) - ass.getTotal()));
                                break;
                            }
                        }
                    }
                    score.setFont(new Font(12));
                    score.setPrefHeight(30);
                    score.setPrefWidth(140);
                    score.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 1 1 1 1");
                    score.setAlignment(Pos.CENTER);
                    info.getChildren().add(score);
                }
            }
            if (!assignment.equals("N") && !criteria.equals("N")) {
                TextField random = new TextField("Comment");
                random.setFont(new Font(12));
                random.setPrefWidth(140);
                random.setAlignment(Pos.CENTER_LEFT);
                info.getChildren().add(random);
            }
            score.getChildren().add(info);
        }
    }

    public void save() {

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

    public void goAssignment() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Assignments.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        AssignmentsController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial();
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }

    public void goStatistic() throws IOException {
        FXMLLoader modify = new FXMLLoader(getClass().getResource("Statistics.fxml"));
        Parent active_fxml = modify.load();
        Scene active = new Scene(active_fxml, 1024, 768);
        StatisticsController modifyCriteriaController = modify.getController();
        modifyCriteriaController.setGS(gs);
        modifyCriteriaController.setParent(parent);
        modifyCriteriaController.initial();
        Stage window = (Stage) courseName.getScene().getWindow();
        window.setScene(active);
    }
}
