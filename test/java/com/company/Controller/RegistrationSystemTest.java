package com.company.Controller;

import com.company.Exceptions.InputException;
import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;
import com.company.Repository.CourseRepository;
import com.company.Repository.StudentRepository;
import com.company.Repository.TeacherRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationSystemTest {
    private Teacher teacher1;
    private Teacher teacher2;
    private Teacher teacher3;

    private Course course1;
    private Course course2;
    private Course course3;
    private Course course4;

    private Student student1;
    private Student student2;
    private Student student3;
    private Student student4;
    private Student student5;
    private Student student6;

    private RegistrationSystem regSystem;

    @BeforeEach
    void createInstances() {

        /* creating Teacher instances */
        teacher1 = new Teacher(1, "Catalin", "Rusu");
        teacher2 = new Teacher(2, "Diana", "Cristea");
        teacher3 = new Teacher(3, "Cristian", "Sacarea");

        /* creating Course instances */
        course1 = new Course(1, "OOP", teacher1, 20, 6);
        course2 = new Course(2, "SDA", teacher2, 30, 5);
        course3 = new Course(3, "NewOptional", teacher2, 3, 20);
        course4 = new Course(4, "MAP", teacher1, 20, 5);

        /* adding courses to each teacher*/
        List<Course> coursesTeacher1=new ArrayList<Course>();
        coursesTeacher1.add(course1);
        teacher1.setCourses(coursesTeacher1);

        List<Course> coursesTeacher2=new ArrayList<Course>();
        coursesTeacher2.add(course2);
        coursesTeacher2.add(course3);
        teacher2.setCourses(coursesTeacher2);

        /* creating a teacher Repo */
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher1);
        teachers.add(teacher2);
        TeacherRepository teacher_repo = new TeacherRepository(teachers);

        /* creating a course Repo */
        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        courses.add(course3);
        CourseRepository course_repo = new CourseRepository(courses);

        /* creating Student instances */
        student1 = new Student(1, "Denisa", "Dragota");
        student2 = new Student(2, "Mihnea", "Aleman");
        student3 = new Student(3, "Raul", "Barbat");
        student4 = new Student(4, "Evelin", "Bohm");
        student5 = new Student(5, "Maria", "Morar");
        student6 = new Student(6, "Iarina", "Demian");

        /* creating a Student Repo */
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        students.add(student3);
        students.add(student4);
        students.add(student5);

        StudentRepository student_repo = new StudentRepository(students);

        /* creating a RegistrationSystem instance */
        regSystem = new RegistrationSystem(student_repo, teacher_repo, course_repo);
    }

    @Test
    void register() {
        /* 1. register a student to a non-existing course in the Course Repo */
        try {
            regSystem.register(course4, student1);
        } catch (InputException e) {
            System.out.println("1. Exception raised: " + e.getMessage());
        }

        /* 2. register a non-existing student in the Student Repo to a Course */
        try {
            regSystem.register(course1, student6);
        } catch (InputException e) {
            System.out.println("2. Exception raised: " + e.getMessage());
        }

        /* 3. register 3 students to a course */

        try {
            regSystem.register(course3, student1);
            regSystem.register(course3, student2);
            regSystem.register(course3, student3);
            regSystem.register(course1, student1);
            /* assert the updates of the instances of the course and student repo */
            assertEquals(3, regSystem.findOneCourse(course3.getCourseId()).getStudentsEnrolled().size());
            assertEquals(1, regSystem.findOneStudent(student2.getStudentId()).getEnrolledCourses().size());
            assertEquals(20, regSystem.findOneStudent(student2.getStudentId()).getTotalCredits());
            assertEquals(26, regSystem.findOneStudent(student1.getStudentId()).getTotalCredits());

        } catch (InputException e) {
            System.out.println("3. Exception raised: " + e.getMessage());
        }

        /* 4. trying to enroll a student to a course with no free places */
        /* course3 has 3 total places and 3 students have been already enrolled */
        try {
            regSystem.register(course3, student4);
        } catch (InputException e) {
            System.out.println("4. Exception raised: " + e.getMessage());
        }

        /* trying to enroll a student to a course that exceeds his credit limit (30) */
        /* student1 is already enrolled to 2 courses with 20 + 6 credits */
        try {
            regSystem.register(course2, student1);
        } catch (InputException e) {
            System.out.println("5. Exception raised: " + e.getMessage());
        }

        /* trying to enroll a already enrolled student to the same course again */
        try {
            regSystem.register(course1, student1);
        } catch (InputException e) {
            System.out.println("6. Exception raised: " + e.getMessage());
        }
    }

    @Test
    void retrieveCoursesWithFreePlaces() {
        /*enroll students to courses */
        /* course3 will have no places free */
        try {
            regSystem.register(course3, student1);
            regSystem.register(course3, student2);
            regSystem.register(course3, student3);
            regSystem.register(course1, student1);
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }

        /*creating the expected result list */
        Course[] freeplacesCourses = new Course[2];
        freeplacesCourses[0] = course2;
        freeplacesCourses[1] = course1;

        assertArrayEquals(freeplacesCourses, regSystem.retrieveCoursesWithFreePlaces().toArray());
    }

    @Test
    void retrieveStudentsEnrolledForACourse() {
        /* register 3 students to course3 */
        try {
            regSystem.register(course3, student1);
            regSystem.register(course3, student2);
            regSystem.register(course3, student3);
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }

        /*creating the expected result list */
        Student[] studentsEnrolled = new Student[3];
        studentsEnrolled[0] = student1;
        studentsEnrolled[1] = student2;
        studentsEnrolled[2] = student3;

        assertArrayEquals(studentsEnrolled, regSystem.retrieveStudentsEnrolledForACourse(course3).toArray());

        /* null for a non-existing course in the Course Repo */
        assertNull(regSystem.retrieveStudentsEnrolledForACourse(course4));

        /* empty list for a course with no students enrolled */
        assertEquals(new ArrayList<Student>(), regSystem.retrieveStudentsEnrolledForACourse(course2));
    }

    @Test
    void getAllCourses() {
        /*creating the expected result list */
        Course[] courses = new Course[3];
        courses[0] = course1;
        courses[1] = course2;
        courses[2] = course3;

        assertArrayEquals(courses, regSystem.getAllCourses().toArray());
    }

    @Test
    void modifyCredits() {
        /* enrolling a student to a course */
        try{
            regSystem.register(course1,student1);
        }catch(InputException e){
            System.out.println(e.getMessage());
        }

        assertEquals(6, student1.getTotalCredits());

        /* modifying the credits of a course */

        course1.setCredits(course1.getCredits()+2);

        /* update in the course repo and students credits */
        regSystem.modifyCredits(course1);

        /* assert update of students credits */
        assertEquals(8, student1.getTotalCredits());

    }

    @Test
    void deleteCourseFromTeacher() {
        /* enroll students to a course*/
        try {
            regSystem.register(course3, student1);
            regSystem.register(course3, student2);
            regSystem.register(course3, student3);
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }

        try {
            //number of courses of the teacher before deleting
            int coursesBefore=regSystem.findOneTeacher(((Teacher)course3.getTeacher()).getTeacherId()).getCourses().size();
            assertEquals(2,coursesBefore);

            //number credits of a student enrolled before deleting
            assertEquals(20,regSystem.findOneStudent(student1.getStudentId()).getTotalCredits());

            //delete Course
            regSystem.deleteCourseFromTeacher(teacher2, course3);

            //number of courses of the teacher before deleting
            int coursesAfter=regSystem.findOneTeacher(((Teacher)course3.getTeacher()).getTeacherId()).getCourses().size();

            //assert update of number courses
            assertEquals(coursesBefore-1,coursesAfter);

            //asserting the update of credits of the student after deleting the course
            assertEquals(0,regSystem.findOneStudent(student1.getStudentId()).getTotalCredits());
        } catch (InputException e) {
            System.out.println(e.getMessage());
        }
    }
}