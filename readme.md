Grading System Group 19
==========
### Coworkers and BUID:
    Name: Yinyun  Cao     BUID:U36509179
    Name: Qichao  Hong    BUID:U83816176
	Name: Yuang   Liu     BUID:U99473611
	Name: Zhitong Wu      BUID:U93211428
	
### How to run:
The entrance of the program is /CONTROLLER/Entrance.java. You can run this program by

	cd CONTROLLER
	javac Entrance.java
	java Entrance
	
### Database
    We use sqlite database.
    You do not need to create the datebase manually, the system will create it automatically.
    The JDBC is also provided. 

### User Interface
    We use fxml user interface.
    If you use Oracle Java 8.0 the supporting files are already included.
    Or you can add the support file, which are in 'lib' folder, into your IDE.

### How to calculate the overall score:
    In this system, we do not deem your input as the percentage but weight instead.
    For instance, if there are 4 criteria for one course:
        Exam            60
        Homework        50
        Participation   30
        Bonus           0
    We allow this kind of distribution, the final score will be calculated by this:
        score(Exam)           *   60  /  140 + 
        score(Homework)       *   50  /  140 + 
        score(Participation)  *   30  /  140 + 
        score(Bonus)          *   0   /  140
    Where 140 = 60 + 50 + 30 + 0
    The same to calcualte the overall score for each criteira and multi-parts assignment
    
### Some configurations: 
    The password for professor is 123456
    The students' information file must be CSV file
    The students' file must orgnized in Last Name, First Name, email, id, kind
    The student has two kinds: graduate or undergraduate
    The student has three status: enrolled, withdraw or drop
    The course has two status: active or inactive
    The template is some kind of pointer to the course, 
        which means when modify the criteria of that course, the template will be changed
    For each course, a bouns with 0 weight will be automatically added

### Features we provide
    1.      Add course with(out) a template
    2.      Add students (not) from a file
    3.      Add single / multi part(s) assignment
    4.      Highlight the row when give grade
    5.      Automatically give "lately enrolled" comments for some students
    6.      Highlight students who have comments
    7.      Click the highlight students' name will show the comment
    8.      Click the name in archive page will bring you to the detail score distribution
    9.      Click the name in statistical page will bring you to the detail score distribution
    10.      Click the assignment name will bring you to the grading page
    11.     Sorting the infomation by many criteria
    12.     Can give percentage grade or lose credits grade in one page
    13.     Flexibel see the statistical number for students




