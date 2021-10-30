package com.company.Repository;

import com.company.Exceptions.NullException;
import com.company.Model.Course;

import java.util.List;

public class CourseRepository extends InMemoryRepository<Course>{
    public CourseRepository(List<Course> repoList) {

        super(repoList);
    }

    @Override
    public Course findOne(Long id) throws NullException {
        if(id == null)
            throw new NullException("Null id!");

        for(Course c: this.repoList)
        {
            if(c.getCourseId()==id)
                return c;
        }
        return null;
    }

    @Override
    public Course save(Course obj)throws NullException {
        if(obj == null)
            throw new NullException("Null object!");
        if (this.findOne(obj.getCourseId()) != null)
            return obj;
        this.repoList.add(obj);
        return null;
    }

    @Override
    public Course update(Course obj) throws NullException {
        if(obj == null)
            throw new NullException("Null object!");

        Course course = this.findOne(obj.getCourseId());
        if (course == null)
            return obj;
        this.repoList.remove(course);
        this.repoList.add(obj);
        return null;
    }

    @Override
    public Course delete(Long id) throws NullException{
        if(id == null)
            throw new NullException("Null id!");
        if (this.findOne(id) == null)
            return null;
        Course toDelete=this.findOne(id);

        this.repoList.remove(toDelete);
        return toDelete;
    }

}
