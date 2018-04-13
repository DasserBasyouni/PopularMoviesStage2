package com.example.dasser.popular.movies.stage2.model;

/**
   Created by DB-Project on 8/23/2017.
*/

public class ReviewsDO {
    private String author, content;

    void setAuthorAndContent(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
