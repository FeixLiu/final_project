package LOGIC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        GradingSystem gs = new GradingSystem();
        gs.logIn("123456");
        Criteria a = new Criteria("Exam", 30);
        Criteria b = new Criteria("LOGIC.Assignment", 20);
        Criteria c = new Criteria("LOGIC.Test", 50);
        List<Criteria> criteria = new ArrayList<>();criteria.add(a); criteria.add(b); criteria.add(c);
        gs.addCourse("591", criteria, "Fall", "2019");
        gs.addCourse("591", criteria, "Fall", "2018");
        gs.archiveCourse("591", "2018", "Fall");
        gs.chooseCourse("591", "2019", "Fall");
        gs.deleteCriteria("LOGIC.Test");
        gs.modifyCriteria("Exam", 80);
        List<Criteria> cri = gs.getCourseCriteria();
        gs.addStudentsFromFile("./src/student.csv");
        gs.addOneStudent(new Name("Xiaoyu", "An"), new Id("101112"), new Email("xyan@bu.edu"), Config.GRADUATE);
        gs.modifyStudentStatus("101112", Config.DROP_STUDENT);
        gs.deleteStudent("101112");
        List<Integer> number = gs.getCourseStudentsNumber();
        gs.giveBonus("Yuang Liu", 12);
        HashMap<String, Double> bonus = gs.getBonus();
        gs.addSingleAssignment("First", "LOGIC.Assignment", "2019", "12", "21", 20, 70);
        gs.addSingleAssignment("Second", "LOGIC.Assignment", "2019", "12", "21", 20, 50);
        gs.modifyAssignmentPercentage("First", 50, "LOGIC.Assignment");
        List<Double> part = new ArrayList<>(); part.add(60.0); part.add(60.0);
        gs.addMultiAssignment("Midterm", "Exam", part, "2019", "12", "30", 50, 80);
        gs.addMultiAssignment("Final", "Exam", part, "2019", "12", "30", 50, 80);
        gs.modifySubAssignmentPercentage("Midterm", part,"Exam");
        HashMap<String, HashMap<String, Double>> grade = gs.grabByCriteriaAndName("LOGIC.Assignment", "First");
        double percentage = 75;
        for (String name: grade.keySet()) {
            HashMap<String, Double> temp = grade.get(name);
            for (String key: temp.keySet()) {
                if (key.contains("percentage"))
                    temp.replace(key, percentage);
            }
            grade.replace(name, temp);
            percentage -= 10;
        }
        gs.giveGrade(grade, "LOGIC.Assignment", "First");
        grade = gs.grabByCriteriaAndName("LOGIC.Assignment", "First");
        grade = gs.grabByCriteria("LOGIC.Assignment");
        grade = gs.grabByCriteriaAndName("Exam", "Midterm");
        double lose = -5;
        for (String name: grade.keySet()) {
            HashMap<String, Double> temp = grade.get(name);
            for (String key: temp.keySet()) {
                if (key.contains("total"))
                    temp.replace(key, lose);
            }
            grade.replace(name, temp);
            lose -= 5;
        }
        gs.giveGrade(grade, "Exam", "Midterm");
        gs.addOneStudent(new Name("Jun", "Xiao"), new Id("789"), new Email("junxiao@bu.edu"), Config.UNDERGRADUATE);
        grade = gs.grabByCriteriaAndName("Exam", "Midterm");
        grade = gs.grabByCriteria("Exam");
        grade = gs.grabAllGrad();
        HashMap<String, Double> overall = gs.getStudentOverall(Config.ALL, 10);
        gs.getCurve();
        List<String> unselect = new ArrayList<>();
        List<Double> statistical = gs.getStudentStatistical(unselect, overall);
        HashMap<String, Character> finalGrade = new HashMap<>();
        char A = 'A';
        for (String name: overall.keySet())
            finalGrade.put(name, A++);
        gs.giveFinalGrade(finalGrade);
        HashMap<String, Character> fi = gs.getFinalGrade();
        HashMap<String, String> comment = new HashMap<>();
        comment.put("Yuang Liu", "GOOD");
        comment.put("Jun Xiao", "EXCEL");
        gs.giveComment(comment);
        comment = gs.getComment();
        gs.saveAsTemplate("LOGIC.Test");
        List<Template> templates = gs.getTemplates();
        gs.exportAsFile("final.csv");
    }

    private static void outputGrade(HashMap<String, HashMap<String, Double>> grade) {
        for (String name: grade.keySet()) {
            System.out.print(name + "\t");
            System.out.println(grade.get(name));
        }
        System.out.println("\n\n\n");
    }

    private static void outputAssignmentGrade(GradingSystem gs) {
        for (Assignment assignment: gs.getAllAssignment().keySet()) {
            HashMap<Student, Double> grade = assignment.getGrade();
            System.out.println("LOGIC.Assignment:\t" + assignment.getName());
            for (Student student: grade.keySet()) {
                System.out.println("LOGIC.Name:\t" + student.getName().getName());
                System.out.println("Grade:\t" + grade.get(student));
            }
            List<Assignment> child = assignment.getChildren();
            if (child.size() != 0) {
                System.out.println("Child grade\n");
                for (Assignment ass: child) {
                    grade = ass.getGrade();
                    System.out.println("LOGIC.Assignment:\t" + ass.getName());
                    for (Student student : grade.keySet()) {
                        System.out.println("LOGIC.Name:\t" + student.getName().getName());
                        System.out.println("Grade:\t" + grade.get(student));
                    }
                    System.out.println();
                }
            }
            System.out.println("\n\n");
        }
    }

    private static void outputAssignment(GradingSystem gs) {
        for (Assignment assignment: gs.getAllAssignment().keySet()) {
            assInfo(assignment);
            List<Assignment> child = assignment.getChildren();
            if (child.size() != 0) {
                System.out.println("Sub");
                for (Assignment ass: child) {
                    assInfo(ass);
                    System.out.println("parent:\t" + ass.getParent().getName());
                }
            }
        }
    }

    private static void assInfo(Assignment ass) {
        System.out.println("name:\t" + ass.getName());
        System.out.println("course name:\t" + ass.getCourse().getName());
        System.out.println("due date:\t" + ass.getDue().getDate());
        System.out.println("assign date:\t" + ass.getAssigned().getDate());
        System.out.println("criteria:\t" + ass.getCriteria().getLabel());
        System.out.println("total:\t" + ass.getTotal());
        System.out.println("percentage:\t" + ass.getPercentage());
    }

    private static void outputStudent(GradingSystem gs) {
        for (Student s: gs.getAllStudent()) {
            System.out.println(s.getName().getName());
            System.out.println(s.getKind());
            System.out.println(s.getId().getId());
            System.out.println(s.getEmail().getEmail());
            System.out.println(s.getStatus());
        }
    }

    private static void output(GradingSystem gs) {
        System.out.println("AAAAA");
        for (Course course : gs.getActive()) {
            outputInfo(course);
        }
        System.out.println("BBBBB");
        for (Course course : gs.getInactive()) {
            outputInfo(course);
        }
        System.out.println("CCCCC");
    }

    private static void outputInfo(Course course) {
        System.out.println(course.getName());
        System.out.println(course.getYear());
        System.out.println(course.getSemester());
        for (Criteria cri: course.getCriteria()) {
            System.out.println(cri.getLabel());
            System.out.println(cri.getPercentage());
        }
    }
}
