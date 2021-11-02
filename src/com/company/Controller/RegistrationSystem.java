package com.company.Controller;

import com.company.Exceptions.InputException;
import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;
import com.company.Repository.CourseRepository;
import com.company.Repository.StudentRepository;
import com.company.Repository.TeacherRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * RegistrationSystem class
 * storing repo lists of: students, teachers, courses
 *
 * Functionalities: enrolling a student to a course, showing courses with free places,
 * showing all courses, showing all students enrolled to a course, deleting a course from a teacher,
 * updating students' total credits number after updating a course credits
 *
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
public class RegistrationSystem {
    private StudentRepository studentsRepo;
    private TeacherRepository teachersRepo;
    private CourseRepository coursesRepo;

    public RegistrationSystem(StudentRepository studentsRepo, TeacherRepository teachersRepo, CourseRepository coursesRepo) {
        this.studentsRepo = studentsRepo;
        this.teachersRepo = teachersRepo;
        this.coursesRepo = coursesRepo;
    }

    /**
     * desc: enroll a student to a course 
     * @param course , Course object
     * @param student, Student object
     * @return true if successfully enroled, else false
     * @throws InputException if course or student params not existing in repo lists
     * or if student can not enroll to that given course under the following conditions
     * Conditions: student can have maximal 30 credits and a course has a maximum number of enrolled students,
     * student can not be enrolled multiple times to the same course
     */
    public boolean register(Course course, Student student) throws InputException {

        List<Student> courseStudents =  course.getStudentsEnrolled();
        //check if course exists in repo
        try {
            if (coursesRepo.findOne(course.getCourseId()) == null) {
                throw new InputException("Non-existing course id!");
            }
        } catch(NullException e){
            System.out.println(e.getMessage());
        }
        //check if student exists in repo
        try{
            if(studentsRepo.findOne(student.getStudentId())==null)
            {
                throw new InputException("Non-existing student id!");
            }

        }catch(NullException e){
            System.out.println(e.getMessage());
        }

        //check if course has free places
        if (courseStudents.size() == course.getMaxEnrollment()) {
            throw new InputException("Course has no free places!");
        }

        //check if student is already enrolled
        for(Student s: courseStudents) {
            if (s.compareTo(student))
                throw new InputException("Student already enrolled!");
        }

        //if student has over 30 credits after enrolling to this course
        int studCredits=student.getTotalCredits()+course.getCredits();
        if(studCredits > 30)
            throw new InputException("Total number of credits exceeded!");;

        //add student to course
        //update courses repo
        courseStudents.add(student);
        course.setStudentsEnrolled(courseStudents);
        try {
            coursesRepo.update(course);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }

        //update total credits of student
        student.setTotalCredits(studCredits);

        //update enrolled courses of Student
        List<Course> studCourses = student.getEnrolledCourses();
        studCourses.add(course);
        student.setEnrolledCourses(studCourses);

        //update students Repo
        try{
        studentsRepo.update(student);}
        catch(NullException e){
            System.out.println(e.getMessage());
        }

        return true;
    }

    /**
     * desc: find courses from the course repo where number of enrolled students < maximum enroll limit
     * @return courses with free places
     * 
     */
    public List<Course> retrieveCoursesWithFreePlaces(){
        List<Course> freePlaces = new ArrayList<>();
        
        for (Course c:coursesRepo.findAll()){
            if(c.getStudentsEnrolled().size()<c.getMaxEnrollment())
                freePlaces.add(c);
        }
        return freePlaces;
    }

