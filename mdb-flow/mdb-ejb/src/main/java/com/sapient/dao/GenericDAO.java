package com.sapient.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, ID extends Serializable> {

	T save(T entity);

	void delete(T entity);

	T findByPrimaryKey(ID id);

	List<T> findAll();

}