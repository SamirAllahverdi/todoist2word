package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class CommentList {

    private List<CommentResponse> commentResponse;


    public CommentList() {
        this.commentResponse = new ArrayList<>();
    }

    public List<CommentResponse> getCommentResponse() {
        return commentResponse;
    }

    public void setCommentResponse(List<CommentResponse> commentResponse) {
        this.commentResponse = commentResponse;
    }
}
