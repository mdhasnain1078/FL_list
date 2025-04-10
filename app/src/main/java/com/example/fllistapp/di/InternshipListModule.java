package com.example.fllistapp.di;

import com.example.fllistapp.MVP.internshipList.InternshipListPresenter;
import com.example.fllistapp.MVP.internshipList.internshipList;
import com.example.fllistapp.MVP.profileFilter.SearchProfile;
import com.example.fllistapp.MVP.profileFilter.SearchProfilePresenter;
import com.example.fllistapp.model.Internship;
import com.example.fllistapp.repository.InternshipRepository;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.FragmentComponent;
import javax.inject.Singleton;

@Module
@InstallIn(FragmentComponent.class)
public class InternshipListModule {

    @Provides
    public static internshipList.Presenter provideInternshipListPresenter(InternshipRepository repository) {
        return new InternshipListPresenter(repository);
    }
}
