package com.example.baptiste.smartcity.objects;

/**
 * Created by ordinateur on 23/05/2018.
 */

public class Invitation {
    private String user_id_send;
    private String user_id_receive;
    private String discussion_id;
    private Boolean reponse = null;

    public Invitation() {}

    public Invitation(String user_id_send, String user_id_receive, String discussion_id) {
        this.user_id_send = user_id_send;
        this.user_id_receive = user_id_receive;
        this.discussion_id = discussion_id;
    }

    public String getUser_id_send() {
        return user_id_send;
    }

    public void setUser_id_send(String user_id_send) {
        this.user_id_send = user_id_send;
    }

    public String getUser_id_receive() {
        return user_id_receive;
    }

    public void setUser_id_receive(String user_id_receive) {
        this.user_id_receive = user_id_receive;
    }

    public String getDiscussion_id() {
        return discussion_id;
    }

    public void setDiscussion_id(String discussion_id) {
        this.discussion_id = discussion_id;
    }

    public Boolean getReponse() {
        return reponse;
    }

    public void setReponse(Boolean reponse) {
        this.reponse = reponse;
    }
}
