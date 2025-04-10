package com.example.fllistapp.MVP.allFilters;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.example.fllistapp.MVP.cityFilter.SearchCityFragment;
import com.example.fllistapp.MVP.profileFilter.SearchProfileFragment;
import com.example.fllistapp.model.FilterStateModel;
import com.example.fllistapp.utils.FragmentUtils;
import java.util.ArrayList;
import javax.inject.Inject;

public class AllFilterPresenter implements AllFilter.Presenter{

    @Inject
    public AllFilterPresenter(){}
    private AllFilter.View view;

    ArrayList<FilterStateModel> filtersCity;

    ArrayList<FilterStateModel> filtersProfile;
    ArrayList<String> durationsData;

    Bundle bundle = new Bundle();
    int selectedDuration = 0;
    @Override
    public void attachView(AllFilter.View view) {
        this.view = view;
    }

    @Override
    public void loadFilters(ArrayList<FilterStateModel> profileFilters, ArrayList<FilterStateModel> cityFilters, int duration, ArrayList<String> durations) {

        filtersCity = cityFilters;
        filtersProfile = profileFilters;
        durationsData = durations;
        selectedDuration = duration;

        view.setDropdown(durationsData);

        view.initProfileFilter(filtersProfile);
        view.initCityFilter(filtersCity);
    }

    @Override
    public void removeProfile(int id) {
        boolean updated = false;
        for (FilterStateModel filter : filtersProfile) {
            if (filter.getId() == id) {
                filter.setSelected(false);
                updated = true;
                break;
            }
        }
        if (updated) {
            view.updateProfileFilter(filtersProfile);
        }
    }

    @Override
    public void removeCity(int id) {
        boolean updated = false;
        for (FilterStateModel filter : filtersCity) {
            if (filter.getId() == id) {
                filter.setSelected(false);
                updated = true;
                break;
            }
        }
        if (updated) {
            view.updateCityFilter(filtersCity);
        }
    }



    @Override
    public void updateDurationFilter(int duration) {
        String mon = " months";
        if (duration > 1){
            mon = " months";
        }else{
            mon = " month";
        }
        view.updateDurationFilter(duration, mon);
    }

    @Override
    public void onClickProfile(FragmentActivity activity) {
        ArrayList<FilterStateModel> deepCopyProfileFilters = new ArrayList<>();
        for (FilterStateModel filter : filtersProfile) {
            deepCopyProfileFilters.add(new FilterStateModel(filter));
        }
        
        bundle.putSerializable("profileFilters", deepCopyProfileFilters);
        SearchProfileFragment fragment = new SearchProfileFragment();
        fragment.setArguments(bundle);
        FragmentUtils.loadFragment(activity, fragment, true, bundle);
    }

    @Override
    public void onClickCity(FragmentActivity activity) {
        ArrayList<FilterStateModel> deepCopyCityFilters = new ArrayList<>();
        for (FilterStateModel filter : filtersCity) {
            deepCopyCityFilters.add(new FilterStateModel(filter));
        }
        
        bundle.putSerializable("cityFilters", deepCopyCityFilters);
        SearchCityFragment fragment = new SearchCityFragment();
        fragment.setArguments(bundle);
        FragmentUtils.loadFragment(activity, fragment, true, bundle);
    }

    @Override
    public void onClickBack(FragmentActivity activity) {
        FragmentUtils.goBack(activity);
    }

    @Override
    public void onApplyButtonBtn(FragmentActivity activity) {

    }

    @Override
    public void onClearButtonBtn() {
        for (FilterStateModel filter : filtersProfile){
            filter.setSelected(false);
        }

        for (FilterStateModel filter : filtersCity){
            filter.setSelected(false);
        }

        view.updateProfileFilter(filtersProfile);
        view.updateCityFilter(filtersCity);
        view.updateDurationFilter(0, "mon");
    }

}
