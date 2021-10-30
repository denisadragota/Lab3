package com.company.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CourseTest class
 * testing Course class
 *
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
class CourseTest {
    private Teacher teacher1;
    private Teacher teacher2;

    private Course course1;
    private Course course2;
    private Course course3;

    private Student student1;
    private Student student2;

    /**
     * create instances for testing before each test method
     */
    @BeforeEach
    void createInstances(){

        teacher1 = new Teacher(1,"Catalin","Rusu");
        teacher2 = new Teacher(2,"Diana", "Cristea");

        course1 = new Course(1,"OOP",teacher1,20,6);
        course2 = new Course(2,"SDA",teacher2,30,5);
        course3 = new Course(3,"NewOptional",teacher2, 5,20);

        student1 = new Student(1,"Denisa","Dragota");
        student2 = new Student(2,"Mihnea", "Aleman");

    }

    @Test
    void getName() {
        assertEquals("OOP",course1.getName());
    }

    @Test
    void setName() {
        course2.setName("DSA");
        assertEquals("DSA",course2.getName());
    }

    @Test
    void getTeacher() {
        assertEquals(teacher1,course1.getTeacher());
    }

    @Test
    void setTeacher() {
        course3.setTeacher(teacher1);
        assertEquals(teacher1, course3.getTeacher());
    }

    @Test
    void getMaxEnrollment() {
        assertEquals(30,course2.getMaxEnrollment());
    }

    @Test
    void setMaxEnrollment() {
        course2.setMaxEnrollment(25);
        assertEquals(25, course2.getMaxEnrollment());
    }

    @Test
    void getStudentsEnrolled() {
        List<Student> stud = new ArrayList<>();
        stud.add(student1);
        stud.add(student2);
        course1.setStudentsEnrolled(stud);

        assertArrayEquals(stud.toArray(),course1.getStudentsEnrolled().toArray());
    }

    @Test
    void setStudentsEnrolled() {

        List<Student> stud = new ArrayList<>();
        stud.add(student1);
        stud.add(student2);
        course1.setStudentsEnrolled(stud);

        assertArrayEquals(stud.toArray(),course1.getStudentsEnrolled().toArray());
    }

    @Test
    void getCredits() {
        assertEquals(5,course2.getCredits());
    }

    @Test
    void setCredits() {
        course2.setCredits(4);
        assertEquals(4,course2.getCredits());
    }

    @Test
    void getCourseId() {
        assertEquals(1,course1.getCourseId());
    }

    @Test
    void setCourseId() {
        course1.setCourseId(10);
        assertEquals(10,course1.getCourseId());
    }

    @Test
    void compareTo() {
        assertTrue(course1.compareTo(course1));
        assertFalse(course1.compareTo(course2));
    }
}