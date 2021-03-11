package org.thoughts.on.java.book;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;

@Entity
public class Book {

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private int version;

    private String title;

    private String author;
    
    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
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
}