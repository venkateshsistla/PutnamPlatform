package com.sapient.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Employee implements Serializable {
	/**
	 * 
	 */
	/**
	 * 
	 */

	private String name;
	private String designation;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", designation=" + designation + "]";
	}

	public Employee(String name, String designation) {
		super();
		this.name = name;
		this.designation = designation;
	}

	public Employee() {
		super();
	}

}