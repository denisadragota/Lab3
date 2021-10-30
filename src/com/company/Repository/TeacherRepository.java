package com.company.Repository;


import com.company.Exceptions.NullException;
import com.company.Model.Teacher;

import java.util.List;

public class TeacherRepository extends InMemoryRepository<Teacher>{
        public TeacherRepository(List<Teacher> repoList) {
            super(repoList);
        }

        @Override
        public Teacher findOne(Long id) throws NullException {

            if(id==null)
                throw new NullException("Null id!");

            for(Teacher t: this.repoList)
            {
                if(t.getTeacherId()==id)
                    return t;
            }
            return null;
        }

        @Override
        public Teacher save(Teacher obj) throws NullException {
            if(obj==null)
                throw new NullException("Null obj!");
            if (this.findOne(obj.getTeacherId()) != null)
                return obj;
            this.repoList.add(obj);
            return null;
        }

        @Override
        public Teacher update(Teacher obj) throws NullException {
            if(obj==null)
                throw new NullException("Null object!");
            Teacher teacher = this.findOne(obj.getTeacherId());
            if (teacher == null)
                return obj;
            this.repoList.remove(teacher);
            this.repoList.add(obj);
            return null;
        }

        @Override
        public Teacher delete(Long id) throws NullException {
            if(id==null)
                throw new NullException("Null id!");
            if (this.findOne(id) == null)
                return null;
            Teacher toDelete=this.findOne(id);

            this.repoList.remove(toDelete);
            return toDelete;
        }

}
