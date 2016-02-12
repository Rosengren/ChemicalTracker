package com.chemicaltracker.persistence.dao;

public interface GenericDao<T> {

    T create(T t);
    T update(T t);
    T find(Object id);
    T find(Object hash, Object range);
    void delete(T t);
}
