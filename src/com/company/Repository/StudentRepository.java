package com.company.Repository;

import com.company.Model.Student;

import java.util.List;

public class StudentRepository extends InMemoryRepository<Student>{

    public StudentRepository(List<Student> repoList) {
        super(repoList);
    }

    @Override
    public Student findOne(Long id) {
        for(Student s: this.repoList)
        {
            if(s.getStudentId()==id)
                return s;
        }
        return null;
    }

    @Override
    public Student save(Student obj) {
        if (this.findOne(obj.getStudentId()) != null)
            return obj;
        this.repoList.add(obj);
        return null;
    }

    @Override
    public Student update(Student obj) {
        Student student = this.findOne(obj.getStudentId());
        if (student == null)
            return obj;
        this.repoList.remove(student);
        this.repoList.add(obj);
        return null;
    }

    @Override
    public Student delete(Long id) {
        if (this.findOne(id) == null)
            return null;
        Student toDelete=this.findOne(id);

        this.repoList.remove(toDelete);
        return toDelete;
    }
}
