package com.bookstore.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

public class Book {
    @Id
    String id;
    String title;
    String author;
    String genre;
    String description;
    Integer stock;
    Integer soldQty;
    Integer pages;
    @Field(targetType = FieldType.DECIMAL128)
    BigDecimal price;
    List<String> images;
    String publisher;
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
    Date publishDate;
    public Book() {
        soldQty = 0;
    }
    public Book(BookForm bookForm) {
        title = bookForm.getTitle();
        author = bookForm.getAuthor();
        description = bookForm.getDescription();
        genre = bookForm.getGenre();
        stock = bookForm.getStock();
        pages = bookForm.getPages();
        price = bookForm.getPrice();
        publishDate = bookForm.getPublishDate();
        publisher = bookForm.getPublisher();
        soldQty = 0;
    }
    public String getId() {
        return id;
    }

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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
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

    public Integer getSoldQty() {
        return soldQty;
    }

    public void setSoldQty(Integer soldQty) {
        this.soldQty = soldQty;
    }
    public void set(BookForm bookForm) {
        title = bookForm.getTitle();
        author = bookForm.getAuthor();
        description = bookForm.getDescription();
        genre = bookForm.getGenre();
        stock = bookForm.getStock();
        pages = bookForm.getPages();
        price = bookForm.getPrice();
        publishDate = bookForm.getPublishDate();
        publisher = bookForm.getPublisher();
    }
}
