package com.example.fllistapp.MVP.internshipList;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.example.fllistapp.di.Interfaces.InternshipRepositoryInter;
import com.example.fllistapp.model.FilterStateModel;
import com.example.fllistapp.model.Internship;
import com.example.fllistapp.repository.InternshipRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.inject.Inject;

public class InternshipListPresenter implements internshipList.Presenter {
    private final InternshipRepositoryInter repository;
    private internshipList.View view;
    private ArrayList<Internship> allInternships;
    private ArrayList<Internship> filteredInternships;
    ArrayList<FilterStateModel> setProfileFilters = new ArrayList<FilterStateModel>();
    ArrayList<FilterStateModel> setCityFilters = new ArrayList<FilterStateModel>();
    ArrayList<FilterStateModel> appliedFilters = new ArrayList<FilterStateModel>();
    int selectedDuration = 0;
    int duration = 0;
    int filterCount = 0;
    int idCounter = 1;
    Bundle bundle = new Bundle();
    @Inject
    public InternshipListPresenter(InternshipRepositoryInter repository) {
        this.repository = repository;
    }

    @Override
    public void attachView(internshipList.View view) {
        this.view = view;
    }

    @Override
    public void loadInternships(boolean isReload) {
        if (view != null) {
            view.showLoading();
        }

        repository.fetchInternships(new InternshipRepository.InternshipCallback() {
            @Override
            public void onSuccess(ArrayList<Internship> internships) {
                if (view != null) {
                    view.hideLoading();
                    allInternships = internships;
                    filteredInternships = null;
                    view.showInternships(internships);
                    view.updateInternshipCount(internships.size());
                    if(!isReload){
                        view.applyFilterCall();
                    }else{
                        applyFilters(setProfileFilters, setCityFilters, selectedDuration);
                    }
                    view.callAppliedFilters();
                }
            }

            @Override
            public void onError(String error) {
                if (view != null) {
                    view.hideLoading();
                    view.showError(error);
                }
            }
        });
    }



    @Override
    public void applyFilters(ArrayList<FilterStateModel> profileFilters, ArrayList<FilterStateModel> cityFilters, int duration) {
        if (allInternships == null || view == null) {
            System.out.println("Filter Error: allInternships or view is null");
            return;
        }

//        boolean matchesProfile = false;
//        boolean matchesCity = false;
//        boolean matchesDuration = false;

        setProfileFilters = profileFilters;

        setCityFilters = cityFilters;

        selectedDuration = duration;

        Set<String> selectedProfiles = new HashSet<>();
        Set<String> selectedCities = new HashSet<>();

        for (FilterStateModel profile : profileFilters) {
            if (profile.isSelected()) {
                selectedProfiles.add(profile.getTitle());
//                System.out.println("Selected Profile: " + profile.getTitle());
            }
        }

        for (FilterStateModel city : cityFilters) {
            if (city.isSelected()) {
                selectedCities.add(city.getTitle());
            }
        }

        filteredInternships = new ArrayList<>();
        for (Internship internship : allInternships) {
            boolean matchesProfile = selectedProfiles.isEmpty() || selectedProfiles.contains(internship.title);
            if (matchesProfile == false) continue;
            boolean matchesCity = selectedCities.isEmpty() || hasMatchingCity(internship.locationNames, selectedCities);
            if (matchesProfile == false) continue;
            boolean matchesDuration = duration == 0 || matchesDuration(internship.duration, duration);
            if (matchesProfile == false) continue;

            if (matchesProfile && matchesCity && matchesDuration) {
                filteredInternships.add(internship);
            }
        }
        view.showInternships(filteredInternships);
        view.updateInternshipCount(filteredInternships.size());
    }

    @Override
    public void setAppliedFilters() {

        appliedFilters.clear();

        FilterStateModel durationFilter;

        filterCount = 0;

        for (FilterStateModel filter : setProfileFilters) {
            appliedFilters.add(filter);
            if (filter.isSelected()){
                filterCount++;
            }
            System.out.println("ID: " + filter.getId());
        }

        for (FilterStateModel filter : setCityFilters) {
            appliedFilters.add(filter);
            if (filter.isSelected() == true){
                filterCount++;
            }
            System.out.println("ID: " + filter.getId());
        }

            if (selectedDuration > 0){
                filterCount++;
                String d = "Duration <= "+ selectedDuration;
                durationFilter = new FilterStateModel(100000, d, true);
                appliedFilters.add(durationFilter);
            }
        view.setAppliedFilters(appliedFilters, filterCount);

    }

