package com.example.hasan.myapplication.Models;

public class Comment {
    private String comment,commentId,sendById,sendByIsim,sendByImage;

    public String getSendById() {
        return sendById;
    }

    public void setSendById(String sendById) {
        this.sendById = sendById;
    }

    public String getSendByIsim() {
        return sendByIsim;
    }

    public void setSendByIsim(String sendByIsim) {
        this.sendByIsim = sendByIsim;
    }

    public String getSendByImage() {
        return sendByImage;
    }

    public void setSendByImage(String sendByImage) {
        this.sendByImage = sendByImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

}
