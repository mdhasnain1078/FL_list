package com.example.fllistapp.utils;

import com.example.fllistapp.model.FilterStateModel;
import com.example.fllistapp.model.Internship;

import java.util.ArrayList;
import java.util.List;

public class commonValues {
    public static List<Internship> internships = new ArrayList<>();
    public static List<FilterStateModel> profileFilters = new ArrayList<>();
    public static List<FilterStateModel> cityFilters = new ArrayList<>();
    public static int duration = 0;
    public static boolean isFilterApplied = false;
}
