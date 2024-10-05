package org.example.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Attachment {

    @JsonProperty("file_name")
    String fileName;
    @JsonProperty("file_type")
    String fileType;
    @JsonProperty("file_url")
    String fileUrl;
    @JsonProperty("image")
    String image;

    public String getFileName() {
        return fileName == null ? "" : fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "fileName='" + fileName + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
