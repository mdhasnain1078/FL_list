package com.example.fllistapp.MVP.internshipList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fllistapp.databinding.FilterChipItemBinding;
import com.example.fllistapp.databinding.SelectedChipBinding;
import com.example.fllistapp.model.FilterStateModel;

import java.util.ArrayList;
import java.util.List;

public class AppliedFiltersAdapter extends RecyclerView.Adapter<AppliedFiltersAdapter.ViewHolder> {
    private final Context context;
    private List<FilterStateModel> filterList;
    private final FilterListListener listListener;
    public AppliedFiltersAdapter(Context context, List<FilterStateModel> filterList, FilterListListener listListener) {
        this.context = context;
        this.filterList = filterList;
        this.listListener = listListener;
        updateList(filterList);
    }

    public void updateList(List<FilterStateModel> newFilterList) {
        this.filterList = new ArrayList<>();
        for (FilterStateModel filter : newFilterList) {
            if (filter.isSelected()) {
                this.filterList.add(filter);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppliedFiltersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FilterChipItemBinding binding = FilterChipItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new AppliedFiltersAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(AppliedFiltersAdapter.ViewHolder holder, int position) {
        FilterStateModel filter = filterList.get(position);
        holder.binding.filterTxt.setText(filter.getTitle());

        // Remove chip when "X" button is clicked
        holder.binding.crossBtn.setOnClickListener(v -> {
            filter.setSelected(false);
            listListener.onItemRemoved(filter.getId());
        });
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final FilterChipItemBinding binding;

        public ViewHolder(@NonNull FilterChipItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

// Interface for handling removal actions
interface FilterListListener {
    void onItemRemoved(int id);
}

