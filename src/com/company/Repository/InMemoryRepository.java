package com.company.Repository;

import java.util.ArrayList;
import java.util.List;

public abstract class InMemoryRepository<T> implements ICrudRepository<T> {
    protected List<T> repoList;

    public InMemoryRepository(List<T> repoList) {

        this.repoList = repoList;
    }


    @Override
    public Iterable<T> findAll() {

        return this.repoList;
    }



}
