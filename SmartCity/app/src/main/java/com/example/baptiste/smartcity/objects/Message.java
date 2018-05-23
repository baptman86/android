package com.example.baptiste.smartcity.objects;

import java.util.Date;

/**
 * Created by ordinateur on 23/05/2018.
 */

public class Message {
    private String sender_id;
    private Date date;
    private String content;

    public Message() {}

    public Message(String sender_id, Date date, String content) {
        this.sender_id = sender_id;
        this.date = date;
        this.content = content;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
