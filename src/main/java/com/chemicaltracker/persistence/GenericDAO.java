package com.chemicaltracker.persistence;

public interface GenericDAO<T> {

	T create(T t);
	T update(T t);
	T find(Object id);
	void delete(T t);
}