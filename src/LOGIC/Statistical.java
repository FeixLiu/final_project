package LOGIC;

import java.util.*;

public class Statistical {
    public static double average(Assignment assignment) {
        HashMap<Student, Double> all = assignment.getGrade();
        double total = 0;
        for (double d: all.values())
            total += d;
        return total / all.size();
    }

    public static double median(Assignment assignment) {
        HashMap<Student, Double> all = assignment.getGrade();
        List<Double> allGrade = new ArrayList<>(all.values());
        Collections.sort(allGrade);
        if (allGrade.size() % 2 != 0)
            return allGrade.get(allGrade.size() / 2);
        return (allGrade.get(allGrade.size() / 2) + allGrade.get(allGrade.size() / 2 - 1)) / 2;
    }

    public static double standardDev(Assignment assignment) {
        double average = Statistical.average(assignment);
        double total = 0;
        HashMap<Student, Double> all = assignment.getGrade();
        for (double d: all.values())
            total += (d - average) * (d - average);
        return Math.sqrt(total / all.size());
    }

    public static double average(HashMap<String, Double> student) {
        double total = 0;
        for (double grade: student.values())
            total += grade;
        return total / student.size();
    }

    public static double median(HashMap<String, Double> student) {
        List<Double> allGrade = new ArrayList<>(student.values());
        Collections.sort(allGrade);
        if (allGrade.size() % 2 != 0)
            return allGrade.get(allGrade.size() / 2);
        return (allGrade.get(allGrade.size() / 2) + allGrade.get(allGrade.size() / 2 - 1)) / 2;
    }

    public static double standardDev(HashMap<String, Double> student) {
        double average = Statistical.average(student);
        double total = 0;
        for (double d: student.values())
            total += (d - average) * (d - average);
        return Math.sqrt(total / student.size());
    }
}
