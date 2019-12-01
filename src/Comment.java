public class Comment {
    private String comment;
    private Date date;

    public Comment(String comment) {
        this.comment = comment;
        date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }
}
