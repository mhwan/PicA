package com.picaproject.pica.Item;

public class CommentItem {
    private int id;
    private String nickname;
    private String contents;
    private String date;
    private String tagId;
    private int authorId;

    /**
     * 배명환&박희수
     * @param id
     * @param nickname
     * @param contents
     * @param date
     */
    public CommentItem(int id, String nickname, String contents, String date) {
        this.id = id;
        this.nickname = nickname;
        this.contents = contents;
        this.date = date;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public CommentItem(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
}
