package ca.javau11.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Experience {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "experience_id", referencedColumnName = "id")
	@JsonBackReference
	private Profile profile;
	
	private String title;
	private String company;
	private String location;
	private LocalDate startDate;
	private LocalDate endDate;
	private Boolean current;
	private String description;
	
	public Experience() {}

	public Experience(Profile profile, String title, String company, String location, LocalDate dateFrom,
			LocalDate dateTo, Boolean current, String description) {
		this.profile = profile;
		this.title = title;
		this.company = company;
		this.location = location;
		this.startDate = dateFrom;
		this.endDate = dateTo;
		this.current = current;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Profile getProfile() {
		return profile;
	}

	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Boolean getCurrent() {
		return current;
	}

	public void setCurrent(Boolean current) {
		this.current = current;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Experience [id=" + id + ", profile=" + profile + ", title=" + title + ", company=" + company
				+ ", location=" + location + ", startDate=" + startDate + ", endDate=" + endDate + ", current="
				+ current + ", description=" + description + "]";
	}

}