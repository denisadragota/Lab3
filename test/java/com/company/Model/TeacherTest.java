package com.company.Model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {
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
        assertEquals("Catalin",teacher1.getFirstName());
    }

    @Test
    void setFirstName(){
        teacher1.setFirstName("CATALIN");
        assertEquals("CATALIN",teacher1.getFirstName());
    }

    @Test
    void getLastName(){
        assertEquals("Rusu",teacher1.getLastName());
    }

    @Test
    void setLastName(){
        teacher1.setLastName("RUSU");
        assertEquals("RUSU",teacher1.getLastName());
    }

    @Test
    void getCourses() {

        List<Course> courses=new ArrayList<Course>();
        courses.add(course2);
        courses.add(course3);

        teacher1.setCourses(courses);
        assertArrayEquals(courses.toArray(),teacher1.getCourses().toArray());

    }

    @Test
    void setCourses() {

        List<Course> courses=new ArrayList<Course>();
        courses.add(course2);
        courses.add(course3);

        teacher1.setCourses(courses);
        assertArrayEquals(courses.toArray(),teacher1.getCourses().toArray());

    }

    @Test
    void getTeacherId() {

        assertEquals(1,teacher1.getTeacherId());
    }

    @Test
    void setTeacherId() {
        teacher1.setTeacherId(10);
        assertEquals(10,teacher1.getTeacherId());

    }
}