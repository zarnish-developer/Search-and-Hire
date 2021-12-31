package com.example.splash_activity.messaging;

public class ModelComments {
    String comment, commenterUid;

    public ModelComments() {
    }

    public ModelComments(String comment, String commenterUid) {
        this.comment = comment;
        this.commenterUid = commenterUid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommenterUid() {
        return commenterUid;
    }

    public void setCommenterUid(String commenterUid) {
        this.commenterUid = commenterUid;
    }
}
