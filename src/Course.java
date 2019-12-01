import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Course {
    private String name;
    private List<Criteria> criteria;
    private List<Assignment> assignments;
    private List<Student> students;
    private String semester;
    private String status;
    private String year;
    private double curve;
    private HashMap<Student, Character> finalGrade;

    public Course(String name, List<Criteria> criteria, String semester, String status, String year) {
        this.name = name;
        this.criteria = criteria;
        this.assignments = new ArrayList<>();
        Criteria bonus = new Criteria(this, "Bonus", 0);
        criteria.add(bonus);
        Assignment bonusAssignment = new Assignment("Bonus Assignment", bonus, 0, this, new Date(), new Date(), 0);
        assignments.add(bonusAssignment);
        this.students = new ArrayList<>();
        this.semester = semester;
        this.status = status;
        this.year = year;
        this.curve = 0;
        this.finalGrade = new HashMap<>();
    }

    public boolean addStudentsFromFile(String path) {
        ArrayList<String> arrayList = new ArrayList<>();
        try {
            FileReader fr = new FileReader("./src/a.csv");
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

    public void addSingleAssignment(String name, String criteria, Date due, double totalPoint, double percentage) {
        Criteria temp = null;
        for (Criteria c: this.criteria) {
            if (c.getLabel().equals(criteria)) {
                temp = c;
                break;
            }
        }
        if (temp == null)
            return;
        assignments.add(new Assignment(name, temp, percentage, this, due, new Date(), totalPoint));
    }

    public void giveFinalGrade(HashMap<String, Character> finalGrade) {
        this.finalGrade.replaceAll((s, v) -> finalGrade.get(s.getName().getName()));
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

    public void giveBonus(String name, double bouns) {
        for (Assignment c: assignments) {
            if (c.getCriteria().getLabel().equals("Bonus")) {
                c.setOneGradeByName(name, bouns);
                break;
            }
        }
    }

    public void addMultiAssignment(String name, String criteria, Date due, double totalPoint, double percentage, List<Double> partPercentage) {
        double total = 0;
        for (double pp: partPercentage)
            total += pp;
        if (total != 1) {
            for (int i = 0; i < partPercentage.size(); i++)
                partPercentage.set(i, partPercentage.get(i) / total);
        }
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
        String part = "part";
        for (int i = 1; i <= partPercentage.size(); i++) {
            String cur = part + i;
            Assignment child = new Assignment(cur, temp, partPercentage.get(i), this, due, new Date(),
                    totalPoint * partPercentage.get(i), father);
            father.setChildren(child);
        }
    }

    public HashMap<String, Double> getStudentOverall(List<String> unselect, String kind) {
        HashMap<String, Double> rst = new HashMap<>();
        if (kind.equals(Config.ALL)) {
            for (Student s: students) {
                if (unselect.contains(s.getName().getName()))
                    continue;
                double overall = 0.0;
                for (Criteria c: criteria)
                    overall += c.getPercentage() * grabGradOfOneStudentForOneCriteria(s, c.getLabel());
                rst.put(s.getName().getName(), overall);
            }
        }
        else if(kind.equals(Config.GRADUATE)) {
            for (Student s: students) {
                if (unselect.contains(s.getName().getName()) || s.getKind().equals(Config.UNDERGRADUATE))
                    continue;
                double overall = 0.0;
                for (Criteria c: criteria)
                    overall += c.getPercentage() * grabGradOfOneStudentForOneCriteria(s, c.getLabel());
                rst.put(s.getName().getName(), overall);
            }
        }
        else {
            for (Student s: students) {
                if (unselect.contains(s.getName().getName()) || s.getKind().equals(Config.GRADUATE))
                    continue;
                double overall = 0.0;
                for (Criteria c: criteria)
                    overall += c.getPercentage() * grabGradOfOneStudentForOneCriteria(s, c.getLabel());
                rst.put(s.getName().getName(), overall);
            }
        }
        return rst;
    }

    public HashMap<String, HashMap<String, Double>> grabAllGrad() {
        HashMap<String, HashMap<String, Double>> rst = new HashMap<>();
        for (Student s: students) {
            HashMap<String, Double> temp = new HashMap<>();
            for (Criteria c: criteria)
                temp.put(c.getLabel(), grabGradOfOneStudentForOneCriteria(s, c.getLabel()));
            double overall = 0;
            for (Criteria c: criteria)
                overall += c.getPercentage() * temp.get(c.getLabel());
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

    public void giveGrade(HashMap<String, HashMap<String, Double>> grade, String cri, String name) {
        for (Assignment ass : assignments) {
            if (!ass.getCriteria().getLabel().equals(cri) && !ass.getName().equals(name))
                continue;
            if (ass.getChildren().size() == 0) {
                for (Student s: students) {
                    double percentage = grade.get(s.getName().getName()).get("Percentage");
                    double lose = grade.get(s.getName().getName()).get("" + ass.getTotal());
                    if (lose < 0)
                        ass.setOneGrade(s, (ass.getTotal() + lose) / ass.getTotal());
                    else
                        ass.setOneGrade(s, Math.max(0, Math.min(percentage, 1)));
                }
            }
            else {
                List<Assignment> child = ass.getChildren();
                for (Assignment assignment: child) {
                    for (Student s: students) {
                        double percentage = grade.get(s.getName().getName()).get(assignment.getName() + " percentage");
                        double lose = grade.get(s.getName().getName()).get(assignment.getName() + " " + assignment.getTotal());
                        if (lose < 0)
                            assignment.setOneGrade(s, (assignment.getTotal() + lose) / assignment.getTotal());
                        else
                            assignment.setOneGrade(s, Math.max(0, Math.min(percentage, 1)));
                    }
                }
                ass.updateGrade();
            }
        }
    }

    public void exportAsFile(String path){
        HashMap<String, HashMap<String, Double>> allGrade = grabAllGrad();
        try{
            File file =new File(path);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file.getName(),true);
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            StringBuilder head = new StringBuilder("Name,");
            for (Criteria c: criteria)
                head.append(c.getLabel()).append(",");
            head.append("Overall").append(",");
            head.append("Final Grade");
            bufferWriter.write(head.toString());
            for (Student s: students) {
                StringBuilder sb = new StringBuilder();
                sb.append(s.getName().getName()).append(",");
                HashMap<String, Double> temp = allGrade.get(s.getName().getName());
                for (double score: temp.values())
                    sb.append(score).append(",");
                sb.append(finalGrade.get(s));
                bufferWriter.write(sb.toString());
            }
            bufferWriter.close();
        } catch(IOException ignored) {
        }
    }

    public void modifyAssignmentPercentage(String name, double percentage) {
        Criteria criteria = null;
        for (Assignment assignment: assignments) {
            if (assignment.getName().equals(name)) {
                criteria = assignment.getCriteria();
                assignment.setPercentage(percentage);
                break;
            }
        }
        if (criteria == null)
            return;
        double total = 0.0;
        for (Assignment assignment: assignments) {
            if (assignment.getCriteria().getLabel().equals(criteria.getLabel()))
                total += assignment.getPercentage();
        }
        for (Assignment assignment: assignments) {
            if (assignment.getCriteria().getLabel().equals(criteria.getLabel()))
                assignment.setPercentage(assignment.getPercentage() / total);
        }
    }

    public void modifySubAssignmentPercentage(String name, List<Double> percentage) {
        for (Assignment assignment: assignments) {
            if (assignment.getName().equals(name)) {
                assignment.updateChildren(percentage);
                break;
            }
        }
    }

    public void giveComment(HashMap<String, String> comments) {
        for (Student s: students) {
            try {
                s.setComments(comments.get(s.getName().getName()));
            }
            catch (Exception ignored){}
        }
    }

    public HashMap<String, HashMap<String, Double>> grabByCriteria(String cri) {
        HashMap<String, HashMap<String, Double>> rst = new HashMap<>();
        for (Student s: students) {
            HashMap<String, Double> temp = new HashMap<>();
            for (Assignment ass: assignments) {
                if (!ass.getCriteria().getLabel().equals(cri))
                    continue;
                temp.put(ass.getName(), ass.getOneStudent(s));
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
                if (!ass.getCriteria().getLabel().equals(cri) && !ass.getName().equals(name))
                    continue;
                if (ass.getChildren().size() == 0) {
                    temp.put("Percentage", ass.getOneStudent(s));
                    temp.put("" + ass.getTotal(), ass.getOneStudent(s) * ass.getTotal());
                }
                else {
                    List<Assignment> child = ass.getChildren();
                    for (Assignment assignment: child) {
                        temp.put(assignment.getName() + " percentage", assignment.getOneStudent(s));
                        temp.put(assignment.getName() + " " + assignment.getTotal(), assignment.getOneStudent(s) * assignment.getTotal());
                    }
                }
            }
            rst.put(s.getName().getName(), temp);
        }
        return rst;
    }

    public double grabGradOfOneStudentForOneCriteria(Student s, String c) {
        double total = 0;
        for (Assignment ass: assignments) {
            if (!ass.getCriteria().getLabel().equals(c))
                continue;
            total += ass.getOneStudentUnderOneCriteria(s);
        }
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
        return getStudents();
    }

    public List<Integer> studentsNumber() {
        List<Integer> number = new ArrayList<>();
        number.add(students.size());
        int graduate = 0;
        for (Student s: students) {
            if (s.getKind().equals("Graduate"))
                graduate++;
        }
        number.add(students.size() - graduate);
        number.add(graduate);
        return number;
    }

    public List<Criteria> modifyCriteria(String name, double percentage) {
        Criteria temp = null;
        for (Criteria c: criteria) {
            if (c.getLabel().equals(name)) {
                temp = c;
                break;
            }
        }
        if (temp == null)
            criteria.add(new Criteria(this, name, percentage));
        double totalPercentage = 0;
        for (Criteria c: criteria)
            totalPercentage += c.getPercentage();
        for (Criteria c: criteria)
            c.setPercentage(c.getPercentage() / totalPercentage);
        return this.getCriteria();
    }

    public List<Criteria> deleteCriteria(String name) {
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
        double totalPercentage = 0;
        for (Criteria c: criteria)
            totalPercentage += c.getPercentage();
        for (Criteria c: criteria)
            c.setPercentage(c.getPercentage() / totalPercentage);
        return this.getCriteria();
    }

    public void setCurve(double curve) {
        this.curve = curve;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public double getCurve() {
        return curve;
    }

    public String getSemester() {
        return semester;
    }

    public String getYear() {
        return year;
    }

    public HashMap<Assignment, List<Double>> getAssignments() {
        HashMap<Assignment, List<Double>> all = new HashMap<>();
        for (Assignment assignment: assignments) {
            List<Double> statistical = new ArrayList<>();
            statistical.add(Statistical.average(assignment));
            statistical.add(Statistical.median(assignment));
            statistical.add(Statistical.standardDev(assignment));
            all.put(assignment, statistical);
        }
        return all;
    }

    public List<Criteria> getCriteria() {
        return criteria;
    }

    public List<Student> getStudents() {
        return students;
    }

    public HashMap<Student, Character> getFinalGrade() {
        return finalGrade;
    }
}
