package com.example.librarymanagementsystem;

public class issue {
    String student_id,book_name,author_name,issue_date,due_date,return_date,status,key,id,idstatus;

    public issue() {
    }

    public issue(String student_id, String book_name, String author_name, String issue_date, String due_date, String return_date, String status, String key, String id, String idstatus) {
        this.student_id = student_id;
        this.book_name = book_name;
        this.author_name = author_name;
        this.issue_date = issue_date;
        this.due_date = due_date;
        this.return_date = return_date;
        this.status = status;
        this.key = key;
        this.id = id;
        this.idstatus = idstatus;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getIssue_date() {
        return issue_date;
    }

    public void setIssue_date(String issue_date) {
        this.issue_date = issue_date;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdstatus() {
        return idstatus;
    }

    public void setIdstatus(String idstatus) {
        this.idstatus = idstatus;
    }
}
