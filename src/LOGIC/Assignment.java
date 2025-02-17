package LOGIC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Assignment {
    private String name;
    private Criteria criteria;
    private Percentage percentage;
    private List<Assignment> children;
    private Assignment parent;
    private Course course;
    private Date due;
    private Date assigned;
    private HashMap<Student, Double> grade;
    private Credit total;

    public Assignment(String name, Criteria criteria, double percentage,
                      Course course, Date due, Date assigned, double total) {
        //constructor for first level assignment
        this.name = name;
        this.criteria = criteria;
        this.percentage = new Percentage(percentage);
        this.course = course;
        this.due = due;
        this.assigned = assigned;
        this.total = new Credit(total);
        parent = null;
        children = new ArrayList<>();
        grade = new HashMap<>();
    }

    public Assignment(String name, Criteria criteria, double percentage,
                      Course course, Date due, Date assigned, double total, Assignment parent) {
        //constructor for sub assignment
        this.name = name;
        this.criteria = criteria;
        this.percentage = new Percentage(percentage);
        this.course = course;
        this.due = due;
        this.assigned = assigned;
        this.total = new Credit(total);
        this.parent = parent;
        children = new ArrayList<>();
        grade = new HashMap<>();
    }

    public void setPercentage(double percentage) {
        this.percentage.setPercentage(percentage);
    }

    public void setDue(Date due) {
        this.due = due;
    }

    public void updateGrade() {
        double total_child = 0;
        for (Assignment child: children)
            total_child += child.getPercentage();
        for (Student s: grade.keySet()) {
            double total = 0;
            for (Assignment child: children) {
                total = total + child.getPercentage() * child.getOneStudent(s) / total_child;
            }
            grade.put(s, total);
        }
    }

    public void setTotal(double total) {
        this.total.setTotal(total);
    }

    public void setChildren(Assignment child) {
        children.add(child);
    }

    public double getOneStudentUnderOneCriteria(Student s) {
        for (Student stu: grade.keySet()) {
            if (!stu.getName().getName().equals(s.getName().getName()))
                continue;
            return grade.get(stu) * percentage.getPercentage();
        }
        return 0;
    }

    public void deleteStudent(Student temp) {
        grade.remove(temp);
    }

    public void setOneGradeByName(String name, double grade) {
        for (Student s: this.grade.keySet()) {
            if (s.getName().getName().equals(name)) {
                this.grade.put(s, grade);
                break;
            }
        }
    }

    public void updateChildren(List<Double> percentage) {
        for (int i = 0; i < children.size(); i++)
            children.get(i).setPercentage(percentage.get(i));
        for (int i = 0; i < children.size(); i++)
            children.get(i).setTotal(total.getTotal() * percentage.get(i) / 100);
        updateGrade();
    }

    public double getOneStudent(Student s) {
        for (Student stu: grade.keySet()) {
            if (!stu.getName().getName().equals(s.getName().getName()))
                continue;
            return grade.get(stu);
        }
        return 0;
    }

    public void setGrade(HashMap<Student, Double> grade) {
        this.grade = grade;
    }

    public void setOneGrade(Student s, double grade) {
        this.grade.put(s, grade);
    }

    public double getPercentage() {
        return percentage.getPercentage();
    }

    public Course getCourse() {
        return course;
    }

    public Assignment getParent() {
        return parent;
    }

    public Criteria getCriteria() {
        return criteria;
    }

    public Date getAssigned() {
        return assigned;
    }

    public Date getDue() {
        return due;
    }

    public double getTotal() {
        return total.getTotal();
    }

    public String getName() {
        return name;
    }

    public List<Assignment> getChildren() {
        return children;
    }

    public HashMap<Student, Double> getGrade() {
        return grade;
    }
}
