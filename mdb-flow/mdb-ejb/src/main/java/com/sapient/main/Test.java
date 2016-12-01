package com.sapient.main;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.sapient.shared.Employee;

public class Test {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("hibernate");
		EntityManager em = emf.createEntityManager();
		Employee login = new Employee();
		login.setName("sist");
		login.setDesignation("vetesh");
		EntityTransaction trans = em.getTransaction();
		try {
			trans.begin();
			em.persist(login);

			trans.commit();
		} catch (Exception e) {

			trans.rollback();

		}
		emf.close();
	}
}