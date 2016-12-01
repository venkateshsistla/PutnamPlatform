package com.sapient.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class GenericDAOImpl<T, ID extends Serializable> implements GenericDAO<T, ID> {

	private Class<T> type;
	private EntityManager em;

	public GenericDAOImpl(Class<T> type, EntityManager em) {
		this.type = type;
		this.em = em;
	}

	@Override
	public T save(T entity) {
		EntityTransaction trxn = em.getTransaction();
		try {
			trxn.begin();
			em.persist(entity);
			trxn.commit();

		} catch (Exception e) {
			trxn.rollback();

		}

		return entity;
	}

	@Override
	public void delete(T entity) {

		EntityTransaction trxn = em.getTransaction();
		try {
			trxn.begin();
			em.remove(entity);
			trxn.commit();

		} catch (Exception e) {
			trxn.rollback();

		}

	}

	@Override
	public T findByPrimaryKey(ID id) {

		return em.find(type, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {

		return em.createQuery("from User").getResultList();
	}

}