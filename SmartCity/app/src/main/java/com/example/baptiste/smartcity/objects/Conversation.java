package com.example.baptiste.smartcity.objects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ordinateur on 23/05/2018.
 */

public class Conversation {
    private String user_id1;
    private String user_id2;
    private String topic;
    private ArrayList<Message> messages = new ArrayList<>();

    public Conversation() {}

    public Conversation(String user_id1, String user_id2, String topic, ArrayList<Message> messages) {
        this.user_id1 = user_id1;
        this.user_id2 = user_id2;
        this.topic = topic;
        this.messages = messages;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getUser_id1() {
        return user_id1;
    }

    public void setUser_id1(String user_id1) {
        this.user_id1 = user_id1;
    }

    public String getUser_id2() {
        return user_id2;
    }

    public void setUser_id2(String user_id2) {
        this.user_id2 = user_id2;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void addMessage(Message message) {
        this.messages.add(message);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
    }
}
