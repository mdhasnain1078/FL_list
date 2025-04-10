package com.example.fllistapp.MVP.cityFilter;

import androidx.fragment.app.FragmentActivity;
import com.example.fllistapp.model.FilterStateModel;
import java.util.ArrayList;
import javax.inject.Inject;

public class SearchCityPresenter implements SearchCity.Presenter{

    private SearchCity.View view;
    private ArrayList<FilterStateModel> flInternships;
    private ArrayList<FilterStateModel> filterList;
    @Inject
    public SearchCityPresenter(){}


    @Override
    public void attachView(SearchCity.View view) {
        this.view = view;
    }

    @Override
    public void loadFilters(ArrayList<FilterStateModel> cityFilters) {

        filterList = cityFilters;
        view.initSelectedCityAdapter(filterList);
        view.initCheckboxAdapter(filterList);
        view.initTextEditView();
    }

    @Override
    public void onClickBackBtn(FragmentActivity activity) {

    }

    @Override
    public void onApplyButtonBtn() {

    }

    @Override
    public void onClearButtonBtn() {
        for (FilterStateModel filter : filterList){
            filter.setSelected(false);
        }
        view.updateSelectedCity(filterList);
        view.updateCheckBoxFilter(filterList);
        view.clearTextEditView();
    }

    @Override
    public void onTextChanged(String s) {
        view.filterCheckBoxList(s);
    }

    @Override
    public void removeSelectedCity(int id, String search) {
        boolean updated = false;
        for (FilterStateModel filter : filterList) {
            if (filter.getId() == id) {
                filter.setSelected(false);
                updated = true;
                break;
            }
        }
        if (updated) {
            view.updateSelectedCity(filterList);
            view.updateCheckBoxFilter(filterList);
            view.updateCheckBoxFilterSearch(search);
        }
    }

    @Override
    public void onClickCheckBox(int id, boolean isChecked) {
        boolean updated = false;
        for (FilterStateModel filter : filterList) {
            if (filter.getId() == id) {
                filter.setSelected(isChecked);
                updated = true;
                break;
            }
        }
        if (updated) {
            view.updateCheckBoxFilter(filterList);
            view.updateSelectedCity(filterList);
            view.clearTextEditView();
        }
    }
}
