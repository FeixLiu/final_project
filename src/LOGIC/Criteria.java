package LOGIC;

public class Criteria {
    private Course course;
    private String label;
    private Percentage percentage;

    public Criteria(String label, double percentage) {
        this.label = label;
        this.percentage = new Percentage(percentage);
    }

    public Criteria(Course course, String label, double percentage) {
        this.course = course;
        this.label = label;
        this.percentage = new Percentage(percentage);
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Course getCourse() {
        return course;
    }

    public double getPercentage() {
        return percentage.getPercentage();
    }

    public String getLabel() {
        return label;
    }

    public void setPercentage(double percentage) {
        this.percentage.setPercentage(percentage);
    }
}
