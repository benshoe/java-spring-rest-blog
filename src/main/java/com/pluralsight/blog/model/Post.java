package com.pluralsight.blog.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;

@Entity
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Long version;

	@NotNull
	@Size(min = 4, max = 100)
	private String title;

	@Column(length = 1000000)
	@Lob
	private String body;

	@Temporal(TemporalType.DATE)
	@CreationTimestamp
	private Date date;

	@ManyToOne(fetch = FetchType.EAGER)
	private Author author;

	public Post() {
		super();
	}

	public Post(String title, String body) {//, Author author) {
		this();
		this.title = title;
		this.body = body;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getDate() {
		return date;
	}

	public String getDateStr() {
		DateFormat outputFormatter = new SimpleDateFormat("MM/dd/yyyy");
		return outputFormatter.format(this.date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Post))
			return false;
		Post otherPost = (Post) obj;
		return this.title.equals(otherPost.getTitle()) &&
			this.body.equals(otherPost.getBody());
	}
}
