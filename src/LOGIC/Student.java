package LOGIC;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private Name name;
    private Email email;
    private Id id;
    private List<Comment> comments;
    private String status;
    private String kind;

    public Student(Name name, Email email, Id id, String status, String kind) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.status = status;
        this.kind = kind;
        comments = new ArrayList<>();
    }

    public Name getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Id getId() {
        return id;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getKind() {
        return kind;
    }

    public String getStatus() {
        return status;
    }

    public void setName(String name) {
        String[] names = name.split(" ");
        if (names.length == 1) {
            this.name.setFirst("");
            this.name.setLast(names[0]);
        }
        else {
            StringBuilder temp = new StringBuilder();
            for (int i = 0; i < names.length - 1; i++)
                temp.append(names[i]);
            this.name.setFirst(temp.toString());
            this.name.setLast(names[names.length - 1]);
        }
    }

    public void setYear(String kind) {
        this.kind = kind;
    }

    public void setEmail(String email) {
        this.email.setEmail(email);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setComments(String comments) {
        this.comments.add(new Comment(comments));
    }
}
