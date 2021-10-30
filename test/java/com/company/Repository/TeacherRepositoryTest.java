package com.company.Repository;

import com.company.Exceptions.NullException;
import com.company.Model.Course;
import com.company.Model.Student;
import com.company.Model.Teacher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TeacherRepositoryTest class
 * test TeacherRepository class
 *
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
class TeacherRepositoryTest {
    private Teacher teacher1;
    private Teacher teacher2;
    private Teacher teacher3;

    private TeacherRepository teacher_repo;

    /**
     * create instances for testing before each test method
     */
    @BeforeEach
    void createInstances(){
        /*creating instances*/
        teacher1 = new Teacher(1,"Catalin","Rusu");
        teacher2 = new Teacher(2,"Diana", "Cristea");
        teacher3 = new Teacher(3, "Cristian", "Sacarea");

        /* set a teacher list to the repo*/
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(teacher1);
        teachers.add(teacher2);
        teacher_repo=new TeacherRepository(teachers);
    }

    /**
     * test findAll() method
     */
    @Test
    void findAll(){
        /*creating the expected result list */
        Teacher[] teachers = new Teacher[2];
        teachers[0]=teacher1;
        teachers[1]=teacher2;

        /* comparing the arrays */
        assertArrayEquals(teachers,((Collection<?>)teacher_repo.findAll()).toArray());
    }

    /**
     * test findOne() method
     */
    @Test
    void findOne() {
        /* search for existing teacher id */
        try {
            assertEquals(teacher1, teacher_repo.findOne(1L));
        }catch(NullException e){
            System.out.println(e.getMessage());
        }

        /*search for non-existing teacher id */
        try {
            assertNull(teacher_repo.findOne(123L));
        }catch(NullException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * test save() method
     */
    @Test
    void save(){
        /* save teacher_repo size at the beginning */
        int sizeBefore=((Collection<?>)teacher_repo.findAll()).size();

        /* add an already existing instance in the repo */
        /* will not be added, size remains the same */
        try {
            assertEquals(teacher1, teacher_repo.save(teacher1));
            assertEquals(sizeBefore, ((Collection<?>) teacher_repo.findAll()).size());
        }catch(NullException e){
            System.out.println(e.getMessage());
        }

        /* add a new instance to the repo */
        /* size of the repo increments */
        try {
            assertNull(teacher_repo.save(teacher3));
            assertEquals(sizeBefore + 1, ((Collection<?>) teacher_repo.findAll()).size());
        }catch (NullException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * test update() method
     */
    @Test
    void update() {
        /* try to update a teacher that does not exist in the repo */
        try {
            teacher3.setLastName(teacher3.getLastName().substring(0, 3));
            assertEquals(teacher3, teacher_repo.update(teacher3));
        }catch(NullException e){
            System.out.println(e.getMessage());
        }

        /* modify the Last Name attribute from existing teacher1 in repo */
        String nameBefore= teacher1.getLastName();
        String nameAfter= nameBefore.substring(0,3)+".";
        teacher1.setLastName(nameAfter);
        try {
            assertNull(teacher_repo.update(teacher1));
            assertEquals(nameAfter, teacher_repo.findOne(teacher1.getTeacherId()).getLastName());
        }catch(NullException e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * test delete() method
     */
    @Test
    void delete(){
        /* try to delete a non-existing teacherId in the repo */
        try {
            assertNull(teacher_repo.delete(teacher3.getTeacherId()));
        }catch(NullException e){
            System.out.println(e.getMessage());
        }

        /* delete a teacher from the repo */
        try {
            assertEquals(teacher1, teacher_repo.delete(teacher1.getTeacherId()));
        }catch(NullException e){
            System.out.println(e.getMessage());
        }

        /* the teacherId does not exist in the repo anymore */
        try {
            assertNull(teacher_repo.findOne(teacher1.getTeacherId()));
        }catch(NullException e){
            System.out.println(e.getMessage());
        }
    }
}