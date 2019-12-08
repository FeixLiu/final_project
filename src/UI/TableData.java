package UI;

import LOGIC.Student;
import javafx.scene.control.CheckBox;

/**
 * This class is used exclusively for the purpose of tableView in JavaFX. A class structure must be supplied
 * for row information.
 */
public class TableData {
    private String firstName;
    private String lastName;
    private double score;
    private CheckBox select;
    private String kind;

    public TableData(Student student, double score) {
        this.firstName = student.getName().getFirst();
        this.lastName = student.getName().getLast();
        this.score = score;
        this.select = new CheckBox();
        this.kind = student.getKind();
    }

    public double getScore() {
        return score;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public CheckBox getSelect() {
        return select;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getKind() {
        return kind;
    }
}