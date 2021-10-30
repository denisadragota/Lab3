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

public class RegistrationSystem {
    private StudentRepository studentsRepo;
    private TeacherRepository teachersRepo;
    private CourseRepository coursesRepo;

    public RegistrationSystem(StudentRepository studentsRepo, TeacherRepository teachersRepo, CourseRepository coursesRepo) {
        this.studentsRepo = studentsRepo;
        this.teachersRepo = teachersRepo;
        this.coursesRepo = coursesRepo;
    }

    public boolean register(Course course, Student student) throws InputException {

        List<Student> courseStudents =  course.getStudentsEnrolled();
        //check if course exists
        try {
            if (coursesRepo.findOne(course.getCourseId()) == null) {
                throw new InputException("Non-existing course id!");
            }
        } catch(NullException e){
            System.out.println(e.getMessage());
        }
        //check if student exists
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
    public List<Course> retrieveCoursesWithFreePlaces(){
        List<Course> freePlaces = new ArrayList<>();

        for (Course c:coursesRepo.findAll()){

            if(c.getStudentsEnrolled().size()<c.getMaxEnrollment())
                freePlaces.add(c);
        }
        return freePlaces;
    }

    public List<Student> retrieveStudentsEnrolledForACourse(Course course) {
        try {
            if (coursesRepo.findOne(course.getCourseId()) != null) {
                return course.getStudentsEnrolled();
            }
        }catch(NullException e){
            System.out.println(e.getMessage());
        }
        return null;
    }


    //deleted course from CourseRepo, from the Students and from the teacher
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

    public void updateStudentsCredits() {
        List<Student> stud = this.getAllStudents();
        //looping through all students of the repo
        for (Student s : stud ) {

            List<Course> coursesEnrolled = s.getEnrolledCourses();
            int sum = 0;
            //looping through all enrolled courses of each student
            for (Course c : coursesEnrolled) {
                //calculate the total sum of credits
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


    public List<Student> getAllStudents(){
        ArrayList<Student> allStudents= new ArrayList<>();
        for (Student s: this.studentsRepo.findAll()){
            allStudents.add(s);
        }
        return allStudents;
    }

    public List<Course> getAllCourses(){
        ArrayList<Course> allCourses= new ArrayList<>();
        for (Course c: this.coursesRepo.findAll()){
            allCourses.add(c);
        }
        return allCourses;
    }

    public List<Teacher> getAllTeachers(){
        ArrayList<Teacher> allTeachers= new ArrayList<>();
        for (Teacher t: this.teachersRepo.findAll()){
            allTeachers.add(t);
        }
        return allTeachers;
    }
    public Student findOneStudent(long id){
        try {
            return this.studentsRepo.findOne(id);
        }catch (NullException e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    public Course findOneCourse(long id){
        try {
            return this.coursesRepo.findOne(id);
        }catch(NullException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Teacher findOneTeacher(long id){
        try{
            return this.teachersRepo.findOne(id);
        }catch(NullException e){
            System.out.println(e.getMessage());
            return null;
       }

    }


}
