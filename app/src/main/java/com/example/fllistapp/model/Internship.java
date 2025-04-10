package com.example.fllistapp.model;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

@JsonObject
public class Internship {

    @JsonField(name = "id")
    public int id;

    @JsonField(name = "profile_name")
    public String title;

    @JsonField(name = "company_name")
    public String companyName;

    @JsonField(name = "location_names")
    public String[] locationNames;

    @JsonField(name = "stipend")
    public Stipend stipend;

    @JsonField(name = "duration")
    public String duration;

    @JsonField(name = "start_date")
    public String startDate;

    @JsonField(name = "application_deadline")
    public String applicationDeadline;

    @JsonField(name = "url")
    public String url;

    @JsonField(name = "part_time")
    public boolean partTime;


    @JsonObject
    public static class Stipend {
        @JsonField(name = "salary")
        public String salary;
    }
}

