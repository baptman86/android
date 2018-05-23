package com.example.baptiste.smartcity.objects;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ordinateur on 23/05/2018.
 */

public class Conversation {
    private String topic;
    private String description;
    private ArrayList<Message> messages = new ArrayList<>();

    public Conversation() {}

    public Conversation(String topic, String description) {
        this.topic = topic;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
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
