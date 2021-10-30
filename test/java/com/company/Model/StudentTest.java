package com.company.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StudentTesst class
 * testing Student class
 *
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
class StudentTest {

    private Teacher teacher1;
    private Teacher teacher2;

    private Course course1;
    private Course course2;

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


        student1 = new Student(1,"Denisa","Dragota");
        student2 = new Student(2,"Mihnea", "Aleman");
    }

    @Test
    void getFirstName(){
        assertEquals("Denisa",student1.getFirstName());
    }

    @Test
    void setFirstName(){
        student1.setFirstName("Denisa-Cristina");
        assertEquals("Denisa-Cristina",student1.getFirstName());
    }

    @Test
    void getLastName(){
        assertEquals("Dragota",student1.getLastName());
    }

    @Test
    void setLastName(){
        student1.setLastName("DRAGOTA");
        assertEquals("DRAGOTA",student1.getLastName());
    }

    @Test
    void getStudentId() {
        assertEquals(1,student1.getStudentId());
    }

    @Test
    void setStudentId() {
        student1.setStudentId(10);
        assertEquals(10, student1.getStudentId());
    }

    @Test
    void getTotalCredits() {
        assertEquals(0,student1.getTotalCredits());
    }

    @Test
    void setTotalCredits() {
        student1.setTotalCredits(5);
        assertEquals(5,student1.getTotalCredits());
    }

    @Test
    void getEnrolledCourses() {

        List<Course> courses = new ArrayList<Course>();
        courses.add(course1);
        courses.add(course2);

        student1.setEnrolledCourses(courses);
        assertArrayEquals(courses.toArray(),student1.getEnrolledCourses().toArray());
    }

    @Test
    void setEnrolledCourses() {
        List<Course> courses = new ArrayList<Course>();
        courses.add(course1);
        courses.add(course2);

        student1.setEnrolledCourses(courses);
        assertArrayEquals(courses.toArray(),student1.getEnrolledCourses().toArray());

    }

    @Test
    void compareTo() {

        assertFalse(student1.compareTo(student2));
        assertTrue(student1.compareTo(student1));

    }
}