package com.example.fllistapp.MVP.internshipList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fllistapp.MVP.allFilters.AllFilterFragment;
import com.example.fllistapp.databinding.FragmentInternshipListBinding;
import com.example.fllistapp.model.FilterStateModel;
import com.example.fllistapp.model.Internship;
import com.example.fllistapp.utils.FragmentUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class InternshipListFragment extends Fragment implements internshipList.View {
    private FragmentInternshipListBinding binding;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private InternshipAdapter adapter;
    private AppliedFiltersAdapter appliedFiltersAdapter;
    private boolean isFilterApplied = false;
    int selectedDurationFilter = 0;
    @Inject
    internshipList.Presenter presenter;
    private TextView totalInternships;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInternshipListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.internshipRecyclerView;
        progressBar = binding.loader;
        totalInternships = binding.totalInternship;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.attachView(this);
        presenter.loadInternships(false);

        binding.filterBtn.setOnClickListener(v -> {
            presenter.onClickFilterBtn((FragmentActivity) requireActivity(), isFilterApplied);
        });

        binding.filterBtnChipCounted.setOnClickListener(v->{
            presenter.onClickFilterBtn((FragmentActivity) requireActivity(), isFilterApplied);
        });

        binding.notFoundTxt.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showInternships(ArrayList<Internship> internships) {
        adapter = new InternshipAdapter(getContext(), internships);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showError(String message) {
        binding.notFoundTxt.setVisibility(View.VISIBLE);
        binding.notFoundTxt.setText(message);
    }

    @Override
    public void updateInternshipCount(int count) {
        totalInternships.setText(String.valueOf(count) + " total opportunities");
        if (count <= 0){
            binding.notFoundTxt.setVisibility(View.VISIBLE);
            binding.notFoundTxt.setText("Internship not found");
        }else {
            binding.notFoundTxt.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClickFilterBtn(ArrayList<FilterStateModel> profileFilters, ArrayList<FilterStateModel> cityFilters, int duration, ArrayList<String> durations ) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("profileFilters", profileFilters);
        bundle.putSerializable("cityFilters", cityFilters);
        bundle.putSerializable("durations", durations);
        System.out.println("durationData at frag: " + durations);
        bundle.putInt("selectedDuration", duration);

        AllFilterFragment fragment = new AllFilterFragment();
        fragment.setArguments(bundle);
        FragmentUtils.loadFragment(requireActivity(), fragment, true, bundle);
    }

    @Override
    public void applyFilterCall() {
        getParentFragmentManager().setFragmentResultListener("filterResult", getViewLifecycleOwner(), (requestKey, bundle) -> {
            isFilterApplied = bundle.getBoolean("isFilterApplied", false);

            if (isFilterApplied) {
                ArrayList<FilterStateModel> selectedProfiles = (ArrayList<FilterStateModel>) bundle.getSerializable("selectedProfiles");
                ArrayList<FilterStateModel> selectedCities = (ArrayList<FilterStateModel>) bundle.getSerializable("selectedCities");
                selectedDurationFilter = bundle.getInt("selectedDuration", 0);

                if (selectedProfiles != null && selectedCities != null) {
                    System.out.println("Applying filters from fragment result");
                    presenter.applyFilters(selectedProfiles, selectedCities, selectedDurationFilter);
                }
            } else {
                System.out.println("Loading all internships - filter not applied");
                selectedDurationFilter = 0;
                presenter.loadInternships(false);
            }
        });
    }

    @Override
    public void setAppliedFilters(ArrayList<FilterStateModel> filters, int filterCount) {
        binding.appliedFiltersList.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        appliedFiltersAdapter = new AppliedFiltersAdapter(requireContext(), filters, this::removeFilterItem);
        binding.appliedFiltersList.setAdapter(appliedFiltersAdapter);
        binding.filterCountTxt.setText(String.valueOf(filterCount));
        binding.filtersList.setVisibility(filterCount > 0 ? View.VISIBLE : View.GONE);
        binding.filterBtn.setVisibility(filterCount > 0? View.GONE : View.VISIBLE);
        binding.appliedFiltersList.setVisibility(filterCount > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void callAppliedFilters() {
        presenter.setAppliedFilters();
    }

    @Override
    public void updateAppliedFilters(int filterCount) {
        binding.filtersList.setVisibility(filterCount > 0 ? View.VISIBLE : View.GONE);
        binding.filterBtn.setVisibility(filterCount > 0? View.GONE : View.VISIBLE);
    }

    private void removeFilterItem(int id) {
        presenter.removeFilter(id);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        binding.contentContainer.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
        binding.contentContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}



