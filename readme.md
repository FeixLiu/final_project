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

### A general work flow (the UI can be seen from doc design)
    1.	Login
    2.	Create a course
    3.	Choose the course
    4.	Add one criteria
    5.	Delete one criteria
    6.	Save as template
    7.	Load students from CSV file
    8.	Edit the information of a student
    9.	Sort by name
    10.	Sort by id
    11.	Sort by email
    12.	Delete a student
    13.	Assign two single part assignments
    14.	Assign two multi parts assignments
    15.	Click by name to grading page
    16.	Give grade to four assignments
    17.	Give bonus for one student
    18.	Give comments
    19.	See the comments
    20.	See all grades filter by criteria
    21.	Sort by name
    22.	See all grades filter by criteria and assignments
    23.	Sort by grade
    24.	Add a new student
    25.	Click student’s name to the score distribution of that student
    26.	Show the comment of ‘lately enrolled’
    27.	Go to statistical page
    28.	Filter by undergraduate
    29.	Filter by graduate
    30.	Unselect some student
    31.	Go to archive page
    32.	Click students with highlight to show comment
    33.	Give letter grade
    34.	Click save
    35.	Click archive
    36.	Save the template
    37.	Close the system and reopen
    38.	Go to inactive course
    39.	Choose the course and see the final grade
    40.	Go back to main menu
    41.	Create a course based on the template




