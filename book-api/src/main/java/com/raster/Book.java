package com.raster;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Book {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq_gen")
	@SequenceGenerator(name="seq_gen", allocationSize=5)
	private Long id;
	private String title;
	private String author;
	private Date publishedOn;
	private String thumbnail;
	
	public Book(Long id, String title, String author, Date publishedOn, String thumbnail) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.publishedOn = publishedOn;
		this.thumbnail = thumbnail;
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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Date getPublishedOn() {
		return publishedOn;
	}

	public void setPublishedOn(Date publishedOn) {
		this.publishedOn = publishedOn;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
}
