package ca.javau11.entities;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Profile {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@OneToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@JsonManagedReference
	private User user;
	
	private String company;
	private String website;
	private String location;	
	private String status;
	private List<String> skills;
	private String bio;
	private String githubusername;
	private String youtube;
	private String twitter;
	private String facebook;
	private String linkedin;
	private String instagram;
	
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime date;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "profile", orphanRemoval=true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<Experience> experiences;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "profile", orphanRemoval=true)
	@JsonManagedReference
	private List<Education> educations;
	
	public Profile() {}

	public Profile(String company, String location, String status, List<String> skills) {
		this.company = company;
		this.location = location;
		this.status = status;
		this.skills = skills;
	}

	// is this correct?
	public void addUser(User user) {
		user.setId(user.getId());
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getSkills() {
		return skills;
	}

	public void setSkills(List<String> skills) {
		this.skills = skills;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getGithubusername() {
		return githubusername;
	}

	public void setGithubusername(String githubusername) {
		this.githubusername = githubusername;
	}

	public String getYoutube() {
		return youtube;
	}

	public void setYoutube(String youtube) {
		this.youtube = youtube;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getLinkedin() {
		return linkedin;
	}

	public void setLinkedin(String linkedin) {
		this.linkedin = linkedin;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public List<Experience> getExperiences() {
		return experiences;
	}

	public void setExperiences(List<Experience> experiences) {
		this.experiences = experiences;
	}

	public List<Education> getEducations() {
		return educations;
	}

	public void setEducations(List<Education> educations) {
		this.educations = educations;
	}

	@Override
	public String toString() {
		return "Profile [id=" + id + ", user=" + user + ", company=" + company + ", website=" + website + ", location="
				+ location + ", status=" + status + ", skills=" + skills + ", bio=" + bio + ", githubusername="
				+ githubusername + ", youtube=" + youtube + ", twitter=" + twitter + ", facebook=" + facebook
				+ ", linkedin=" + linkedin + ", instagram=" + instagram + ", date=" + date + ", experiences="
				+ experiences + ", educations=" + educations + "]";
	}

}