package com.dejobhu.skhu.dejobhu.models;

public class ItemText {

    int postNum;
    String content;

    public ItemText(int postNum, String content) {
        this.postNum = postNum;
        this.content = content;
    }

    public int getPostNum() {
        return postNum;
    }

    public void setPostNum(int postNum) {
        this.postNum = postNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
