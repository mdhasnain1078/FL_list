package com.example.fllistapp.MVP.profileFilter;

import androidx.fragment.app.FragmentActivity;

import com.example.fllistapp.model.FilterStateModel;
import com.example.fllistapp.model.Internship;

import java.util.ArrayList;
import java.util.List;

public interface SearchProfile {

    interface View {
        void initSelectedProfileAdapter(ArrayList<FilterStateModel> filterList);
        void initCheckboxAdapter(ArrayList<FilterStateModel> filterList);
        void initTextEditView();
        void updateSelectedProfile(ArrayList<FilterStateModel> profileFilters);
        void updateCheckBoxFilter(ArrayList<FilterStateModel> checkBoxFilter);
        void updateCheckBoxFilterSearch(String s);
        void filterCheckBoxList(String s);
        void clearTextEditView();

    }

    interface Presenter {
        void attachView(SearchProfile.View view);
        void loadFilters(ArrayList<FilterStateModel> profileFilter);
        void onClickBackBtn(FragmentActivity activity);
        void onApplyButtonBtn(FragmentActivity activity);
        void onClearButtonBtn();
        void onTextChanged(String s);
        void removeSelectedProfile(int id, String search);
        void onClickCheckBox(int id, boolean isChecked);


    }
}
