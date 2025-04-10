package com.example.fllistapp.MVP.profileFilter;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.fllistapp.MVP.allFilters.AllFilterPresenter;
import com.example.fllistapp.MVP.cityFilter.SelectedCityFilterChipAdapter;
import com.example.fllistapp.databinding.FragmentSearchCityBinding;
import com.example.fllistapp.databinding.FragmentSearchProfileBinding;
import com.example.fllistapp.model.FilterStateModel;
import com.example.fllistapp.utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchProfileFragment extends Fragment implements SearchProfile.View {
    private FragmentSearchProfileBinding binding;
    private ArrayList<FilterStateModel> filterList;
    private CheckboxFilterAdapter adapter;
    private SelectedProfileFilterChip profileAdapter;

    public SearchProfileFragment() {}
    String search = "";
    @Inject
    SearchProfile.Presenter presenter;
    private ArrayList<FilterStateModel> originalFilterList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSearchProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            filterList = (ArrayList<FilterStateModel>) getArguments().getSerializable("profileFilters");
            // Store original state
            originalFilterList = new ArrayList<>();
            for (FilterStateModel filter : filterList) {
                originalFilterList.add(new FilterStateModel(filter));
            }
        }

        presenter.attachView(this);
        presenter.loadFilters(filterList);

        binding.backBtn.setOnClickListener(v -> {
            ArrayList<FilterStateModel> deepCopyBack = new ArrayList<>();
            for (FilterStateModel filter : originalFilterList) {
                deepCopyBack.add(new FilterStateModel(filter));
            }
            Bundle result = new Bundle();
            result.putSerializable("profileFilterBack", deepCopyBack);
            getParentFragmentManager().setFragmentResult("profileKey", result);
            FragmentUtils.goBack((FragmentActivity) requireActivity());
        });

        binding.clearBtn.setOnClickListener(v -> {
            presenter.onClearButtonBtn();
        });

        binding.applyBtn.setOnClickListener(v -> {
            ArrayList<FilterStateModel> deepCopyBack = new ArrayList<>();
            for (FilterStateModel filter : filterList) {
                deepCopyBack.add(new FilterStateModel(filter));
            }
            Bundle result = new Bundle();
            result.putSerializable("profileFilterBack", deepCopyBack);
            getParentFragmentManager().setFragmentResult("profileKey", result);
            FragmentUtils.goBack((FragmentActivity) requireActivity());
        });

        binding.profileEditTxt.requestFocus();
        binding.profileEditTxt.postDelayed(() -> {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(binding.profileEditTxt, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);


    }

    @Override
    public void initSelectedProfileAdapter(ArrayList<FilterStateModel> filterList) {
        profileAdapter = new SelectedProfileFilterChip(getContext(), filterList, this::removeProfileItem);
        binding.selectedProfileRecycleView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.selectedProfileRecycleView.setAdapter(profileAdapter);
    }

    @Override
    public void initCheckboxAdapter(ArrayList<FilterStateModel> filterList) {
        adapter = new CheckboxFilterAdapter(requireContext(), filterList, this::onFilterChecked);
        binding.profilesRecycleView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.profilesRecycleView.setAdapter(adapter);
    }


    private void removeProfileItem(int id) {
        presenter.removeSelectedProfile(id, search);
    }


    private void onFilterChecked(int id, boolean isChecked){
        presenter.onClickCheckBox(id, isChecked);
    }

    @Override
    public void initTextEditView() {
        binding.profileEditTxt.addTextChangedListener(new TextWatcher() {
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
    public void updateSelectedProfile(ArrayList<FilterStateModel> profileFilters) {
        profileAdapter.updateList(profileFilters);
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
        binding.profileEditTxt.setText("");
    }

}