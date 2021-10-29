package com.company.Controller;

import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;
import com.company.Repository.CourseRepository;
import com.company.Repository.StudentRepository;
import com.company.Repository.TeacherRepository;

import java.util.ArrayList;
import java.util.List;

public class RegistrationSystem {
    private StudentRepository studentsRepo;
    private TeacherRepository teachersRepo;
    private CourseRepository coursesRepo;

    public RegistrationSystem(StudentRepository studentsRepo, TeacherRepository teachersRepo, CourseRepository coursesRepo) {
        this.studentsRepo = studentsRepo;
        this.teachersRepo = teachersRepo;
        this.coursesRepo = coursesRepo;
    }

    public boolean register(Course course, Student student){

        List<Student> courseStudents =  course.getStudentsEnrolled();
        //check if course exists
        if(coursesRepo.findOne(course.getCourseId())==null)
        {
            return false;
        }
        //check if student exists
        if(studentsRepo.findOne(student.getStudentId())==null)
        {
            return false;
        }

        //check if course has free places
        if (courseStudents.size() == course.getMaxEnrollment()) {
            return false;
        }

        //check if student is already enrolled
        for(Student s: courseStudents) {
            if (s.compareTo(student))
                return false;
        }

        //if student has over 30 credits after enrolling to this course
        int studCredits=student.getTotalCredits()+course.getCredits();
        if(studCredits > 30)
            return false;

        //add student to course
        //update courses repo
        courseStudents.add(student);
        course.setStudentsEnrolled(courseStudents);
        coursesRepo.update(course);

        //update total credits of student
        student.setTotalCredits(studCredits);

        //update enrolled courses of Student
        List<Course> studCourses = student.getEnrolledCourses();
        studCourses.add(course);
        student.setEnrolledCourses(studCourses);

        //update students Repo
        studentsRepo.update(student);

        return true;
    }
    public List<Course> retrieveCoursesWithFreePlaces(){
        List<Course> freePlaces = new ArrayList<>();

        for(Course c:coursesRepo.findAll()){
            if(c.getStudentsEnrolled().size()<c.getMaxEnrollment())
                freePlaces.add(c);
        }
        return freePlaces;
    }

    public List<Student> retrieveStudentsEnrolledForACourse(Course course) {
        if (coursesRepo.findOne(course.getCourseId()) != null) {
            return course.getStudentsEnrolled();
        }
        return null;
    }

    public List<Course> getAllCourses(){
        ArrayList<Course> allCourses= new ArrayList<>();
        for (Course c: this.coursesRepo.findAll()){
            allCourses.add(c);
        }
        return allCourses;
    }

    public void updateStudentsCredits() {
        for (Student s : this.studentsRepo.findAll()) {
            //System.out.println(s.getEnrolledCourses());
            List<Course> coursesEnrolled = s.getEnrolledCourses();
            int sum = 0;
            for (Course c : coursesEnrolled) {
                sum += c.getCredits();
            }

            s.setTotalCredits(sum);
            studentsRepo.update(s);
        }
    }
    //deleted course from CourseRepo, from the Students and from the teacher
    public boolean deleteCourseFromTeacher(Teacher teacher, Course course) {

        //check if course exists
        if(coursesRepo.findOne(course.getCourseId())==null)
        {
            return false;
        }
        //check if teacher exists
        if(teachersRepo.findOne(teacher.getTeacherId())==null)
        {
            return false;
        }

        //check if course actually is in the teacher's list of courses
        //delete course from teacher's list
        boolean found=false;
        List<Course> courseList = teacher.getCourses();
        for (Course c : courseList) {
            if (c.compareTo(course)) {
                found=true;
                //update teacher repo
                courseList.remove(c);
                teacher.setCourses(courseList);
                teachersRepo.update(teacher);
            }
        }
        if(!found)
            return false;

        //delete course from Course Repo
        coursesRepo.delete(course.getCourseId());

        //delete course from all students enrolled
        for(Student s:studentsRepo.findAll()) {
            List<Course> coursesEnrolled = s.getEnrolledCourses();
            for (Course c : coursesEnrolled) {
                if (c.compareTo(course)) { //student enrolled to the course
                    coursesEnrolled.remove(course);
                }
            }
            //update student with the new courses enrolled list
            s.setEnrolledCourses(coursesEnrolled);
            this.studentsRepo.update(s);
        }

        //the total credits of some students got updated
        this.updateStudentsCredits();

        return true;
    }

    public Student findOneStudent(Long id){
        return this.studentsRepo.findOne(id);
    }

    public Course findOneCourse(Long id){
        return this.coursesRepo.findOne(id);
    }

    public Teacher findOneTeacher(Long id){
        return this.teachersRepo.findOne(id);
    }


}
