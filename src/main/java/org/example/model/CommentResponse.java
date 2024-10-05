package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentResponse {
    @JsonProperty("id")
    private String id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("attachment")
    private Attachment attachment;

    public Attachment getAttachment() {
        return attachment;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "CommentResponse{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", attachment=" + attachment +
                '}';
    }
}
