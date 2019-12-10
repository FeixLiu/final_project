package LOGIC;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Course {
    private String name;
    private List<Criteria> criteria;
    private List<Assignment> assignments;
    private List<Student> students;
    private Semester semester;
    private Status status;
    private Year year;
    private Curve curve;
    private HashMap<Student, Character> finalGrade;

    public Course(String name, List<Criteria> criteria, String semester, String status, String year, DatabaseDAO dao) {
        this.name = name;
        //flat(criteria);
        this.criteria = new ArrayList<>(criteria);
        this.assignments = new ArrayList<>();
        Criteria bonus = null;
        for (Criteria cri : this.criteria) {
            if (cri.getLabel().equals("Bonus")) {
                bonus = cri;
                break;
            }
        }
        if (bonus == null) {
            bonus = new Criteria(this, "Bonus", 0);
            this.criteria.add(bonus);
        }
        Assignment bonusAssignment = new Assignment("Bonus", bonus, 100, this, new Date("2020", "5", "30"), new Date(), 0);
        assignments.add(bonusAssignment);
        this.students = new ArrayList<>();
        this.semester = new Semester(semester);
        this.status = new Status(status);
        this.year = new Year(year);
        this.curve = new Curve(0);
        this.finalGrade = new HashMap<>();
        dao.addCourse(name, this.criteria, semester, year);
        dao.updateAssignment(assignments, this);
    }

    public Course(String name, List<Criteria> criteria, String semester, String status, String year) {
        this.name = name;
        //flat(criteria);
        this.criteria = new ArrayList<>(criteria);
//        this.assignments = new ArrayList<>();
//        Criteria bonus = null;
//        for (Criteria cri : this.criteria) {
//            if (cri.getLabel().equals("Bonus")) {
//                bonus = cri;
//                break;
//            }
//        }
//        if (bonus == null) {
//            bonus = new Criteria(this, "Bonus", 0);
//            this.criteria.add(bonus);
//        }
//        Assignment bonusAssignment = new Assignment("Bonus", bonus, 100, this, new Date("2020", "5", "30"), new Date(), 0);
//        assignments.add(bonusAssignment);
        this.students = new ArrayList<>();
        this.semester = new Semester(semester);
        this.status = new Status(status);
        this.year = new Year(year);
        this.curve = new Curve(0);
        this.finalGrade = new HashMap<>();
    }

    private void flat(List<Criteria> criteria) {
        double total = 0;
        for (Criteria c: criteria)
            total += c.getPercentage();
        for (Criteria c: criteria)
            c.setPercentage(c.getPercentage() / total);
    }

    public boolean addStudentsFromFile(String path, DatabaseDAO dao) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader(path);
            BufferedReader bf = new BufferedReader(fr);
            String str;
            while ((str = bf.readLine()) != null) {
                arrayList.add(str);
            }
            bf.close();
            fr.close();
        } catch (IOException e) {
            return false;
        }
        HashMap<Student, Double> grade = new HashMap<>();
        for (String s: arrayList) {
            String[] temp = s.split(",");
            if (temp.length != 5)
                return false;
            Name sName = new Name(temp[0], temp[1]);
            Email sEmail = new Email(temp[2]);
            Id id = new Id(temp[3]);
            Student newStu = new Student(sName, sEmail, id, Config.NORMAL_STUDENT, temp[4]);
            dao.addOneStudent(sName, id.getId(), sEmail.getEmail(), temp[4], this);
            students.add(newStu);
            grade.put(newStu, 0.0);
            finalGrade.put(newStu, 'N');
        }
        for(Assignment assignment: assignments)
            assignment.setGrade(grade);
        return true;
    }

    public void addOneStudent(Name name, Id id, Email email, String kind) {
        Student newStudent = new Student(name, email, id, Config.NORMAL_STUDENT, kind);
        students.add(newStudent);
        newStudent.setComments(Config.LATELY_ENROLL);
        finalGrade.put(newStudent, 'N');
        for(Assignment assignment: assignments)
            assignment.setOneGrade(newStudent, 0);
    }

    public List<Student> modifyStudentStatus(String id, String status) {
        for (Student s: students) {
            if (s.getId().getId().equals(id)) {
                s.setStatus(status);
                break;
            }
        }
        return getStudents();
    }

    public List<Student> modifyStudentYear(String id, String year) {
        for (Student s: students) {
            if (s.getId().getId().equals(id)) {
                s.setYear(year);
                break;
            }
        }
        return getStudents();
    }

    public List<Student> modifyStudentEmail(String id, String email) {
        for (Student s: students) {
            if (s.getId().getId().equals(id)) {
                s.setEmail(email);
                break;
            }
        }
        return getStudents();
    }

    public List<Student> modifyStudentName(String id, String name) {
        for (Student s: students) {
            if (s.getId().getId().equals(id)) {
                s.setName(name);
                break;
            }
        }
        return getStudents();
    }

    public void addSingleAssignment(String name, String criteria, Date due, double totalPoint, double percentage, DatabaseDAO dao) {
        Criteria temp = null;
        for (Criteria c: this.criteria) {
            if (c.getLabel().equals(criteria)) {
                temp = c;
                break;
            }
        }
        if (temp == null)
            return;
        Assignment assignment = new Assignment(name, temp, percentage, this, due, new Date(), totalPoint);
        assignments.add(assignment);
        for (Student s: students)
            assignment.setOneGrade(s, 0);
        dao.updateAssignment(assignments, this);
        //updateAssignmentPercentage(criteria);
    }

    public void updateAssignmentPercentage(String criteria) {
        double total = 0;
        for (Assignment ass: assignments) {
            if (ass.getCriteria().getLabel().equals(criteria))
                total += ass.getPercentage();
        }
        for (Assignment ass: assignments) {
            if (ass.getCriteria().getLabel().equals(criteria))
                ass.setPercentage(ass.getPercentage() / total);
        }
    }

    public void giveFinalGrade(HashMap<String, Character> finalGrade, DatabaseDAO dao) {
        this.finalGrade.replaceAll((s, v) -> finalGrade.get(s.getName().getName()));
        HashMap<Name, Character> temp = new HashMap<>();
        for (Student student: students) {
            if (finalGrade.get(student.getName().getName()) != null)
                temp.put(student.getName(), finalGrade.get(student.getName().getName()));
        }
        dao.giveFinalGrade(temp, this);
    }

    public HashMap<String, Double> getBonus() {
        HashMap<String, Double> rst = new HashMap<>();
        for (Assignment c: assignments) {
            if (c.getCriteria().getLabel().equals("Bonus")) {
                for (Student s: students)
                    rst.put(s.getName().getName(), c.getOneStudent(s));
                break;
            }
        }
        return rst;
    }

    public void giveBonus(String name, double bonus) {
        for (Assignment c: assignments) {
            if (c.getCriteria().getLabel().equals("Bonus")) {
                c.setOneGradeByName(name, bonus);
                break;
            }
        }
    }

    public void addMultiAssignment(String name, String criteria, Date due, double totalPoint, double percentage,
                                   List<Double> partPercentage, DatabaseDAO dao) {
        double total = 0;
        for (double pp: partPercentage)
            total += pp;
//        if (total != 1) {
//            for (int i = 0; i < partPercentage.size(); i++)
//                partPercentage.set(i, partPercentage.get(i) / total);
//        }
        Criteria temp = null;
        for (Criteria c: this.criteria) {
            if (c.getLabel().equals(criteria)) {
                temp = c;
                break;
            }
        }
        if (temp == null)
            return;
        Assignment father = new Assignment(name, temp, percentage, this, due, new Date(), totalPoint);
        assignments.add(father);
        for (Student s: students)
            father.setOneGrade(s, 0);
        String part = "part";
        for (int i = 1; i <= partPercentage.size(); i++) {
            String cur = part + i;
//            LOGIC.Assignment child = new LOGIC.Assignment(cur, temp, partPercentage.get(i - 1), this, due, new LOGIC.Date(),
//                    totalPoint * partPercentage.get(i - 1), father);
            double t = totalPoint * partPercentage.get(i - 1) / total;
            BigDecimal bd = new BigDecimal(t);
            t = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            Assignment child = new Assignment(cur, temp, partPercentage.get(i - 1), this, due, new Date(), t, father);
            father.setChildren(child);
            for (Student s: students)
                child.setOneGrade(s, 0);
        }
        dao.updateAssignment(assignments, this);
        //updateAssignmentPercentage(criteria);
    }

    public HashMap<String, Double> getStudentOverall(String kind) {
        double total = 0;
        for (Criteria cri: criteria)
            total += cri.getPercentage();
        HashMap<String, Double> rst = new HashMap<>();
        if (kind.equals(Config.ALL)) {
            for (Student s: students) {
                double overall = 0.0;
                for (Criteria c: criteria)
                    overall += c.getPercentage() * grabGradOfOneStudentForOneCriteria(s, c.getLabel()) / total;
                BigDecimal bd = new BigDecimal(overall);
                overall = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                rst.put(s.getName().getName(), Math.min(overall + curve.getCurve(), 100));
            }
        }
        else if(kind.equals(Config.GRADUATE)) {
            for (Student s: students) {
                if (s.getKind().equals(Config.UNDERGRADUATE))
                    continue;
                double overall = 0.0;
                for (Criteria c: criteria)
                    overall += c.getPercentage() * grabGradOfOneStudentForOneCriteria(s, c.getLabel()) / total;
                BigDecimal bd = new BigDecimal(overall);
                overall = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                rst.put(s.getName().getName(), Math.min(overall + curve.getCurve(), 100));
            }
        }
        else {
            for (Student s: students) {
                if (s.getKind().equals(Config.GRADUATE))
                    continue;
                double overall = 0.0;
                for (Criteria c: criteria)
                    overall += c.getPercentage() * grabGradOfOneStudentForOneCriteria(s, c.getLabel()) / total;
                BigDecimal bd = new BigDecimal(overall);
                overall = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                rst.put(s.getName().getName(), Math.min(overall + curve.getCurve(), 100));
            }
        }
        return rst;
    }

    public HashMap<String, HashMap<String, Double>> grabAllGrad() {
        double total = 0;
        for (Criteria c: criteria)
            total += c.getPercentage();
        HashMap<String, HashMap<String, Double>> rst = new HashMap<>();
        for (Student s: students) {
            HashMap<String, Double> temp = new HashMap<>();
            for (Criteria c: criteria)
                temp.put(c.getLabel(), grabGradOfOneStudentForOneCriteria(s, c.getLabel()));
            double overall = 0;
            for (Criteria c: criteria)
                overall += c.getPercentage() * temp.get(c.getLabel()) / total;
            BigDecimal bd = new BigDecimal(overall);
            overall = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            temp.put("Overall", overall);
            rst.put(s.getName().getName(), temp);
        }
        return rst;
    }

    public HashMap<String, String> getComment() {
        HashMap<String, String> comments = new HashMap<>();
        for (Student s: students) {
            StringBuilder comment = new StringBuilder();
            for (Comment c: s.getComments())
                comment.append(c.getComment()).append(" ");
            comments.put(s.getName().getName(), comment.toString());
        }
        return comments;
    }

    public void giveGrade(HashMap<String, HashMap<String, Double>> grade, String cri, String name, DatabaseDAO dao) {
        for (Assignment ass : assignments) {
            if (!ass.getCriteria().getLabel().equals(cri) || !ass.getName().equals(name))
                continue;
            if (ass.getChildren().size() == 0) {
                for (Student s: students) {
                    double percentage = grade.get(s.getName().getName()).get("percentage");
                    double lose = grade.get(s.getName().getName()).get("total point " + ass.getTotal());
                    if (lose < 0) {
                        if (ass.getTotal() < Math.abs(lose))
                            lose = -ass.getTotal();
                        ass.setOneGrade(s, (ass.getTotal() + lose) / ass.getTotal() * 100);
                    }
                    else
                        ass.setOneGrade(s, Math.max(0, Math.min(percentage, 100)));
                }
            }
            else {
                List<Assignment> child = ass.getChildren();
                for (Assignment assignment: child) {
                    for (Student s: students) {
                        double percentage = grade.get(s.getName().getName()).get(assignment.getName() + " percentage");
                        double lose = grade.get(s.getName().getName()).get(assignment.getName() + " total point " + assignment.getTotal());
                        if (lose < 0) {
                            if (assignment.getTotal() < Math.abs(lose))
                                lose = -assignment.getTotal();
                            assignment.setOneGrade(s, (assignment.getTotal() + lose) / assignment.getTotal() * 100);
                        }
                        else
                            assignment.setOneGrade(s, Math.max(0, Math.min(percentage, 100)));
                    }
                }
                ass.updateGrade();
            }
        }
        dao.updateAssignment(assignments, this);
    }

    public void exportAsFile(String path) {
        HashMap<String, HashMap<String, Double>> allGrade = grabAllGrad();
        try{
            File file =new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getPath(),true);
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            StringBuilder head = new StringBuilder("Name,");
            for (Criteria c: criteria)
                head.append(c.getLabel()).append(",");
            head.append("Overall").append(",");
            head.append("Final Grade\n");
            bufferWriter.write(head.toString());
            for (Student s: students) {
                StringBuilder sb = new StringBuilder();
                sb.append(s.getName().getName()).append(",");
                HashMap<String, Double> temp = allGrade.get(s.getName().getName());
                for (Criteria c: criteria)
                    sb.append(temp.get(c.getLabel())).append(",");
                sb.append(temp.get("Overall")).append(",");
                sb.append(finalGrade.get(s));
                sb.append("\n");
                bufferWriter.write(sb.toString());
            }
            bufferWriter.close();
        } catch(IOException ignored) {
        }
    }

    public void modifyAssignmentPercentage(String name, double percentage, String criteria, DatabaseDAO dao) {
        for (Assignment assignment: assignments)
            if (assignment.getName().equals(name) && assignment.getCriteria().getLabel().equals(criteria))
                assignment.setPercentage(percentage);
        dao.updateAssignment(assignments, this);
//        double total = 0.0;
//        for (LOGIC.Assignment assignment: assignments) {
//            if (assignment.getCriteria().getLabel().equals(criteria))
//                total += assignment.getPercentage();
//        }
//        for (LOGIC.Assignment assignment: assignments) {
//            if (assignment.getCriteria().getLabel().equals(criteria))
//                assignment.setPercentage(assignment.getPercentage() / total);
//        }
    }

    public void modifySubAssignmentPercentage(String name, List<Double> percentage, String criteria, DatabaseDAO dao) {
        for (Assignment assignment: assignments) {
            if (assignment.getName().equals(name) && assignment.getCriteria().getLabel().equals(criteria)) {
                assignment.updateChildren(percentage);
                break;
            }
        }
        dao.updateAssignment(assignments, this);
    }

    public void giveComment(HashMap<String, String> comments) {
        for (Student s: students) {
            if (comments.containsKey(s.getName().getName())) {
                s.setComments(comments.get(s.getName().getName()));
            }
        }
    }

    public HashMap<String, HashMap<String, Double>> grabByCriteria(String cri) {
        HashMap<String, HashMap<String, Double>> rst = new HashMap<>();
        for (Student s: students) {
            HashMap<String, Double> temp = new HashMap<>();
            for (Assignment ass: assignments) {
                if (!ass.getCriteria().getLabel().equals(cri))
                    continue;
                double t = ass.getOneStudent(s);
                BigDecimal bd = new BigDecimal(t);
                t = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                temp.put(ass.getName(), t);
            }
            rst.put(s.getName().getName(), temp);
        }
        return rst;
    }

    public HashMap<String, HashMap<String, Double>> grabByCriteriaAndName(String cri, String name) {
        HashMap<String, HashMap<String, Double>> rst = new HashMap<>();
        for (Student s: students) {
            HashMap<String, Double> temp = new HashMap<>();
            for (Assignment ass: assignments) {
                if (!ass.getCriteria().getLabel().equals(cri) || !ass.getName().equals(name))
                    continue;
                if (ass.getChildren().size() == 0) {
                    double t = ass.getOneStudent(s);
                    BigDecimal bd = new BigDecimal(t);
                    t = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    temp.put("total point " + ass.getTotal(), t);
                    temp.put("percentage", t);
                    t = ass.getOneStudent(s) * ass.getTotal() / 100;
                    bd = new BigDecimal(t);
                    t = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    temp.put("total point " + ass.getTotal(), t);
                }
                else {
                    List<Assignment> child = ass.getChildren();
                    for (Assignment assignment: child) {
                        double t = assignment.getOneStudent(s);
                        BigDecimal bd = new BigDecimal(t);
                        t = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        temp.put(assignment.getName() + " percentage", t);
                        t = assignment.getOneStudent(s) * assignment.getTotal() / 100;
                        bd = new BigDecimal(t);
                        t = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                        temp.put(assignment.getName() + " total point " + assignment.getTotal(), t);
                    }
                }
            }
            rst.put(s.getName().getName(), temp);
        }
        return rst;
    }

    public double grabGradOfOneStudentForOneCriteria(Student s, String c) {
        double assTotal = 0;
        for (Assignment ass: assignments) {
            if (ass.getCriteria().getLabel().equals(c)) {
                assTotal += ass.getPercentage();
            }
        }
        double total = 0;
        for (Assignment ass: assignments) {
            if (!ass.getCriteria().getLabel().equals(c))
                continue;
            total += ass.getOneStudentUnderOneCriteria(s) / assTotal;
        }
        BigDecimal bd = new BigDecimal(total);
        total = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return total;
    }

    public List<Student> deleteStudent(String id) {
        Student temp = null;
        for (Student s: students) {
            if (s.getId().getId().equals(id)) {
                temp = s;
                break;
            }
        }
        if (temp == null)
            return getStudents();
        students.remove(temp);
        deleteStudent(temp);
        return getStudents();
    }

    private void deleteStudent(Student temp) {
        finalGrade.remove(temp);
        for (Assignment ass: assignments)
            ass.deleteStudent(temp);
    }

    public List<Integer> studentsNumber() {
        List<Integer> number = new ArrayList<>();
        number.add(students.size());
        int graduate = 0;
        for (Student s: students) {
            if (s.getKind().equals(Config.GRADUATE))
                graduate++;
        }
        number.add(students.size() - graduate);
        number.add(graduate);
        return number;
    }

    public List<Criteria> modifyCriteria(String name, double percentage, DatabaseDAO dao) {
        Criteria temp = null;
        for (Criteria c: criteria) {
            if (c.getLabel().equals(name)) {
                temp = c;
                break;
            }
        }
        if (temp == null)
            criteria.add(new Criteria(this, name, percentage));
        else
            temp.setPercentage(percentage);
//        double totalPercentage = 0;
//        for (LOGIC.Criteria c: criteria)
//            totalPercentage += c.getPercentage();
//        for (LOGIC.Criteria c: criteria)
//            c.setPercentage(c.getPercentage() / totalPercentage);
        dao.updateCriteria(criteria, this);
        return this.getCriteria();
    }

    public List<Criteria> deleteCriteria(String name, DatabaseDAO dao) {
        Criteria temp = null;
        for (Criteria c: criteria) {
            if (c.getLabel().equals(name)) {
                temp = c;
                break;
            }
        }
        if (temp == null)
            return this.getCriteria();
        criteria.remove(temp);
//        double totalPercentage = 0;
//        for (LOGIC.Criteria c: criteria)
//            totalPercentage += c.getPercentage();
//        for (LOGIC.Criteria c: criteria)
//            c.setPercentage(c.getPercentage() / totalPercentage);
        dao.updateCriteria(criteria, this);
        List<Assignment> outdated = new ArrayList<>();
        for (Assignment assignment: assignments)
            if (assignment.getCriteria().getLabel().equals(name))
                outdated.add(assignment);
        assignments.removeAll(outdated);
        dao.updateAssignment(assignments, this);
        return this.getCriteria();
    }

    public void setCurve(double curve) {
        this.curve.setCurve(curve);
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status.getStatus();
    }

    public void setStatus(String status) {
        this.status.setStatus(status);
    }

    public double getCurve() {
        return curve.getCurve();
    }

    public String getSemester() {
        return semester.getSemester();
    }

    public String getYear() {
        return year.getYear();
    }

    public HashMap<Assignment, List<Double>> getAssignments() {
        HashMap<Assignment, List<Double>> all = new HashMap<>();
        for (Assignment assignment: assignments) {
            List<Double> statistical = new ArrayList<>();
            double average = Statistical.average(assignment);
            BigDecimal bd = new BigDecimal(average);
            average = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            statistical.add(average);
            double median = Statistical.median(assignment);
            bd = new BigDecimal(median);
            median = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            statistical.add(median);
            double standard = Statistical.standardDev(assignment);
            bd = new BigDecimal(standard);
            standard = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            statistical.add(standard);
            all.put(assignment, statistical);
        }
        return all;
    }

    public HashMap<String, String> grabCriteriaOverall(String criteria) {
        HashMap<String, String> rst = new HashMap<>();
        for (Student student: students) {
            double temp = grabGradOfOneStudentForOneCriteria(student, criteria);
            BigDecimal bd = new BigDecimal(temp);
            temp = bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            rst.put(student.getName().getName(), String.valueOf(temp));
        }
        return rst;
    }

    public List<Criteria> getCriteria() {
        return criteria;
    }

    public List<Student> getStudents() {
        return students;
    }

    public HashMap<String, Character> getFinalGrade() {
        HashMap<String, Character> rst = new HashMap<>();
        for (Student s: finalGrade.keySet()) {
            rst.put(s.getName().getName(), finalGrade.get(s));
        }
        return rst;
    }

    public void setStudents(List<Student> students) {
        this.students = new ArrayList<>(students);
    }

    public void setFinalGrade(HashMap<Student, Character> finalGrade) {
        this.finalGrade = new HashMap<>(finalGrade);
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = new ArrayList<>(assignments);
    }
}