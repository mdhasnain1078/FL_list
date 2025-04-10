package com.example.fllistapp.MVP.allFilters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fllistapp.R;
import com.example.fllistapp.databinding.FragmentAllFilterBinding;
import com.example.fllistapp.model.FilterStateModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AllFilterFragment extends Fragment implements AllFilter.View {

    private FragmentAllFilterBinding binding;
    private TextInputLayout durationTxtLayout;
    private AutoCompleteTextView dropdownDuration;
    private FilterChipAdapter cityFilterAdapter;
    private FilterChipAdapter profileFilterAdapter;
    private ArrayList<FilterStateModel> profileFilters = new ArrayList<>();
    private ArrayList<FilterStateModel> cityFilters = new ArrayList<>();
    private int duration = 0;
    private ArrayList<FilterStateModel> originalProfileFilters = new ArrayList<>();
    private ArrayList<FilterStateModel> originalCityFilters = new ArrayList<>();
    private int originalDuration = 0;
    private ArrayList<String> durations = new ArrayList<>();

    @Inject
    AllFilter.Presenter presenter;

    public AllFilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            profileFilters = (ArrayList<FilterStateModel>) getArguments().getSerializable("profileFilters");
            cityFilters = (ArrayList<FilterStateModel>) getArguments().getSerializable("cityFilters");
            duration = getArguments().getInt("selectedDuration", 0);
            durations = (ArrayList<String>) getArguments().getSerializable("durations");

            // Create deep copies of the lists
            originalProfileFilters = new ArrayList<>();
            for (FilterStateModel filter : profileFilters) {
                originalProfileFilters.add(new FilterStateModel(filter));
            }

            originalCityFilters = new ArrayList<>();
            for (FilterStateModel filter : cityFilters) {
                originalCityFilters.add(new FilterStateModel(filter));
            }

            originalDuration = duration;
        }

        // Handle profile filter results
        getParentFragmentManager().setFragmentResultListener("profileKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                ArrayList<FilterStateModel> returnedFilters = (ArrayList<FilterStateModel>) bundle.getSerializable("profileFilterBack");
                if (returnedFilters != null) {
                    for (int i = 0; i < returnedFilters.size(); i++) {
                        profileFilters.get(i).setSelected(returnedFilters.get(i).isSelected());
                    }
                    if (binding != null) {
                        profileFilterAdapter.updateList(profileFilters);
                    }
                }
            }
        });

        // Handle city filter results
        getParentFragmentManager().setFragmentResultListener("cityKey", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                ArrayList<FilterStateModel> returnedFilters = (ArrayList<FilterStateModel>) bundle.getSerializable("cityFilterBack");
                if (returnedFilters != null) {
                    for (int i = 0; i < returnedFilters.size(); i++) {
                        cityFilters.get(i).setSelected(returnedFilters.get(i).isSelected());
                    }
                    if (binding != null) {
                        cityFilterAdapter.updateList(cityFilters);
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAllFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        durationTxtLayout = binding.durationTxtLayout;
        dropdownDuration = binding.dropdownDuration;

        binding.selectedExperienceCrossBtn.setOnClickListener(v -> {
            presenter.updateDurationFilter(0);
            dropdownDuration.setText("");
        });

        presenter.attachView(this);
        presenter.loadFilters(profileFilters, cityFilters, duration, durations);
        onClickInit();

        dropdownDuration.setOnItemClickListener((parent, view1, position, id) -> {
            String selectedValue = (String) parent.getItemAtPosition(position);
            presenter.updateDurationFilter(Integer.parseInt(selectedValue));
        });

        presenter.updateDurationFilter(duration);
    }

    public void onClickInit() {
        binding.addProfileBtn.setOnClickListener(v -> presenter.onClickProfile((FragmentActivity) requireActivity()));
        binding.addCityBtn.setOnClickListener(v -> presenter.onClickCity((FragmentActivity) requireActivity()));
        binding.backBtn.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putInt("selectedDuration", originalDuration);
            result.putSerializable("selectedProfiles", originalProfileFilters);
            result.putSerializable("selectedCities", originalCityFilters);
            result.putBoolean("isFilterApplied", true);
            getParentFragmentManager().setFragmentResult("filterResult", result);
            requireActivity().onBackPressed();
        });

        binding.clearBtn.setOnClickListener(v -> presenter.onClearButtonBtn());

        binding.applyBtn.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putInt("selectedDuration", duration);
            result.putSerializable("selectedProfiles", profileFilters);
            result.putSerializable("selectedCities", cityFilters);
            result.putBoolean("isFilterApplied", true);
            getParentFragmentManager().setFragmentResult("filterResult", result);
            requireActivity().onBackPressed();
        });
    }

    @Override
    public void showProfileFilter() {

    }

    @Override
    public void hideProfileFilter() {

    }

    @Override
    public void showCityFilter() {

    }

    @Override
    public void hideCityFilter() {

    }

    @Override
    public void initProfileFilter(ArrayList<FilterStateModel> profileFilter) {
        binding.profileRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        profileFilterAdapter = new FilterChipAdapter(getContext(), profileFilter, this::removeProfileItem);
        binding.profileRecyclerView.setAdapter(profileFilterAdapter);
    }

    @Override
    public void initCityFilter(ArrayList<FilterStateModel> cityFilter) {
        binding.cityRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        cityFilterAdapter = new FilterChipAdapter(getContext(), cityFilter, this::removeCityItem);
        binding.cityRecyclerView.setAdapter(cityFilterAdapter);
    }

    @Override
    public void setDropdown(ArrayList<String> durations) {
        if (durations == null || durations.isEmpty()) {
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                R.layout.dropdown_menu_popup_item, durations);


        dropdownDuration.setAdapter(adapter);

        dropdownDuration.setThreshold(10000);
        if (duration > 0) {
            dropdownDuration.setText(String.valueOf(duration), false);
        }
    }

    @Override
    public void updateProfileFilter(ArrayList<FilterStateModel> profileFilters) {
        profileFilterAdapter.updateList(profileFilters);
    }

    @Override
    public void updateCityFilter(ArrayList<FilterStateModel> cityFilters) {
        cityFilterAdapter.updateList(cityFilters);
    }

    @Override
    public void updateDurationFilter(int newDuration, String mon) {
        duration = newDuration;
        binding.selectedDuration.setVisibility(newDuration > 0 ? View.VISIBLE : View.GONE);
        binding.selectedDurationTxt.setText(newDuration > 0 ? newDuration + mon : "");
        dropdownDuration.setText(newDuration > 0 ? String.valueOf(newDuration) : "", true);
    }

    private void removeProfileItem(int id) {
        presenter.removeProfile(id);
    }

    private void removeCityItem(int id) {
        presenter.removeCity(id);
    }
}
