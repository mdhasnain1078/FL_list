package com.example.fllistapp.di;
import com.example.fllistapp.MVP.cityFilter.SearchCity;
import com.example.fllistapp.MVP.cityFilter.SearchCityPresenter;
import com.example.fllistapp.MVP.cityFilter.SearchCityPresenter_Factory;
import com.example.fllistapp.MVP.profileFilter.SearchProfile;
import com.example.fllistapp.MVP.profileFilter.SearchProfilePresenter;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;

@Module
@InstallIn(FragmentComponent.class)
public class CityFilterModule {
    @Provides
    public static SearchCity.Presenter provideCityFilterPresenter() {
        return new SearchCityPresenter();
    }

} 