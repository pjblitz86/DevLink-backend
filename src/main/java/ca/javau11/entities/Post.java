package ca.javau11.entities;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Post {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	@JsonIgnoreProperties({"posts", "likedPosts"})
	private User user;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "post", orphanRemoval=true)
	@JsonManagedReference
	
	private List<Comment> comments;
	
	private String text;
	private String name;
	private String avatar;
	
	@ManyToMany(mappedBy = "likedPosts")
	@JsonIgnoreProperties("likedPosts")
	private List<User> likes;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate date = LocalDate.now();
	
	public Post() {
		this.date = LocalDate.now();
	}

	public Post(User user, List<Comment> comments, String text, String name, String avatar, List<User> likes,
			LocalDate date) {
		this.user = user;
		this.comments = comments;
		this.text = text;
		this.name = name;
		this.avatar = avatar;
		this.likes = likes;
		this.date = LocalDate.now();
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

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<User> getLikes() {
		return likes;
	}

	public void setLikes(List<User> likes) {
		this.likes = likes;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Post [id=" + id + ", user=" + user + ", comment=" + comments + ", text=" + text + ", name=" + name
				+ ", avatar=" + avatar + ", likes=" + likes + ", date=" + date + "]";
	}

}