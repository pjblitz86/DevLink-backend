package ca.javau11.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "post_id", referencedColumnName = "id")
	@JsonBackReference
	private Post post;
	
	private String text;
	private String name;
	private String avatar;
	
	@Column(columnDefinition = "TIMESTAMP")
	private LocalDateTime date;
	
	public Comment() {}

	public Comment(Post post, String text, String name, String avatar, LocalDateTime date) {
		this.post = post;
		this.text = text;
		this.name = name;
		this.avatar = avatar;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Post getPost() {
		return post;
	}

	public void setPost(Post post) {
		this.post = post;
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

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", post=" + post + ", text=" + text + ", name=" + name + ", avatar=" + avatar
				+ ", date=" + date + "]";
	}
	
}