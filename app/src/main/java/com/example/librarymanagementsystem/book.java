package com.example.librarymanagementsystem;

public class book {
    String Id,Name,Author_name,Publisher,no,loc;

    book()
    {

    }

    public book(String id, String name, String author_name, String publisher, String no, String loc) {
        Id = id;
        Name = name;
        Author_name = author_name;
        Publisher = publisher;
        this.no = no;
        this.loc = loc;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAuthor_name() {
        return Author_name;
    }

    public void setAuthor_name(String author_name) {
        Author_name = author_name;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }
}
