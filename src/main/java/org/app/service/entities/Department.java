package org.app.service.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Department implements Comparable<Department>, Serializable{

	@Id
	@Column(name = "department_id")
	private String id;

	@Column(name = "department_name")
	private String name;

	@Column(name = "department_identification_code")
	private String identificationCode;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
	private List<Position> positions = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "department")
	private List<Employee> employee = new ArrayList<>();


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentificationCode() {
		return identificationCode;
	}

	public void setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}

	@Override
	public int compareTo(Department o) {
		// TODO Auto-generated method stub
		return 0;
	}
}