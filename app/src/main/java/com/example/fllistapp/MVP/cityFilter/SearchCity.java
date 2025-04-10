package com.example.fllistapp.MVP.cityFilter;

import androidx.fragment.app.FragmentActivity;

import com.example.fllistapp.MVP.profileFilter.SearchProfile;
import com.example.fllistapp.model.FilterStateModel;
import com.example.fllistapp.model.Internship;

import java.util.ArrayList;
import java.util.List;

public interface SearchCity {
    interface View {
        void initSelectedCityAdapter(ArrayList<FilterStateModel> filterList);

        void initCheckboxAdapter(ArrayList<FilterStateModel> filterList);

        void initTextEditView();

        void updateSelectedCity(ArrayList<FilterStateModel> profileFilters);

        void updateCheckBoxFilter(ArrayList<FilterStateModel> checkBoxFilter);
        void updateCheckBoxFilterSearch(String s);

        void filterCheckBoxList(String s);

        void clearTextEditView();

    }

    interface Presenter {
        void attachView(SearchCity.View view);

        void loadFilters(ArrayList<FilterStateModel> cityFilters);

        void onClickBackBtn(FragmentActivity activity);

        void onApplyButtonBtn();

        void onClearButtonBtn();

        void onTextChanged(String s);

        void removeSelectedCity(int id, String s);

        void onClickCheckBox(int id, boolean isChecked);
    }
}