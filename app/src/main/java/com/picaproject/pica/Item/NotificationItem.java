package com.picaproject.pica.Item;

public class NotificationItem {
    private int id;
    private String content;
    private String nickname;
    private String date;

    public NotificationItem(int id, String content, String nickname, String date) {
        this.id = id;
        this.content = content;
        this.nickname = nickname;
        this.date = date;
    }

    public NotificationItem(int id, String content, String date) {
        this.id = id;
        this.content = content;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
