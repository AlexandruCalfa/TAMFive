package org.app.service.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Evaluation implements Comparable<Evaluation>, Serializable{

	@Id
	@Column(name = "evaluation_id")
    private String id;

	@Column(name = "evaluation_grade")
    private int grade;

	@Column(name = "evaluation_subject")
    private String subject;

	@Column(name = "evaluation_type")
    private String type;

	@Column(name = "evaluation_date")
    private Date evaluationDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "employee_id")
	private Employee employee;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getEvaluationDate() {
		return evaluationDate;
	}

	public void setEvaluationDate(Date evaluationDate) {
		this.evaluationDate = evaluationDate;
	}

	@Override
	public int compareTo(Evaluation o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
