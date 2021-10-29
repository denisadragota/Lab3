package com.company.Model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StudentTest {

    private static Teacher teacher1;
    private static Teacher teacher2;

    private static Course course1;
    private static Course course2;
    private static Course course3;

    private static Student student1;
    private static Student student2;
    private static Student student3;
    private static Student student4;
    private static Student student5;
    private static Student student6;



    @BeforeAll
    static void createInstances(){

        teacher1 = new Teacher(1,"Catalin","Rusu");
        teacher2 = new Teacher(2,"Diana", "Cristea");

        course1 = new Course(1,"OOP",teacher1,20,6);
        course2 = new Course(2,"SDA",teacher2,30,5);
        course3 = new Course(3,"NewOptional",teacher2, 5,20);

        student1 = new Student(1,"Denisa","Dragota");
        student2 = new Student(2,"Mihnea", "Aleman");
        student3 = new Student(3,"Raul","Barbat");
        student4 = new Student(4,"Evelin","Bohm");
        student5 = new Student(5,"Maria", "Morar");
        student6 = new Student(6,"Iarina", "Demian");
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