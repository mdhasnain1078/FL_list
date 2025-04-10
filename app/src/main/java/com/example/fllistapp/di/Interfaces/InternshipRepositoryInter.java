package com.example.fllistapp.di.Interfaces;

import com.example.fllistapp.model.Internship;

import java.util.ArrayList;

public interface InternshipRepositoryInter {
    interface InternshipCallback {
        void onSuccess(ArrayList<Internship> internships);
        void onError(String error);
    }

    void fetchInternships(InternshipCallback callback);
}
