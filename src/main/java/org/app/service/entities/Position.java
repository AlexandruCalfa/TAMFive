package org.app.service.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name="Position")
public class Position implements Comparable<Position>, Serializable {

	@Id
	@Column(name ="position_id")
	private String id;

	@Column(name = "position_name")
	private String name;

	@Column(name = "position_level")
	private int level;

	@Column(name = "position_status_open")
	private boolean positionStatusOpen;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "department_id")
	private Department department;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private Employee employee;

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
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public boolean isPositionStatusOpen() {
		return positionStatusOpen;
	}
	public void setPositionStatusOpen(boolean positionStatusOpen) {
		this.positionStatusOpen = positionStatusOpen;
	}
	@Override
	public int compareTo(Position o) {
		// TODO Auto-generated method stub
		return 0;
	}
}