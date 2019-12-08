package LOGIC;

import javax.xml.transform.Result;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseDAO {
    public DatabaseDAO() {
        Connection c = null;
        File file = new File("database.db");
        if (file.exists()) {
            System.out.println("tables have already been created.");
        } else {
            Statement stmt = null;
            try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:database.db");
                System.out.println("Opened database successfully");

                stmt = c.createStatement();
                String sql;
                //create table: Student
                sql = "CREATE TABLE Student " +
                        "(id INT PRIMARY KEY     NOT NULL," +
                        " firstName           TEXT    NOT NULL, " +
                        " lastName            TEXT     NOT NULL, " +
                        " email        TEXT NOT NULL," +
                        " kind        TEXT NOT NULL)";
                stmt.executeUpdate(sql);

                //create table: StudentToCourse
                sql = "CREATE TABLE StudentToCourse " +
                        "(studentId TEXT      NOT NULL," +
                        " courseId           TEXT    NOT NULL, " +
                        " grade            TEXT, " +
                        " status        TEXT NOT NULL," +
                        " comments        TEXT," +
                        "PRIMARY KEY(studentId, courseId))";

                stmt.executeUpdate(sql);

                //create table: Course
                sql = "CREATE TABLE Course " +
                        "(id INTEGER PRIMARY KEY     AUTOINCREMENT," +
                        " curve          REAL, " +
                        " name            TEXT     NOT NULL, " +
                        " semester        TEXT NOT NULL," +
                        " status        TEXT NOT NULL," +
                        " year        TEXT NOT NULL)";
                stmt.executeUpdate(sql);

                //create table: Criteria
                sql = "CREATE TABLE Criteria " +
                        "(id INTEGER PRIMARY KEY     AUTOINCREMENT," +
                        " courseId          INTEGER    NOT NULL, " +
                        " label        TEXT NOT NULL," +
                        " percentage        REAL NOT NULL)";
                stmt.executeUpdate(sql);

                //create table: Assignment
                sql = "CREATE TABLE Assignment " +
                        "(assignmentId INTEGER PRIMARY KEY     AUTOINCREMENT ," +
                        " percentage          REAL    NOT NULL, " +
                        " total          REAL    NOT NULL, " +

                        " name            TEXT     NOT NULL, " +
                        " criteriaId        INT NOT NULL," +
                        " parentId        INTEGER," +
                        " courseId        INTEGER NOT NULL," +
                        " dueDate            TEXT     NOT NULL, " +
                        " assignDate        TEXT NOT NULL)";
                stmt.executeUpdate(sql);

                //create table: StudentAssignment
                sql = "CREATE TABLE StudentAssignment " +
                        "(studentId INT      NOT NULL," +
                        " assignmentId          INTEGER    NOT NULL, " +
                        " grade            REAL     NOT NULL, " +
                        "PRIMARY KEY(studentId, assignmentId))";
                stmt.executeUpdate(sql);

                //create table: GradingSystem
                sql = "CREATE TABLE GradingSystem " +
                        "(password TEXT      NOT NULL) ";
                stmt.executeUpdate(sql);

                //create table: Template
                sql = "CREATE TABLE Template " +
                        "(courseId INTEGER PRIMARY KEY     NOT NULL," +
                        " name        TEXT NOT NULL)";
                stmt.executeUpdate(sql);

                stmt.close();
                c.close();
            } catch (Exception e) {
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
        }

    }

    //util function
    String sqlText(String s) {
        return "'" + s + "'";
    }

    long getCourseId(Course course) {
        Connection c = null;
        Statement stmt = null;
        long courseId = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            //find courseId
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Course where name=" + sqlText(course.getName()) + " and year=" + sqlText(course.getYear()) + " and semester=" + sqlText(course.getSemester()) +  ";");
            while (rs.next()) {
                courseId = rs.getLong("id");
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("getCourseId() failed");
            System.exit(0);
        }
        return courseId;
    }

    long getAssignmentId(String assignmentName) {
        Connection c = null;
        Statement stmt = null;
        long assignmentId = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            //find courseId
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Assignment where name=" + sqlText(assignmentName) + ";");
            while (rs.next()) {
                assignmentId = rs.getLong("assignmentId");
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("getAssignmentId() failed");
            System.exit(0);
        }
        return assignmentId;
    }

    String getStudentId(Name name) {
        Connection c = null;
        Statement stmt = null;
        String studentId = "";
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            //find courseId
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Student where firstName=" + sqlText(name.getFirst()) + " and lastName=" + sqlText(name.getLast()) + ";");
            while (rs.next()) {
                studentId = rs.getString("id");
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("getStudentId() failed");
            System.exit(0);
        }
        return studentId;
    }

    long getCriteriaId(Criteria criteria) {
        Connection c = null;
        Statement stmt = null;
        long criteriaId = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Criteria where label=" + sqlText(criteria.getLabel()) + ";");
            while (rs.next()) {
                criteriaId = rs.getLong("id");
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("getCriteriaId() failed");
            System.exit(0);
        }
        return criteriaId;
    }

    void setAssignmentGrades(HashMap<Student, Double> grades, long assignmentId)  {
        Connection c = null;
        Statement stmt = null;
        String studentId = "";
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();



            for (Map.Entry<Student, Double> entry : grades.entrySet()) {
                Name name = entry.getKey().getName();
                double grade = entry.getValue();
                studentId = getStudentId(name);


                //modify StudentToCourse, update status
                String sql  = "INSERT INTO StudentAssignment (studentId, assignmentId, grade) " +
                        "VALUES (" + sqlText(studentId) + ", " + assignmentId + ", " + grade +");";                stmt.executeUpdate(sql);
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("setAssignmentGrades() failed");
            System.exit(0);
        }
    }

    /*
        List<Course> getActive()
        List<Course> getInactive()
        List<Template> getTemplate()
        void archiveCourse(String name, String year, String semester)
        void addCourse(String name, List criteria, String semester, String year)
        void updateCriteria(List criteria, Course course)
        void saveAsTemplate(String name, Course course)
        void setCurve(double curve, Course course)
        void addOneStudent(Name name, String id, String email, String kind, Course course)
        void modifyStudentStatus(String id, String status, Course course)
        void giveComment(HashMap<String, String> comments, Course course)
        void deleteStudent(String id, Course course)
        void updateAssignment(List assignments, Course course)
        void giveFinalGrade(HashMap<String, Character> finalGrade, Course course)
     */
    void archiveCourse(String name, String year, String semester) {
        long courseId = 0;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Course where name=" + sqlText(name) + " and year=" + sqlText(year) + " and semester=" + sqlText(semester) +  ";");
            while (rs.next()) {
                courseId = rs.getLong("id");
                Statement statementUpdate = c.createStatement();
                statementUpdate.executeUpdate("UPDATE Course set status =" + sqlText(Config.INACTIVE_COURSE) + " where id=" + courseId + ";");
            }
            rs.close();
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("archiveCourse() failed");
            System.exit(0);
        }
    }

    void addCourse(String name, List<Criteria> criterias, String semester, String year) {
        long courseId = 0;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            stmt = c.createStatement();
            //insert a new record into course table
            String sql = "INSERT INTO Course (name, semester, status, year) " +
                    "VALUES (" + sqlText(name) + ", " + sqlText(semester) + ", " + sqlText(Config.ACTIVE_COURSE) + ", " + sqlText(year) +");";
            stmt.executeUpdate(sql);

            //insert records into Criteria table
            ResultSet rs = stmt.executeQuery("SELECT * FROM Course where name=" + sqlText(name) + " and year=" + sqlText(year) + " and semester=" + sqlText(semester) +  ";");
            while (rs.next()) {
                courseId = rs.getLong("id");
            }
            for (Criteria criteria : criterias) {
                String cLabel = criteria.getLabel();
                double percentage = criteria.getPercentage();
                sql  = "INSERT INTO Criteria (courseId, label, percentage) " +
                        "VALUES (" + courseId + ", " + sqlText(cLabel) + ", " + percentage +");";
                stmt.executeUpdate(sql);
            }

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("addCourse() failed");
            System.exit(0);
        }
    }

    void updateCriteria(List<Criteria>  criterias, Course course) {
        //delete all the criterias of this course
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            stmt = c.createStatement();
            stmt.executeUpdate("DELETE from Criteria;");


            long courseId = 0;
            ResultSet rs = stmt.executeQuery("SELECT * FROM Course where name=" + sqlText(course.getName()) + " and year=" + sqlText(course.getYear()) + " and semester=" + sqlText(course.getSemester()) +  ";");
            while (rs.next()) {
                courseId = rs.getLong("id");
            }

            //add new criterias  to this course
            String sql;
            for (Criteria criteria : criterias) {
                String cLabel = criteria.getLabel();
                double percentage = criteria.getPercentage();
                sql  = "INSERT INTO Criteria (courseId, label, percentage) " +
                        "VALUES (" + courseId + ", " + sqlText(cLabel) + ", " + percentage +");";
                stmt.executeUpdate(sql);
            }


            stmt.close();
            c.close();


        } catch (Exception e) {
            System.err.println("updateCriteria() failed");
            System.exit(0);
        }
    }

    void saveAsTemplate(String name, Course course) {
        long courseId = 0;
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            stmt = c.createStatement();

            //get course id
            String sql;
            ResultSet rs = stmt.executeQuery("SELECT * FROM Course where name=" + sqlText(course.getName()) + " and year=" + sqlText(course.getYear()) + " and semester=" + sqlText(course.getSemester()) +  ";");
            while (rs.next()) {
                courseId = rs.getLong("id");
            }

            sql  = "INSERT INTO Template (courseId, name) " +
                    "VALUES (" + courseId + ", " + sqlText(name) + ");";
            stmt.executeUpdate(sql);

            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("saveAsTemplate() failed");
            System.exit(0);
        }
    }

    void setCurve(double curve, Course course) {
        Connection c = null;
        Statement stmt = null;
        long courseId = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Course where name=" + sqlText(course.getName()) + " and year=" + sqlText(course.getYear()) + " and semester=" + sqlText(course.getSemester()) +  ";");
            while (rs.next()) {
                courseId = rs.getLong("id");
            }

            String sql = "UPDATE Course set curve =" + curve + " where id=" + courseId + ";";
            stmt.executeUpdate(sql);


            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("setCurve() failed");
            System.exit(0);
        }
    }

    void addOneStudent(Name name, String id, String email, String kind, Course course) {
        int studentId = Integer.parseInt(id);
        Connection c = null;
        Statement stmt = null;
        long courseId = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            //find courseId
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Course where name=" + sqlText(course.getName()) + " and year=" + sqlText(course.getYear()) + " and semester=" + sqlText(course.getSemester()) +  ";");
            while (rs.next()) {
                courseId = rs.getLong("id");
            }

            //modify Student and StudentToCourse
            //if this is a new student, insert into the student table
            boolean studentExist = false;
            rs = stmt.executeQuery("SELECT * FROM Student where id=" + studentId + ";");
            while (rs.next()) {
                studentExist = true;
            }
            //if this student doesn't exist, create a new one
            if (!studentExist) {
                String sql = "INSERT INTO Student (firstName, lastName, email, id, kind) " +
                        "VALUES (" + sqlText(name.getFirst()) + ", " + sqlText(name.getLast()) + ", " + sqlText(email) + ", " + studentId + ", " + sqlText(kind) +");";
                stmt.executeUpdate(sql);
            }

            //modify studentToCourse table
            String sql = "INSERT INTO StudentToCourse (studentId, courseId, status) " +
                    "VALUES (" + sqlText(id) + ", " + courseId + ", " + sqlText(Config.NORMAL_STUDENT) +");";
            stmt.executeUpdate(sql);


            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("addOneStudent() failed");
            System.exit(0);
        }
    }

    void modifyStudentStatus(String id, String status, Course course) {
        int studentId = Integer.parseInt(id);
        Connection c = null;
        Statement stmt = null;
        long courseId = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            //find courseId
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Course where name=" + sqlText(course.getName()) + " and year=" + sqlText(course.getYear()) + " and semester=" + sqlText(course.getSemester()) +  ";");
            while (rs.next()) {
                courseId = rs.getLong("id");
            }

            //modify StudentToCourse, update status
            String sql = "UPDATE StudentToCourse set status =" + sqlText(status) + " where studentId=" + sqlText(id) + " and courseId=" + courseId + ";";
            stmt.executeUpdate(sql);


            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("modifyStudentStatus() failed");
            System.exit(0);
        }
    }

    void giveComment(HashMap<Name, String> comments, Course course) {
        Connection c = null;
        Statement stmt = null;
        long courseId = getCourseId(course);
        String studentId = "";
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();



            for (Map.Entry<Name, String> entry : comments.entrySet()) {
                Name name = entry.getKey();
                String comment = entry.getValue();
                studentId = getStudentId(name);


                //modify StudentToCourse, update status
                String sql = "UPDATE StudentToCourse set comments =" + sqlText(comment) + " where studentId=" + sqlText(studentId) + " and courseId=" + courseId + ";";
                stmt.executeUpdate(sql);
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("modifyStudentStatus() failed");
            System.exit(0);
        }
    }

    void deleteStudent(String id, Course course) {
        Connection c = null;
        Statement stmt = null;
        long courseId = getCourseId(course);
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            stmt = c.createStatement();
            stmt.executeUpdate("DELETE from StudentToCourse where studentId=" + id + " and courseId=" + courseId + ";");


            stmt.close();
            c.close();

        } catch (Exception e) {
            System.err.println("deleteStudent() failed");
            System.exit(0);
        }
    }

    void updateAssignment(List<Assignment> assignments, Course course) {
        long courseId = getCourseId(course);
        Connection c = null;
        Statement stmt = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            //delete all assignments
            //delete all assignment grades
            stmt = c.createStatement();
            stmt.executeUpdate("DELETE from Assignment;");
            stmt.executeUpdate("DELETE from StudentAssignment;");



            String sql;
            for (Assignment assignment : assignments) {
                String aName = assignment.getName();
                Criteria aCriteria = assignment.getCriteria();
                double aPercentage = assignment.getPercentage();
                Course aCourse = assignment.getCourse();
                Date aDuedate = assignment.getDue();
                Date aAssigndate = assignment.getAssigned();
                double aTotal = assignment.getTotal();
                HashMap<Student, Double> aGrade = assignment.getGrade();
                List<Assignment> aChildren = assignment.getChildren();

                long criteriaId = getCriteriaId(aCriteria);



                sql  = "INSERT INTO Assignment (name, criteriaId, percentage, courseId, dueDate, assignDate, total) " +
                        "VALUES (" + sqlText(aName) + ", " + criteriaId + ", " + aPercentage + ", " + courseId + ", " + sqlText(aDuedate.getDate()) + ", " + sqlText(aAssigndate.getDate()) + ", " + aTotal + ");";
                stmt.executeUpdate(sql);

                long assignmentId = getAssignmentId(aName);
                if (aGrade.size() > 0) {
                    setAssignmentGrades(aGrade, assignmentId);
                }
                if (aChildren.size() > 0) {
                    for (Assignment subAssignment : aChildren) {
                        String subName = subAssignment.getName();
                        Criteria subCriteria = subAssignment.getCriteria();
                        double subPercentage = subAssignment.getPercentage();
                        Course subCourse = subAssignment.getCourse();
                        Date subDuedate = subAssignment.getDue();
                        Date subAssigndate = subAssignment.getAssigned();
                        double subTotal = subAssignment.getTotal();
                        HashMap<Student, Double> subGrade = subAssignment.getGrade();
                        Assignment subParent = subAssignment.getParent();

                        long subCriteriaId = getCriteriaId(subCriteria);
                        sql  = "INSERT INTO Assignment (name, criteriaId, percentage, courseId, dueDate, assignDate, total, parentId) " +
                                "VALUES (" + sqlText(subName) + ", " + subCriteriaId + ", " + subPercentage + ", " + courseId + ", " + sqlText(subDuedate.getDate()) + ", " + sqlText(subAssigndate.getDate()) + ", " + subTotal + ", " + assignmentId + ");";
                        stmt.executeUpdate(sql);

                        long subAssignmentId = getAssignmentId(subName);
                        setAssignmentGrades(subGrade, subAssignmentId);


                    }
                }




            }


            stmt.close();
            c.close();


        } catch (Exception e) {
            System.err.println("updateAssignment() failed");
            System.exit(0);
        }



    }

    void giveFinalGrade(HashMap<Name, Character> finalGrade, Course course) {
        Connection c = null;
        Statement stmt = null;
        long courseId = getCourseId(course);
        String studentId = "";
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");
            stmt = c.createStatement();



            for (Map.Entry<Name, Character> entry : finalGrade.entrySet()) {
                Name name = entry.getKey();
                String grade = entry.getValue().toString();
                studentId = getStudentId(name);


                //modify StudentToCourse, update status
                String sql = "UPDATE StudentToCourse set grade=" + sqlText(grade) + " where studentId=" + sqlText(studentId) + " and courseId=" + courseId + ";";
                stmt.executeUpdate(sql);
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("giveFinalGrade() failed");
            System.exit(0);
        }
    }

    List<Template> getTemplate() {
        List<Template> templates = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        long courseId = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Template" + ";");
            while (rs.next()) {
                List<Criteria> criterias = new ArrayList<>();
                courseId = rs.getLong("courseId");
                String templateName = rs.getString("name");
                Statement stmtCriteria  = c.createStatement();
                ResultSet rsCriteria = stmtCriteria.executeQuery("SELECT * FROM Criteria" + " where courseId=" + courseId + ";");
                while (rsCriteria.next()) {
                    String cLabel = rsCriteria.getString("label");
                    double cPercentage = rsCriteria.getDouble("percentage");
                    Criteria criteria = new Criteria(cLabel, cPercentage);
                    criterias.add(criteria);
                }
                templates.add(new Template(templateName, criterias));
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("getTemplate() failed");
            System.exit(0);
        }
        return templates;
    }

    List<Course> getActive() {
        List<Course> courses = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        long courseId = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Course" + " where status=" + sqlText(Config.ACTIVE_COURSE) + ";");
            while (rs.next()) {
                String courseName = rs.getString("name");
                courseId = rs.getLong("id");
                String semester = rs.getString("semester");
                String year = rs.getString("year");
                String status = Config.ACTIVE_COURSE;
                double curve = rs.getDouble("curve");

                //get criterias
                List<Criteria> criterias = new ArrayList<>();
                Statement stmtCriteria  = c.createStatement();
                ResultSet rsCriteria = stmtCriteria.executeQuery("SELECT * FROM Criteria" + " where courseId=" + courseId + ";");
                while (rsCriteria.next()) {
                    String cLabel = rsCriteria.getString("label");
                    double cPercentage = rsCriteria.getDouble("percentage");
                    Criteria criteria = new Criteria(cLabel, cPercentage);
                    criterias.add(criteria);
                }
                Course course = new Course(courseName, criterias, semester, status, year);
                for (Criteria criteria : criterias) {
                    criteria.setCourse(course);
                }
                courses.add(course);

                //get students
                List<Student> students = new ArrayList<>();
                HashMap<Student, Character> finalGrade = new HashMap<>();
                List<Assignment> assignments = new ArrayList<>();
                Statement stmtStudentCourse = c.createStatement();
                ResultSet rsStudentCourse = stmtStudentCourse.executeQuery("SELECT * FROM StudentToCourse" + " where courseId=" + courseId + ";");
                while (rsStudentCourse.next()) {
                    String studentId = rsStudentCourse.getString("studentId");
                    String studentStatus = rsStudentCourse.getString("status");
                    String comment = rsStudentCourse.getString("comments");
                    String studentFianlGrade = rsStudentCourse.getString("grade");
                    Statement stmtStudent = c.createStatement();
                    ResultSet rsStudent = stmtStudent.executeQuery("SELECT * FROM Student" + " where id=" + sqlText(studentId) + ";");
                    while (rsStudent.next()) {
                        String firstName = rsStudent.getString("firstName");
                        String lastName = rsStudent.getString("lastName");
                        String email = rsStudent.getString("email");
                        String kind = rsStudent.getString("kind");

                        Name studentName = new Name(firstName, lastName);
                        Student student = new Student(studentName, new Email(email), new Id(studentId), studentStatus, kind);
                        student.setComments(comment);
                        students.add(student);
                        finalGrade.put(student, studentFianlGrade == null ? ' ' : new Character(studentFianlGrade.charAt(0)));
                    }
                }

                //get the information of assignments. First get the top level assignments
                Statement stmtAssignment = c.createStatement();
                ResultSet rsAssignment = stmtAssignment.executeQuery("SELECT * FROM Assignment" + " where courseId=" + courseId + ";");
                while (rsAssignment.next()) {
                    long parentId = rsAssignment.getLong("parentId");
                    if (parentId != 0) {
                        //this is a sub-assignement
                        continue;
                    } else {
                        //this is a top-assignment
                        long topId = rsAssignment.getLong("assignmentId");
                        String topName = rsAssignment.getString("name");
                        double topPercentage = rsAssignment.getDouble("percentage");
                        double topTotal = rsAssignment.getDouble("total");
                        long topCriteriaId = rsAssignment.getLong("criteriaId");
                        String topDue = rsAssignment.getString("dueDate");
                        String topAssign = rsAssignment.getString("assignDate");
                        Date topDueDate = new Date(topDue.split("-", 0)[0], topDue.split("-", 0)[1], topDue.split("-", 0)[2]);
                        Date topAssignDate = new Date(topAssign.split("-", 0)[0], topAssign.split("-", 0)[1], topAssign.split("-", 0)[2]);
                        Criteria topCriteria = null;
                        rsCriteria = stmtCriteria.executeQuery("SELECT * FROM Criteria" + " where id=" + topCriteriaId + ";");
                        while (rsCriteria.next()) {
                            String cLabel = rsCriteria.getString("label");
                            double cPercentage = rsCriteria.getDouble("percentage");
                            topCriteria = new Criteria(course, cLabel, cPercentage);
                        }

                        Assignment topAssignment = new Assignment(topName, topCriteria, topPercentage, course, topDueDate, topAssignDate, topTotal);
                        //set grade of top assignment
                        for (Student student : students) {
                            String studentId = student.getId().getId();
                            Statement stmtTopGrade = c.createStatement();
                            ResultSet rsTopGrade = stmtTopGrade.executeQuery("SELECT * FROM StudentAssignment" + " where studentId=" + sqlText(studentId) + " and assignmentId=" + topId + ";");
                            while (rsTopGrade.next()) {
                                double topGrade = rsTopGrade.getDouble("grade");
                                topAssignment.setOneGrade(student, topGrade);
                            }
                        }
                        /*---------------------------------------------------------------*/
                        //update sub-assignment
                        Statement stmtSubAssignment = c.createStatement();
                        ResultSet rsSubAssignment = stmtSubAssignment.executeQuery("SELECT * FROM Assignment" + " where parentId=" + topId + ";");
                        while (rsSubAssignment.next()) {
                            long subId = rsSubAssignment.getLong("assignmentId");
                            String subName = rsSubAssignment.getString("name");
                            double subPercentage = rsSubAssignment.getDouble("percentage");
                            double subTotal = rsSubAssignment.getDouble("total");
                            long subCriteriaId = rsSubAssignment.getLong("criteriaId");
                            String subDue = rsSubAssignment.getString("dueDate");
                            String subAssign = rsSubAssignment.getString("assignDate");
                            Date subDueDate = new Date(subDue.split("-", 0)[0], subDue.split("-", 0)[1], subDue.split("-", 0)[2]);
                            Date subAssignDate = new Date(subAssign.split("-", 0)[0], subAssign.split("-", 0)[1], subAssign.split("-", 0)[2]);
                            Criteria subCriteria = null;
                            rsCriteria = stmtCriteria.executeQuery("SELECT * FROM Criteria" + " where id=" + subCriteriaId + ";");
                            while (rsCriteria.next()) {
                                String cLabel = rsCriteria.getString("label");
                                double cPercentage = rsCriteria.getDouble("percentage");
                                subCriteria = new Criteria(course, cLabel, cPercentage);
                            }

                            Assignment subAssignment = new Assignment(subName, subCriteria, subPercentage, course, subDueDate, subAssignDate, subTotal, topAssignment);
                            //set grade of sub assignment
                            for (Student student : students) {
                                String studentId = student.getId().getId();
                                Statement stmtSubGrade = c.createStatement();
                                ResultSet rsSubGrade = stmtSubGrade.executeQuery("SELECT * FROM StudentAssignment" + " where studentId=" + sqlText(studentId) + " and assignmentId=" + subId + ";");
                                while (rsSubGrade.next()) {
                                    double subGrade = rsSubGrade.getDouble("grade");
                                    subAssignment.setOneGrade(student, subGrade);
                                }
                            }
                            topAssignment.setChildren(subAssignment);
                        }


                        assignments.add(topAssignment);
                    }
                }


                course.setStudents(students);
                course.setFinalGrade(finalGrade);
                course.setAssignments(assignments);
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("getActive() failed");
            System.exit(0);
        }
        return courses;
    }


    List<Course> getInactive() {
        List<Course> courses = new ArrayList<>();
        Connection c = null;
        Statement stmt = null;
        long courseId = 0;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:database.db");

            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Course" + " where status=" + sqlText(Config.INACTIVE_COURSE) + ";");
            while (rs.next()) {
                String courseName = rs.getString("name");
                courseId = rs.getLong("id");
                String semester = rs.getString("semester");
                String year = rs.getString("year");
                String status = Config.INACTIVE_COURSE;
                double curve = rs.getDouble("curve");

                //get criterias
                List<Criteria> criterias = new ArrayList<>();
                Statement stmtCriteria  = c.createStatement();
                ResultSet rsCriteria = stmtCriteria.executeQuery("SELECT * FROM Criteria" + " where courseId=" + courseId + ";");
                while (rsCriteria.next()) {
                    String cLabel = rsCriteria.getString("label");
                    double cPercentage = rsCriteria.getDouble("percentage");
                    Criteria criteria = new Criteria(cLabel, cPercentage);
                    criterias.add(criteria);
                }
                Course course = new Course(courseName, criterias, semester, status, year);
                for (Criteria criteria : criterias) {
                    criteria.setCourse(course);
                }
                courses.add(course);

                //get students
                List<Student> students = new ArrayList<>();
                HashMap<Student, Character> finalGrade = new HashMap<>();
                List<Assignment> assignments = new ArrayList<>();
                Statement stmtStudentCourse = c.createStatement();
                ResultSet rsStudentCourse = stmtStudentCourse.executeQuery("SELECT * FROM StudentToCourse" + " where courseId=" + courseId + ";");
                while (rsStudentCourse.next()) {
                    String studentId = rsStudentCourse.getString("studentId");
                    String studentStatus = rsStudentCourse.getString("status");
                    String comment = rsStudentCourse.getString("comments");
                    String studentFianlGrade = rsStudentCourse.getString("grade");
                    Statement stmtStudent = c.createStatement();
                    ResultSet rsStudent = stmtStudent.executeQuery("SELECT * FROM Student" + " where id=" + sqlText(studentId) + ";");
                    while (rsStudent.next()) {
                        String firstName = rsStudent.getString("firstName");
                        String lastName = rsStudent.getString("lastName");
                        String email = rsStudent.getString("email");
                        String kind = rsStudent.getString("kind");

                        Name studentName = new Name(firstName, lastName);
                        Student student = new Student(studentName, new Email(email), new Id(studentId), studentStatus, kind);
                        student.setComments(comment);
                        students.add(student);
                        finalGrade.put(student, studentFianlGrade == null ? ' ' : new Character(studentFianlGrade.charAt(0)));
                    }
                }

                //get the information of assignments. First get the top level assignments
                Statement stmtAssignment = c.createStatement();
                ResultSet rsAssignment = stmtAssignment.executeQuery("SELECT * FROM Assignment" + " where courseId=" + courseId + ";");
                while (rsAssignment.next()) {
                    long parentId = rsAssignment.getLong("parentId");
                    if (parentId != 0) {
                        //this is a sub-assignement
                        continue;
                    } else {
                        //this is a top-assignment
                        long topId = rsAssignment.getLong("assignmentId");
                        String topName = rsAssignment.getString("name");
                        double topPercentage = rsAssignment.getDouble("percentage");
                        double topTotal = rsAssignment.getDouble("total");
                        long topCriteriaId = rsAssignment.getLong("criteriaId");
                        String topDue = rsAssignment.getString("dueDate");
                        String topAssign = rsAssignment.getString("assignDate");
                        Date topDueDate = new Date(topDue.split("-", 0)[0], topDue.split("-", 0)[1], topDue.split("-", 0)[2]);
                        Date topAssignDate = new Date(topAssign.split("-", 0)[0], topAssign.split("-", 0)[1], topAssign.split("-", 0)[2]);
                        Criteria topCriteria = null;
                        rsCriteria = stmtCriteria.executeQuery("SELECT * FROM Criteria" + " where id=" + topCriteriaId + ";");
                        while (rsCriteria.next()) {
                            String cLabel = rsCriteria.getString("label");
                            double cPercentage = rsCriteria.getDouble("percentage");
                            topCriteria = new Criteria(course, cLabel, cPercentage);
                        }

                        Assignment topAssignment = new Assignment(topName, topCriteria, topPercentage, course, topDueDate, topAssignDate, topTotal);
                        //set grade of top assignment
                        for (Student student : students) {
                            String studentId = student.getId().getId();
                            Statement stmtTopGrade = c.createStatement();
                            ResultSet rsTopGrade = stmtTopGrade.executeQuery("SELECT * FROM StudentAssignment" + " where studentId=" + sqlText(studentId) + " and assignmentId=" + topId + ";");
                            while (rsTopGrade.next()) {
                                double topGrade = rsTopGrade.getDouble("grade");
                                topAssignment.setOneGrade(student, topGrade);
                            }
                        }
                        /*---------------------------------------------------------------*/
                        //update sub-assignment
                        Statement stmtSubAssignment = c.createStatement();
                        ResultSet rsSubAssignment = stmtSubAssignment.executeQuery("SELECT * FROM Assignment" + " where parentId=" + topId + ";");
                        while (rsSubAssignment.next()) {
                            long subId = rsSubAssignment.getLong("assignmentId");
                            String subName = rsSubAssignment.getString("name");
                            double subPercentage = rsSubAssignment.getDouble("percentage");
                            double subTotal = rsSubAssignment.getDouble("total");
                            long subCriteriaId = rsSubAssignment.getLong("criteriaId");
                            String subDue = rsSubAssignment.getString("dueDate");
                            String subAssign = rsSubAssignment.getString("assignDate");
                            Date subDueDate = new Date(subDue.split("-", 0)[0], subDue.split("-", 0)[1], subDue.split("-", 0)[2]);
                            Date subAssignDate = new Date(subAssign.split("-", 0)[0], subAssign.split("-", 0)[1], subAssign.split("-", 0)[2]);
                            Criteria subCriteria = null;
                            rsCriteria = stmtCriteria.executeQuery("SELECT * FROM Criteria" + " where id=" + subCriteriaId + ";");
                            while (rsCriteria.next()) {
                                String cLabel = rsCriteria.getString("label");
                                double cPercentage = rsCriteria.getDouble("percentage");
                                subCriteria = new Criteria(course, cLabel, cPercentage);
                            }

                            Assignment subAssignment = new Assignment(subName, subCriteria, subPercentage, course, subDueDate, subAssignDate, subTotal, topAssignment);
                            //set grade of sub assignment
                            for (Student student : students) {
                                String studentId = student.getId().getId();
                                Statement stmtSubGrade = c.createStatement();
                                ResultSet rsSubGrade = stmtSubGrade.executeQuery("SELECT * FROM StudentAssignment" + " where studentId=" + sqlText(studentId) + " and assignmentId=" + subId + ";");
                                while (rsSubGrade.next()) {
                                    double subGrade = rsSubGrade.getDouble("grade");
                                    subAssignment.setOneGrade(student, subGrade);
                                }
                            }
                            topAssignment.setChildren(subAssignment);
                        }


                        assignments.add(topAssignment);
                    }
                }


                course.setStudents(students);
                course.setFinalGrade(finalGrade);
                course.setAssignments(assignments);
            }
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println("getInactive() failed");
            System.exit(0);
        }
        return courses;
    }

}
