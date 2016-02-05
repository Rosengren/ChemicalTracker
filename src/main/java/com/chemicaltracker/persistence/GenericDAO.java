package com.chemicaltracker.persistence;

import java.util.List;

public interface GenericDAO<T> {

	T create(T t);
	T update(T t);
	T find(Object id);
	T find(Object hash, Object range);
	void delete(T t);
}