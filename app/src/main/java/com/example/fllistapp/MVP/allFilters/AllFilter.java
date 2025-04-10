package com.example.fllistapp.MVP.allFilters;

import androidx.fragment.app.FragmentActivity;

import com.example.fllistapp.model.FilterStateModel;

import java.util.ArrayList;
import java.util.List;

public interface AllFilter {

    interface View {
        void showProfileFilter();
        void hideProfileFilter();
        void showCityFilter();
        void hideCityFilter();
        void initProfileFilter(ArrayList<FilterStateModel> profileFilter);
        void initCityFilter(ArrayList<FilterStateModel> cityFilter);
        void setDropdown(ArrayList<String> durations);
        void updateProfileFilter(ArrayList<FilterStateModel> profileFilters);
        void updateCityFilter(ArrayList<FilterStateModel> cityFilters);
        void updateDurationFilter(int duration, String mon);

    }

    interface Presenter {
        void attachView(AllFilter.View view);
        void loadFilters(ArrayList<FilterStateModel> profileFilters, ArrayList<FilterStateModel> cityFilters, int duration, ArrayList<String> durations);
        void removeProfile(int id);
        void removeCity(int id);
        void updateDurationFilter(int duration);
        void onClickProfile(FragmentActivity activity);
        void onClickCity(FragmentActivity activity);
        void onClickBack(FragmentActivity activity);

        void onApplyButtonBtn(FragmentActivity activity);

        void onClearButtonBtn();

    }

}
