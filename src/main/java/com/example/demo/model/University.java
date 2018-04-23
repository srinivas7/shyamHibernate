/**
 * 
 */
package com.example.demo.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author shysatya
 *
 */
@Entity
@Table(name="UNIVERSITY")
public class University implements Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="UNIVERSITY_ID")
	private int UNIVERSITY_ID;
	
	private String unv_name;
	
	private String place;
	

    public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<Student> students;
    
	
	public int getUnv_id() {
		return UNIVERSITY_ID;
	}

	public void setUnv_id(int unv_id) {
		this.UNIVERSITY_ID = unv_id;
	}

	public String getUnv_name() {
		return unv_name;
	}

	public void setUnv_name(String unv_name) {
		this.unv_name = unv_name;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	
	public University() {
		 
    }
 
    public University(String name, String country) {
        this.unv_name = name;
        this.place = country;
    }
 
}
