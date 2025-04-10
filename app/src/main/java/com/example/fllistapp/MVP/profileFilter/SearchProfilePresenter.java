package com.example.fllistapp.MVP.profileFilter;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.example.fllistapp.model.FilterStateModel;
import java.util.ArrayList;
import javax.inject.Inject;

public class SearchProfilePresenter implements SearchProfile.Presenter {

    private SearchProfile.View view;
    private ArrayList<FilterStateModel> filterList;
    Bundle bundle = new Bundle();
    @Inject
    public SearchProfilePresenter(){}

    @Override
    public void attachView(SearchProfile.View view) {
        this.view = view;
    }

    @Override
    public void loadFilters(ArrayList<FilterStateModel> cityFilters) {

        filterList = cityFilters;

        view.initSelectedProfileAdapter(filterList);
        view.initCheckboxAdapter(filterList);
        view.initTextEditView();
    }

    @Override
    public void onClickBackBtn(FragmentActivity activity) {

    }

    @Override
    public void onApplyButtonBtn(FragmentActivity activity) {

    }


    @Override
    public void onClearButtonBtn() {
        for (FilterStateModel filter : filterList){
            filter.setSelected(false);
        }
        view.updateSelectedProfile(filterList);
        view.updateCheckBoxFilter(filterList);
        view.clearTextEditView();
    }

    @Override
    public void onTextChanged(String s) {
        view.filterCheckBoxList(s);
    }

    @Override
    public void removeSelectedProfile(int id, String search) {
        boolean updated = false;
        for (FilterStateModel filter : filterList) {
            if (filter.getId() == id) {
                filter.setSelected(false);
                updated = true;
                break;
            }
        }
        if (updated) {
            view.updateSelectedProfile(filterList);
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
            view.updateSelectedProfile(filterList);
            view.clearTextEditView();
        }
    }
}
