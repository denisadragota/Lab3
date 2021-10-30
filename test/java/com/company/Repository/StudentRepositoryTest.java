package com.company.Repository;

import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * StudentRepositoryTest class
 * testing StudentRepository class
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
class StudentRepositoryTest {

    private Student student1;
    private Student student2;
    private Student student3;
    private Student student4;
    private StudentRepository stud_repo;

    /**
     * create instances for testing before each test method
     */
    @BeforeEach
    void createInstances(){
        /*creating instances*/
        student1 = new Student(1,"Denisa","Dragota");
        student2 = new Student(2,"Mihnea", "Aleman");
        student3 = new Student(3,"Raul","Barbat");
        student4 = new Student(4,"Evelin","Bohm");

        /* set a student list to the repo*/
        List<Student> students = new ArrayList<>();
        students.add(student1);
        students.add(student2);
        stud_repo=new StudentRepository(students);
    }

    /**
     * test findAll() method
     */
    @Test
    void findAll(){
        /*creating the expected result list */
        Student[] students = new Student[2];
        students[0]=student1;
        students[1]=student2;

        /* comparing the arrays */
        assertArrayEquals(students,((Collection<?>)stud_repo.findAll()).toArray());
    }

    /**
     * test findOne() method
     */
    @Test
    void findOne() {
        /* search for existing student id */
        try {
            assertEquals(student1, stud_repo.findOne(null));
        }catch(NullException e){
            System.out.println(e.getMessage());
        }

        /*search for non-existing student id */
        try {
            assertNull(stud_repo.findOne(123L));
        }catch(NullException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * test save() method
     */
    @Test
    void save(){
        /* save stud_repo size at the beginning */
        int sizeBefore=((Collection<?>)stud_repo.findAll()).size();

        /* add an already existing instance in the repo */
        /* will not be added, size remains the same */
        try {
            assertEquals(student1, stud_repo.save(student1));
            assertEquals(sizeBefore, ((Collection<?>) stud_repo.findAll()).size());
        }catch(NullException e){
            System.out.println(e.getMessage());
        }

        /* add a new instance to the repo */
        /* size of the repo increments */
        try {
            assertNull(stud_repo.save(student3));
            assertEquals(sizeBefore + 1, ((Collection<?>) stud_repo.findAll()).size());
        } catch (NullException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * test update() method
     */
    @Test
    void update(){
        /* try to update a student that does not exist in the repo */
        try {
            student4.setTotalCredits(30);
            assertEquals(student4, stud_repo.update(student4));
        }catch (NullException e){
            System.out.println(e.getMessage());
        }

        /* modify the TotalCredits attribute from existing student1 in repo */
        try {
            assertEquals(0, stud_repo.findOne(student1.getStudentId()).getTotalCredits());
            student1.setTotalCredits(30);
            assertNull(stud_repo.update(student1));
            assertEquals(30, stud_repo.findOne(student1.getStudentId()).getTotalCredits());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * test delete() method
     */
    @Test
    void delete() {
        /* try to delete a non-existing studentId in the repo */
        try {
            assertNull(stud_repo.delete(student4.getStudentId()));
        }catch(NullException e){
            System.out.println(e.getMessage());
        }

        /* delete a student from the repo */
        try {
            assertEquals(student1, stud_repo.delete(student1.getStudentId()));
        }catch(NullException e){
            System.out.println(e.getMessage());
        }

        /* the studentId does not exist in the repo anymore */
        try {
            assertNull(stud_repo.findOne(student1.getStudentId()));
        }catch(NullException e){
            System.out.println(e.getMessage());
        }
    }
}