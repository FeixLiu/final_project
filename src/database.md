#System Entrance
##getActive()
#####Using scene:
    at the begin of the program, load all the information into the memeory
    calling the constructor with parameters of gradingSystem class
#####Parameters:
    None
#####Return:
    List<Course> the list of active course
##getInactive()
#####Using scene:
    at the begin of the program, load all the information into the memeory
    calling the constructor with parameters of gradingSystem class
#####Parameters:
    None
#####Return:
    List<Course> the list of inactive course
##getTemplate()
#####Using scene:
    at the begin of the program, load all the information into the memeory
    calling the constructor with parameters of gradingSystem class
#####Parameters:
    None
#####Return:
    List<Template> the list of tempaltes


#Course Relating
##archiveCourse(String name, String year, String semester)
#####Using scene:
    archive the course
#####Parameters:
    name: the name of the course<br>
    year: the year of the course<br>
    semester: the semester of the course<br>
    
#addCourse(String name, List<Criteria> criteria, String semester, String year)
#####Using scene:
    create a course
#####Parameters:
    name: the name of the course<br>
    criteria: the criteira of the course<br>
    semester: the semester of the course<br>
    year: the year of the course<br>

#updateCriteria(List<Criteria> criteria, Course course)
#####Using scene:
    update the criteria of the course
#####Parameters:
    criteria: the criteira of the course
    course: the instance of current Course

#saveAsTemplate(String name, Course course)
#####Using scene:
    save the criteria of the course as a template
#####Parameters:
    name: the name of the template
    course: the instance of current Course
    
#setCurve(double curve, Course course)
#####Using scene:
    set the curve of the course
#####Parameters:
    curve: the curve of the course
    course: the instance of current Course


#Student Relating
##addOneStudent(Name name, String id, String email, String kind, Course course)
#####Using scene:
    add a student<br>
    when adding a student, set his final grade to 'N' and set all current assignment's grade to 0
#####Parameters:
    name: the Name instance of the student<br>
    id: the id of the course<br>
    email: the Email of the course<br>
    kind: the kind of the student (reference the Config class)
    course: the instance of current Course
    
##modifyStudentStatus(String id, String status, Course course)
#####Using scene:
    modify the status of the student
#####Parameters:
    id: the id of the course<br>
    status: the status of the student (reference the Config class)
    course: the instance of current Course
    
#giveComment(HashMap<String, String> comments, Course course)
#####Using scene:
    update the comment for student
#####Parameters:
    comments: the comments for all students<br>
    course: the instance of current Course
    
#deleteStudent(String id, Course course)
#####Using scene:
    delete a student from the course
#####Parameters:
    id: the id of the student<br>
    course: the instance of current Course


#Assignment Relating
##updateAssignment(List<Assignment> assignments, Course course)
#####Using scene:
    user add new assignment or modify attributes of assignments<br>
    user give grade to this assignment or update the grade of the assignment<br>
    cause the modification relating with percentage will lead some calcualtion, this call will pass all the information of the assignments<br>
    mind the sub assignment for each assignment<br>
#####Parameters:
    assignments: all infomation relating with the course<br>
    course: the instance of current Course


#Grade Relating
##giveFinalGrade(HashMap<String, Character> finalGrade, Course course)
#####Using scene:
    give final grade to student
#####Parameters:
    finalGrade: the final grade<br>
    course: the instance of current Course