package com.example.fllistapp.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import java.util.Map;

@JsonObject
public class InternshipResponse {

    @JsonField(name = "internships_meta")
    public Map<String, Internship> internshipsMeta;

    @JsonField(name = "internship_ids")
    public int[] internshipIds;
}