    /**
     * desc: retrieve all students enrolled to a course
     * @param course Course object
     * @return list of students enrolled to the given course, or null if course is NULL
     */
    public List<Student> retrieveStudentsEnrolledForACourse(Course course) {
        /* find course in the course repo */
        try {
            if (coursesRepo.findOne(course.getCourseId()) != null) {
                return course.getStudentsEnrolled();
            }
        }catch(NullException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    /**
     * desc: Delete a course from a teacher. Removing course from the teacher's courses list, from the students' enrolled lists and from the course repo
     * Update number of credits of certain students
     *
     * @param teacher  Teacher object from whom we delete a course
     * @param course  Course object, from the teacher's list, to be deleted
     * @return true if successfully deleted
     * @throws InputException if teacher or course do not exist in te repo lists,
     * or if the course does not correspond to that teacher
     * deleting course from the teacher's teaching list, from the students enrolled list and from the courses repo
     *
     */

    public boolean deleteCourseFromTeacher(Teacher teacher, Course course) throws InputException{

        //check if course exists
        try {
            if (coursesRepo.findOne(course.getCourseId()) == null) {
                throw new InputException("Non-existing course id!");
            }
        }catch (NullException e){
            System.out.println(e.getMessage());
        }
        //check if teacher exists
        try {
            if (teachersRepo.findOne(teacher.getTeacherId()) == null) {
                throw new InputException("Non-existing teacher id!");
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        //check if course actually is in the teacher's list of courses
        List<Course> courseList = teacher.getCourses();
        Optional<Course> c = courseList.stream().filter(el-> el.compareTo(course)).findFirst();

        // course not found in teacher courses list
        if(c.isEmpty())
            throw new InputException("Course id not corresponding to teacher id!");
        else
        {   //delete course from teacher's list
            List<Student> studentsEnrolled= course.getStudentsEnrolled(); //store students enrolled
            courseList.remove(c.get());
            teacher.setCourses(courseList);
            try {
                teachersRepo.update(teacher);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }

            //delete course from Course Repo
            try {
                coursesRepo.delete(course.getCourseId());
            }catch(NullException e){
                System.out.println(e.getMessage());
            }

            //delete course from all students enrolled
            for(Student s:studentsEnrolled) {

                List<Course> coursesEnrolled= s.getEnrolledCourses();
                coursesEnrolled.remove(course);
                
                //update student with the new courses enrolled list and the new credits
                s.setEnrolledCourses(coursesEnrolled);
                s.setTotalCredits(s.getTotalCredits()-course.getCredits());

                //update in the Repo
                try {
                    this.studentsRepo.update(s);
                }catch(NullException e){
                    System.out.println(e.getMessage());
                }
                }
            return true;
        }
    }

    /**
     * desc: Recalculate the sum of credits provided from the enrolled courses of the students
     * Update the credits sum for each student
     */
    public void updateStudentsCredits() {
        List<Student> stud = this.getAllStudents();
        
        for (Student s : stud ) {
            List<Course> coursesEnrolled = s.getEnrolledCourses();
            int sum = 0;
          
            for (Course c : coursesEnrolled) {
                //calculate the total sum of credits for each student
                sum += c.getCredits();
            }

            //update the total sum of credits for the student
            s.setTotalCredits(sum);

            //update in the repo
            try {
                studentsRepo.update(s);
            }catch(NullException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * desc: modifying credit number for a course, that leads to updating repo with the updated course and updating students' credits 
     * @param c Course object, which credits were updated
     */
    public void modifyCredits(Course c){
        /* update course in the repo */
        try {
            this.coursesRepo.update(c);
        }catch(NullException e){
            System.out.println(e.getMessage());
        }

        /*update all students*/
        this.updateStudentsCredits();
    }


    /**
     * desc: get all students from the repo
     * @return student list from the student repo
     */
    public List<Student> getAllStudents(){
        ArrayList<Student> allStudents= new ArrayList<>();
        for (Student s: this.studentsRepo.findAll()){
            allStudents.add(s);
        }
        return allStudents;
    }

    /**
     * desc: get all courses from the repo
     * @return courses list from the course repo
     */
    public List<Course> getAllCourses(){
        ArrayList<Course> allCourses= new ArrayList<>();
        for (Course c: this.coursesRepo.findAll()){
            allCourses.add(c);
        }
        return allCourses;
    }

    /**
     * get all teachers from the repo
     * @return teachers list from teh teacher repo
     */
    public List<Teacher> getAllTeachers(){
        ArrayList<Teacher> allTeachers= new ArrayList<>();
        for (Teacher t: this.teachersRepo.findAll()){
            allTeachers.add(t);
        }
        return allTeachers;
    }


    /**
     * searching for a student in the repo by the id
     * @param id of a Student object
     * @return Student object from the student repo list with the given id
     */
    public Student findOneStudent(long id){
        try {
            return this.studentsRepo.findOne(id);
        }catch (NullException e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    /**
     * desc: searching for a course in the repo by the id
     * @param id of a Course object
     * @return Course object from the course repo list with the given id
     */
    public Course findOneCourse(long id){
        try {
            return this.coursesRepo.findOne(id);
        }catch(NullException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * desc: searching for a teacher in the repo by the id
     * @param id of a Teacher object
     * @return Teacher object from the teacher repo list with the given id
     */
    public Teacher findOneTeacher(long id){
        try{
            return this.teachersRepo.findOne(id);
        }catch(NullException e){
            System.out.println(e.getMessage());
            return null;
       }

    }


}
