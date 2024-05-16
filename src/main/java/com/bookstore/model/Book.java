package com.bookstore.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;

public class Book {
    @Id 
    String id;
    String title;
    String author;
    String genre;
    String description;
    Integer stock;
    Integer pages;
    BigDecimal price;
    List<String> images;
    String publisher;
    Date publishDate;

    public Date getpublishDate() {
        return publishDate;
    }
    public void setpublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
    public String getPublisher() {
        return publisher;
    }
    public void setPublisher(String publisher) {
        this.publisher = publisher;
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
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Integer getstock() {
        return stock;
    }
    public void setstock(Integer stock) {
        this.stock = stock;
    }
    public Integer getPages() {
        return pages;
    }
    public void setPages(Integer pages) {
        this.pages = pages;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public List<String> getImages() {
        return images;
    }
    public void setImages(List<String> images) {
        this.images = images;
    }
}
