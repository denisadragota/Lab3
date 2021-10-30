package com.company.Repository;


import com.company.Exceptions.NullException;
import com.company.Model.Teacher;

import java.util.List;

/**
 * TeacherRepository class extending InMemoryRepository <Teacher>
 * storing and updating Teacher instances in repoList
 *
 * @version
 *          30.10.2021
 * @author
 *          Denisa Dragota
 */
public class TeacherRepository extends InMemoryRepository<Teacher>{
        public TeacherRepository(List<Teacher> repoList) {
            super(repoList);
        }

    /**
     * @param id -the id of the entity to be returned id must not be null
     * @return the entity with the specified id or null - if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     */
        @Override
        public Teacher findOne(Long id) throws NullException {
            /* Exception */
            if(id==null)
                throw new NullException("Null id!");

            /* loop through all teachers in the repo and find id */
            for(Teacher t: this.repoList)
            {
                if(t.getTeacherId()==id)
                    return t;
            }
            return null;
        }

    /**
     * @param obj entity must be not null
     * @return null- if the given entity is saved otherwise returns the entity (id already exists)
     * @throws NullException if input parameter entity obj is NULL
     * adding a Teacher object to the repo list
     * first checking if already exist, then adding
     */
        @Override
        public Teacher save(Teacher obj) throws NullException {
            /* Exception */
            if(obj==null)
                throw new NullException("Null obj!");

            /* if object already exists in the repo */
            if (this.findOne(obj.getTeacherId()) != null)
                return obj;

            /* add object */
            this.repoList.add(obj);
            return null;
        }

    /**
     * @param obj entity must not be null
     * @return null - if the entity is updated, otherwise returns the entity - (e.g id does not exist).
     * @throws NullException if input parameter entity obj is NULL
     * finds old instance with the same id as the new updated given object
     * removes the old instance and adds the updated one
     */
        @Override
        public Teacher update(Teacher obj) throws NullException {
            /* Exception */
            if(obj==null)
                throw new NullException("Null object!");

            /* find id of object to be updated */
            Teacher teacher = this.findOne(obj.getTeacherId());

            /* if object does not exist in the repo*/
            if (teacher == null)
                return obj;

            /* update by: removing old instance and adding new given updated instance */
            this.repoList.remove(teacher);
            this.repoList.add(obj);
            return null;
        }


    /**
     * @param id id must be not null
     * @return the removed entity or null if there is no entity with the given id
     * @throws NullException if input parameter id is NULL
     * deletes object with given id from the repo list
     * first checks if id exists in the repoList, then delete
     */
        @Override
        public Teacher delete(Long id) throws NullException {
            /* Exception */
            if(id==null)
                throw new NullException("Null id!");

            /* if object does not exist in the repo */
            if (this.findOne(id) == null)
                return null;

            /*removing object with the given id */
            Teacher toDelete=this.findOne(id);
            this.repoList.remove(toDelete);
            return toDelete;
        }

}
