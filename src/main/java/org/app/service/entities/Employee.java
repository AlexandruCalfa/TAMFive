package org.app.service.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Employee implements Comparable<Employee>, Serializable{
	@Id
	@Column(name = "employee_id")
	private String id;
	@Column(name = "employee_full_name")
	private String fullName;
	@Column(name = "employee_cnp")
	private String cnp;
	@Column(name = "employee_address")
	private String address;
	@Column(name = "employee_birth_date")
	private Date birthDate;
	@Column(name = "employee_employment_date")
	private Date employmentDate;
	@Column(name = "employee_email")
	private String email;
	@Column(name = "employee_phone")
	private String phone;
	@Column(name = "employee_activity_status")
	private boolean activityStatus;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
	private List<Benefit> benefits = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
	private List<Evaluation> evaluation = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "employee")
	private Position position;

	@OneToOne(fetch = FetchType.LAZY, mappedBy = "employee")
	private Credentials credentials;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getCnp() {
		return cnp;
	}
	public void setCnp(String cnp) {
		this.cnp = cnp;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public Date getEmploymentDate() {
		return employmentDate;
	}
	public void setEmploymentDate(Date employmentDate) {
		this.employmentDate = employmentDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public boolean isActivityStatus() {
		return activityStatus;
	}
	public void setActivityStatus(boolean activityStatus) {
		this.activityStatus = activityStatus;
	}
	@Override
	public int compareTo(Employee o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
