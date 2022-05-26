package com.example.writocityapplication.models;

public class Comment {
    public String comment, commentPostedBy;

    public Comment(){}

    public Comment(String comment, String commentPostedBy) {
        this.comment = comment;
        this.commentPostedBy = commentPostedBy;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentPostedBy() {
        return commentPostedBy;
    }

    public void setCommentPostedBy(String commentPostedBy) {
        this.commentPostedBy = commentPostedBy;
    }
}
