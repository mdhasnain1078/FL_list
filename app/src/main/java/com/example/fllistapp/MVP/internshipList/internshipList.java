package com.example.fllistapp.MVP.internshipList;

import androidx.fragment.app.FragmentActivity;

import com.example.fllistapp.model.FilterStateModel;
import com.example.fllistapp.model.Internship;


import java.util.ArrayList;

public interface internshipList {

    interface View {
        void showLoading();
        void hideLoading();
        void showInternships(ArrayList<Internship> internships);
        void showError(String message);
        void updateInternshipCount(int count);
        void onClickFilterBtn(ArrayList<FilterStateModel> profileFilters, ArrayList<FilterStateModel> cityFilters, int duration, ArrayList<String> durations);
        void applyFilterCall();
        void setAppliedFilters(ArrayList<FilterStateModel> filters, int filterCount);
        void callAppliedFilters();
        void updateAppliedFilters(int filterCount);

    }

    interface Presenter {
        void attachView(View view);
        void loadInternships(boolean isReload);
        void onClickFilterBtn(FragmentActivity activity, boolean isFilterApplied);
        void applyFilters(ArrayList<FilterStateModel> profileFilters, ArrayList<FilterStateModel> cityFilters, int duration);
        void setAppliedFilters();
        void removeFilter(int id);
    }
}

