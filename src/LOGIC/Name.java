package LOGIC;

public class Name {
    private String first;
    private String last;

    public Name(String first, String last) {
        this.first = first;
        this.last = last;
    }

    public String getFirst() {
        return first;
    }

    public String getLast() {
        return last;
    }

    public String getName() {
        if (getFirst().length() == 0 || getLast().length() == 0)
            return getFirst().length() == 0 ? getLast() : getFirst();
        return getFirst() + " " + getLast();
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public void setLast(String last) {
        this.last = last;
    }
}
