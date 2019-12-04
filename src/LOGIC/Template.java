package LOGIC;

import java.util.List;

public class Template {
    private String name;
    private List<Criteria> criteria;

    public Template(String name, List<Criteria> criteria) {
        this.name = name;
        this.criteria = criteria;
    }

    public String getName() {
        return name;
    }

    public List<Criteria> getCriteria() {
        return criteria;
    }
}
