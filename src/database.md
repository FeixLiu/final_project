#System Entrance
##getActive()
#####Using scene:
    at the begin of the program, load all the information into the memeory
    calling the constructor with parameters of gradingSystem class
#####Parameters:
    None
#####Return:
    List<LOGIC.Course> the list of active course
##getInactive()
#####Using scene:
    at the begin of the program, load all the information into the memeory
    calling the constructor with parameters of gradingSystem class
#####Parameters:
    None
#####Return:
    List<LOGIC.Course> the list of inactive course
##getTemplate()
#####Using scene:
    at the begin of the program, load all the information into the memeory
    calling the constructor with parameters of gradingSystem class
#####Parameters:
    None
#####Return:
    List<LOGIC.Template> the list of tempaltes


#LOGIC.Course Relating
##archiveCourse(String name, String year, String semester)
#####Using scene:
    archive the course
#####Parameters:
    name: the name of the course<br>
    year: the year of the course<br>
    semester: the semester of the course<br>
    
#addCourse(String name, List<LOGIC.Criteria> criteria, String semester, String year)
#####Using scene:
    create a course
#####Parameters:
    name: the name of the course<br>
    criteria: the criteira of the course<br>
    semester: the semester of the course<br>
    year: the year of the course<br>

#updateCriteria(List<LOGIC.Criteria> criteria, LOGIC.Course course)
#####Using scene:
    update the criteria of the course
#####Parameters:
    criteria: the criteira of the course
    course: the instance of current LOGIC.Course

#saveAsTemplate(String name, LOGIC.Course course)
#####Using scene:
    save the criteria of the course as a template
#####Parameters:
    name: the name of the template
    course: the instance of current LOGIC.Course
    
#setCurve(double curve, LOGIC.Course course)
#####Using scene:
    set the curve of the course
#####Parameters:
    curve: the curve of the course
    course: the instance of current LOGIC.Course


#LOGIC.Student Relating
##addOneStudent(LOGIC.Name name, String id, String email, String kind, LOGIC.Course course)
#####Using scene:
    add a student<br>
    when adding a student, set his final grade to 'N' and set all current assignment's grade to 0
#####Parameters:
    name: the LOGIC.Name instance of the student<br>
    id: the id of the course<br>
    email: the LOGIC.Email of the course<br>
    kind: the kind of the student (reference the LOGIC.Config class)
    course: the instance of current LOGIC.Course
    
##modifyStudentStatus(String id, String status, LOGIC.Course course)
#####Using scene:
    modify the status of the student
#####Parameters:
    id: the id of the course<br>
    status: the status of the student (reference the LOGIC.Config class)
    course: the instance of current LOGIC.Course
    
#giveComment(HashMap<String, String> comments, LOGIC.Course course)
#####Using scene:
    update the comment for student
#####Parameters:
    comments: the comments for all students<br>
    course: the instance of current LOGIC.Course
    
#deleteStudent(String id, LOGIC.Course course)
#####Using scene:
    delete a student from the course
#####Parameters:
    id: the id of the student<br>
    course: the instance of current LOGIC.Course


#LOGIC.Assignment Relating
##updateAssignment(List<LOGIC.Assignment> assignments, LOGIC.Course course)
#####Using scene:
    user add new assignment or modify attributes of assignments<br>
    user give grade to this assignment or update the grade of the assignment<br>
    cause the modification relating with percentage will lead some calcualtion, this call will pass all the information of the assignments<br>
    mind the sub assignment for each assignment<br>
#####Parameters:
    assignments: all infomation relating with the course<br>
    course: the instance of current LOGIC.Course


#Grade Relating
##giveFinalGrade(HashMap<String, Character> finalGrade, LOGIC.Course course)
#####Using scene:
    give final grade to student
#####Parameters:
    finalGrade: the final grade<br>
    course: the instance of current LOGIC.Course