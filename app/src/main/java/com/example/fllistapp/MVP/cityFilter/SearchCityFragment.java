package com.example.fllistapp.MVP.cityFilter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.fllistapp.MVP.profileFilter.CheckboxFilterAdapter;
import com.example.fllistapp.MVP.profileFilter.SearchProfilePresenter;
import com.example.fllistapp.MVP.profileFilter.SelectedProfileFilterChip;
import com.example.fllistapp.databinding.FragmentSearchCityBinding;
import com.example.fllistapp.databinding.FragmentSearchProfileBinding;
import com.example.fllistapp.model.FilterStateModel;
import com.example.fllistapp.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchCityFragment extends Fragment implements SearchCity.View{

    private FragmentSearchCityBinding binding;
    private CityCheckBoxFilterAdapter adapter;
    private SelectedCityFilterChipAdapter cityAdapter;

    private ArrayList<FilterStateModel> cityFilters;
    private ArrayList<FilterStateModel> originalCityFilters;
    String search = "";
    Bundle bundle = new Bundle();

    public SearchCityFragment() {}

    @Inject
    SearchCity.Presenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchCityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            cityFilters = (ArrayList<FilterStateModel>) getArguments().getSerializable("cityFilters");
            originalCityFilters = new ArrayList<>();
            for (FilterStateModel filter : cityFilters) {
                originalCityFilters.add(new FilterStateModel(filter));
            }
        }

        presenter.attachView(this);
        presenter.loadFilters(cityFilters);

        binding.backBtn.setOnClickListener(v -> {
            Bundle result = new Bundle();
            result.putSerializable("cityFilterBack", originalCityFilters);
            getParentFragmentManager().setFragmentResult("cityKey", result);
            FragmentUtils.goBack((FragmentActivity) requireActivity());
        });

        binding.clearBtn.setOnClickListener(v -> {
            presenter.onClearButtonBtn();
        });

        binding.applyBtn.setOnClickListener(v -> {
            // On apply, return modified data
            ArrayList<FilterStateModel> deepCopyBack = new ArrayList<>();
            for (FilterStateModel filter : cityFilters) {
                deepCopyBack.add(new FilterStateModel(filter));
            }
            Bundle result = new Bundle();
            result.putSerializable("cityFilterBack", deepCopyBack);
            getParentFragmentManager().setFragmentResult("cityKey", result);
            FragmentUtils.goBack((FragmentActivity) requireActivity());
        });

        binding.cityEditTxt.requestFocus();
        binding.cityEditTxt.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.cityEditTxt, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    @Override
    public void initSelectedCityAdapter(ArrayList<FilterStateModel> filterList) {
        cityAdapter = new SelectedCityFilterChipAdapter(getContext(), filterList, this::removeCityItem);
        binding.selectedCityRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.selectedCityRecycleView.setAdapter(cityAdapter);
    }

    @Override
    public void initCheckboxAdapter(ArrayList<FilterStateModel> filterList) {
        adapter = new CityCheckBoxFilterAdapter(requireContext(), filterList, this::onFilterChecked);
        binding.citiesRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.citiesRecycleView.setAdapter(adapter);
    }

    private void removeCityItem(int id) {
        presenter.removeSelectedCity(id, search);
    }


    private void onFilterChecked(int id, boolean isChecked){
        presenter.onClickCheckBox(id, isChecked);
    }

    @Override
    public void initTextEditView() {
        binding.cityEditTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search = s.toString();
                presenter.onTextChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void updateSelectedCity(ArrayList<FilterStateModel> profileFilters) {
        cityAdapter.updateList(profileFilters);
    }

    @Override
    public void updateCheckBoxFilter(ArrayList<FilterStateModel> checkBoxFilter) {
        adapter.updateList(checkBoxFilter);
    }

    @Override
    public void updateCheckBoxFilterSearch(String s) {
        adapter.filterList(s);
    }

    @Override
    public void filterCheckBoxList(String s) {
        adapter.filterList(s.toString());
    }

    @Override
    public void clearTextEditView() {
        binding.cityEditTxt.setText("");
    }
}


