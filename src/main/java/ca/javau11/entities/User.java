package ca.javau11.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import ca.javau11.utils.GravatarUtils;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonBackReference
	private Profile profile;
	
	@NotEmpty(message = "Name is required")
	private String name;
	
	@Email(message = "Invalid email format")
	@NotEmpty(message = "Email is required")
	private String email;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotEmpty(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;
	
	@Column(nullable = true)
	private String avatar;
	
	@CreationTimestamp
	@Column(columnDefinition = "TIMESTAMP", updatable = false)
	private LocalDateTime date;
	
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Post> posts;
	
	@ManyToMany
	@JoinTable(
	    name = "user_likes_post", 
	    joinColumns = @JoinColumn(name = "user_id"), 
	    inverseJoinColumns = @JoinColumn(name = "post_id")
	)
	@JsonIgnoreProperties("likes")
	private List<Post> likedPosts;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Job> jobs;
	
	public User() {}

	public User(String name, String email) {
		this.name = name;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar != null ? avatar : GravatarUtils.getGravatarUrl(this.email, 200);  
    }

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	public List<Post> getLikedPosts() {
		return likedPosts;
	}

	public void setLikedPosts(List<Post> likedPosts) {
		this.likedPosts = likedPosts;
	}
	
	public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + ", avatar="
				+ avatar + ", date=" + date + ", posts=" + posts + ", likedPosts=" + likedPosts + "]";
	}

}