public class Criteria {
    private Course course;
    private String label;
    private double percentage;

    public Criteria(Course course, String label, double percentage) {
        this.course = course;
        this.label = label;
        this.percentage = percentage;
    }

    public Course getCourse() {
        return course;
    }

    public double getPercentage() {
        return percentage;
    }

    public String getLabel() {
        return label;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
