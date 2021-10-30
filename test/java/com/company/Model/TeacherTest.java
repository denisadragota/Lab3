package com.company.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TeacherTest class
 * testing Teacher class
 *
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
class TeacherTest {

    private Teacher teacher1;
    private Teacher teacher2;

    private Course course1;
    private Course course2;
    private Course course3;


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