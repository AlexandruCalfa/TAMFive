package org.app.service.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Benefit implements EntityBase, Comparable<Benefit>, Serializable{

	public static final String START_DATE_FORMAT = "dd-MM-yyyy";
	public static final String END_DATE_FORMAT = "dd-MM-yyyy";

	@Id
	private String id;
	private String type;
	private double value;
	private Date startDate;
	private Date endDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@Override
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	@Override
	public int compareTo(Benefit o) {
		return this.getType().compareTo(o.getType());
	}
}