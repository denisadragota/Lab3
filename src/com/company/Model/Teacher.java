package com.company.Model;
import java.util.ArrayList;
import java.util.List;

public class Teacher extends Person{
    private List<Course> courses;
    private long teacherId;

    public Teacher(long teacherId, String firstName, String lastName) {
        this.teacherId=teacherId;
        this.courses = new ArrayList<>();
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    public long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(long teacherId) {
        this.teacherId = teacherId;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", teacherId=" + teacherId +
                '}';
    }
}
