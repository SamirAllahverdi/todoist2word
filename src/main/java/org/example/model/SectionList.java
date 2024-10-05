package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class SectionList {

    private List<SectionResponse> sectionResponse;

    public SectionList() {
        this.sectionResponse = new ArrayList<>();
    }

    public List<SectionResponse> getProjectResponse() {
        return sectionResponse;
    }

    public void setProjectResponse(List<SectionResponse> sectionResponse) {
        this.sectionResponse = sectionResponse;
    }
}
