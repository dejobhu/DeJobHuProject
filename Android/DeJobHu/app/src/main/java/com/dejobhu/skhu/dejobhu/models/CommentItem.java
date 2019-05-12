package com.dejobhu.skhu.dejobhu.models;

public class CommentItem {
    String name;
    String time;
    String comment;

    public CommentItem(String name, String time, String comment) {
        this.name = name;
        this.time = time;
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
