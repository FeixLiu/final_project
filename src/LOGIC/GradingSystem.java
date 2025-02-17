package LOGIC;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GradingSystem {
    private Password pass;
    private List<Course> active;
    private List<Course> inactive;
    private List<Template> templates;
    private Course currentView;
    private DatabaseDAO dao;

    public GradingSystem() {
        dao = new DatabaseDAO();
        this.active = dao.getActive();
        this.inactive = dao.getInactive();
        this.templates = dao.getTemplate();
        pass = new Password("123456");
        currentView = null;
    }

    public GradingSystem(List<Course> active, List<Course> inactive, List<Template> templates) {
        this.active = active;
        this.inactive = inactive;
        this.templates = templates;
        pass = new Password("123456");
        currentView = null;
    }

    public boolean logIn(String pass) {
        return this.pass.checkPass(pass);
    }

    public boolean chooseCourse(String name, String year, String semester) {
        for(Course c: active) {
            if(c.getName().equals(name) && c.getYear().equals(year) && c.getSemester().equals(semester)) {
                currentView = c;
                return true;
            }
        }
        for(Course c: inactive) {
            if(c.getName().equals(name) && c.getYear().equals(year) && c.getSemester().equals(semester)) {
                currentView = c;
                return true;
            }
        }
        return false;
    }

    public List<Course> getActive() {
        return active;
    }

    public List<Course> getInactive() {
        return inactive;
    }

    public void returnMain() {
        currentView = null;
    }

    public void archiveCourse(String name, String year, String semester) {
        Course temp = null;
        for(Course c: active) {
            if(c.getName().equals(name) && c.getYear().equals(year) && c.getSemester().equals(semester)) {
                temp = c;
                break;
            }
        }
        if (temp != null) {
            inactive.add(temp);
            active.remove(temp);
            active.remove(temp);
            temp.setStatus(Config.INACTIVE_COURSE);
            dao.archiveCourse(name, year, semester);
        }
    }

    public boolean addStudentsFromFile(String path) {
        return currentView.addStudentsFromFile(path, dao);
    }

    public boolean addOneStudent(Name name, Id id, Email email, String kind) {
        if (currentView.getStatus().equals(Config.INACTIVE_COURSE))
            return false;
        dao.addOneStudent(name, id.getId(), email.getEmail(), kind, currentView);
        HashMap<Name, String> temp = new HashMap<>();
        temp.put(name, Config.LATELY_ENROLL);
        dao.giveComment(temp, currentView);
        currentView.addOneStudent(name, id, email, kind);
        return true;
    }

    public HashMap<Assignment, List<Double>> getAllAssignment() {
        return currentView.getAssignments();
    }

    public List<Student> modifyStudentStatus(String id, String status) {
        dao.modifyStudentStatus(id, status, currentView);
        return currentView.modifyStudentStatus(id, status);
    }

    public List<Student> modifyStudentYear(String id, String year) {
        dao.modifyStudentKind(id, year, currentView);
        return currentView.modifyStudentYear(id, year);
    }

    public List<Student> modifyStudentEmail(String id, String email) {
        dao.modifyStudentEmail(id, email, currentView);
        return currentView.modifyStudentEmail(id, email);
    }

    public List<Student> modifyStudentName(String id, String name) {
        dao.modifyStudentName(id, name, currentView);
        return currentView.modifyStudentName(id, name);
    }

    public HashMap<Assignment, List<Double>> addSingleAssignment(String name, String criteria,
                                                                 String year, String month, String day,
                                                                 double totalPoint, double percentage) {
        Date due = new Date(year, month, day);
        currentView.addSingleAssignment(name, criteria, due, totalPoint, percentage, dao);
        return currentView.getAssignments();
    }

    public double getCurve() {
        return currentView.getCurve();
    }

    public HashMap<Assignment, List<Double>> addMultiAssignment(String name, String criteria, List<Double> partPercentage,
                                                                String year, String month, String day,
                                                                double totalPoint, double percentage) {
        Date due = new Date(year, month, day);
        currentView.addMultiAssignment(name, criteria, due, totalPoint, percentage, partPercentage, dao);
        return currentView.getAssignments();
    }

    public HashMap<String, String> getComment() {
        return currentView.getComment();
    }

    public HashMap<String, String> grabCriteriaOverall(String criteria) {
        return currentView.grabCriteriaOverall(criteria);
    }

    public HashMap<String, HashMap<String, Double>> grabAllGrad() {
        return currentView.grabAllGrad();
    }

    public HashMap<String, HashMap<String, Double>> grabByCriteria(String criteria) {
        return currentView.grabByCriteria(criteria);
    }

    public HashMap<Student, Double> grabAllOverall() {
        return currentView.grabAllOverall();
    }

    public void giveGrade(HashMap<String, HashMap<String, Double>> grade, String criteria, String name) {
        currentView.giveGrade(grade, criteria, name, dao);
    }

    public void giveComment(HashMap<String, String> comments) {
        HashMap<Name, String> temp = new HashMap<>();
        for (Student student: currentView.getStudents()) {
            if (comments.get(student.getName().getName()) != null)
                temp.put(student.getName(), comments.get(student.getName().getName()));
        }
        dao.giveComment(temp, currentView);
        currentView.giveComment(comments);
    }

    public HashMap<String, HashMap<String, Double>> grabByCriteriaAndName(String criteria, String name) {
        return currentView.grabByCriteriaAndName(criteria, name);
    }

    public Course getCurrent() {
        return currentView;
    }

    public List<Student> deleteStudent(String id) {
        dao.deleteStudent(id, currentView);
        return currentView.deleteStudent(id);
    }

    public void addCourse(String name, List<Criteria> criteria, String semester, String year) {
        Course course = new Course(name, criteria, semester, Config.ACTIVE_COURSE, year, dao);
        active.add(course);
        for (Criteria cri: criteria)
            cri.setCourse(course);
    }

    public List<Template> getTemplates() {
        return templates;
    }

    public List<Integer> getCourseStudentsNumber() {
        return currentView.studentsNumber();
    }

    public List<Criteria> getCourseCriteria() {
        return currentView.getCriteria();
    }

    public List<Criteria> modifyCriteria(String name, double percentage) {
        //return currentView.modifyCriteria(name, percentage / 100);
        return currentView.modifyCriteria(name, percentage, dao);
    }

    public List<Criteria> deleteCriteria(String name) {
        return currentView.deleteCriteria(name, dao);
    }

    public List<Student> getAllStudent() {
        return currentView.getStudents();
    }

    public List<Double> getStudentStatistical(List<String> unselect, HashMap<String, Double> currentStudent) {
        HashMap<String, Double> copy = new HashMap<>(currentStudent);
        for (String name: unselect)
            copy.remove(name);
        List<Double> rst = new ArrayList<>();
        double temp = Statistical.average(copy);
        BigDecimal bd = new BigDecimal(temp);
        temp = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        rst.add(temp);
        temp = Statistical.median(copy);
        bd = new BigDecimal(temp);
        temp = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        rst.add(temp);
        temp = Statistical.standardDev(copy);
        bd = new BigDecimal(temp);
        temp = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        rst.add(temp);
        return rst;
    }

    public HashMap<String, Double> getStudentOverall(String kind, double curve) {
        currentView.setCurve(curve);
        return currentView.getStudentOverall(kind);
    }

    public HashMap<String, Double> getBonus() {
        return currentView.getBonus();
    }

    public void giveBonus(String name, double bouns) {
        currentView.giveBonus(name, bouns);
        //currentView.giveBonus(name, bouns / 100);
    }

    public void giveFinalGrade(HashMap<String, Character> finalGrade) {
        currentView.giveFinalGrade(finalGrade, dao);
    }

    public HashMap<String, Character> getFinalGrade() {
        return currentView.getFinalGrade();
    }

    public void saveAsTemplate(String name) {
        for (Template template: templates)
            if (template.getName().equals(name))
                return;
        List<Criteria> criteria = new ArrayList<>(currentView.getCriteria());
        dao.saveAsTemplate(name, currentView);
        templates.add(new Template(name, criteria));
    }

    public void exportAsFile(String path) {
        currentView.exportAsFile(path);
    }

    public void modifyAssignmentPercentage(String name, double percentage, String criteria) {
        currentView.modifyAssignmentPercentage(name, percentage, criteria, dao);
    }

    public void modifySubAssignmentPercentage(String name, List<Double> percentage, String criteria) {
        currentView.modifySubAssignmentPercentage(name, percentage, criteria, dao);
    }
}