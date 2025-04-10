package com.example.fllistapp.di;

import com.example.fllistapp.MVP.allFilters.AllFilter;
import com.example.fllistapp.MVP.allFilters.AllFilterPresenter;
import com.example.fllistapp.MVP.profileFilter.SearchProfile;
import com.example.fllistapp.MVP.profileFilter.SearchProfilePresenter;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;

@Module
@InstallIn(FragmentComponent.class)
public class AllFilterModule {

    @Provides
    public static AllFilter.Presenter provideAllFilterPresenter() {
        return new AllFilterPresenter();
    }
}