    @Override
    public void removeFilter(int id) {

        filterCount = 0;

        FilterStateModel durationFilter;

        for (FilterStateModel filter : setProfileFilters) {

            if (filter.isSelected()){
                filterCount++;
            }

            if (id == filter.getId()){
                filter.setSelected(false);
                filterCount--;
            }
        }

        for (FilterStateModel filter : setCityFilters) {

            if (filter.isSelected()){
                filterCount++;
            }

            if (id == filter.getId()){
                filter.setSelected(false);
                filterCount--;
            }
        }

        appliedFilters.clear();

        appliedFilters.addAll(setProfileFilters);
        appliedFilters.addAll(setCityFilters);

        if (id == 100000) {
            selectedDuration = 0;
            filterCount--;
            appliedFilters.removeIf(filter -> filter.getId() == 100000);
        }

        if (selectedDuration > 0){
            filterCount++;
            String d = "Duration <= "+ selectedDuration;
            durationFilter = new FilterStateModel(100000, d, true);
            appliedFilters.add(durationFilter);
        }

        applyFilters(setProfileFilters, setCityFilters, selectedDuration);

        view.callAppliedFilters();

    }


    private boolean hasMatchingCity(String[] locationNames, Set<String> selectedCities) {
        if (locationNames == null) return false;
        for (String location : locationNames) {
            if (selectedCities.contains(location)) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesDuration(String durationStr, int targetDuration) {
        if (durationStr == null || durationStr.isEmpty()) {
            System.out.println("Duration string is null or empty");
            return false;
        }
        try {
            String numStr = durationStr.replaceAll("[^0-9]", "");
            int duration = Integer.parseInt(numStr);
            return duration <= targetDuration;
        } catch (NumberFormatException e) {
            System.out.println("Failed to parse duration: " + durationStr);
            return false;
        }
    }

    @Override
    public void onClickFilterBtn(FragmentActivity activity, boolean isFilterApplied) {
        appliedFilters.clear();

        ArrayList<String> durationData = new ArrayList<>(getDurationFilters(allInternships));

        if(!isFilterApplied){
            ArrayList<FilterStateModel> profileFilters = new ArrayList<>(getProfileFilters(allInternships));
            ArrayList<FilterStateModel> cityFilters = new ArrayList<>(getCityFilters(allInternships));
            view.onClickFilterBtn(profileFilters, cityFilters, selectedDuration, durationData);
        }else{
            view.onClickFilterBtn(setProfileFilters, setCityFilters, selectedDuration, durationData);
        }
    }

    private ArrayList<String> getDurationFilters(ArrayList<Internship> internships) {
        Set<String> uniqueDurations = new HashSet<>();
        for (Internship internship : internships) {
            if (internship.duration != null && !internship.duration.isEmpty()) {
                // Extract numeric duration value from the string
                String durationStr = internship.duration.replaceAll("[^0-9]", "");
                if (!durationStr.isEmpty()) {
                    uniqueDurations.add(durationStr);
                }
            }
        }
        ArrayList<String> durationList = new ArrayList<>(uniqueDurations);
        Collections.sort(durationList, (a, b) -> Integer.parseInt(a) - Integer.parseInt(b));
        return durationList;
    }

    public ArrayList<FilterStateModel> getProfileFilters(ArrayList<Internship> internships) {
        Set<String> uniqueProfiles = new HashSet<>();
        ArrayList<FilterStateModel> profileFilters = new ArrayList<>();


        for (Internship internship : internships) {
            if (internship.title != null && uniqueProfiles.add(internship.title)) {
                profileFilters.add(new FilterStateModel(idCounter++, internship.title, false));
            }
        }
        return profileFilters;
    }

    public ArrayList<FilterStateModel> getCityFilters(ArrayList<Internship> internships) {
        Set<String> uniqueCities = new HashSet<>();
        ArrayList<FilterStateModel> cityFilters = new ArrayList<>();

        for (Internship internship : internships) {
            if (internship.locationNames != null) {
                for (String city : internship.locationNames) {
                    if (city != null && uniqueCities.add(city)) {
                        cityFilters.add(new FilterStateModel(idCounter++, city, false));
                    }
                }
            }
        }
        return cityFilters;
    }

}
