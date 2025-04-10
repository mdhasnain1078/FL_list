package com.example.fllistapp.MVP.cityFilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fllistapp.databinding.SelectedChipBinding;
import com.example.fllistapp.model.FilterStateModel;

import java.util.ArrayList;
import java.util.List;




public class SelectedCityFilterChipAdapter extends RecyclerView.Adapter<SelectedCityFilterChipAdapter.ViewHolder> {
    private final Context context;
    private List<FilterStateModel> filterList;
    private FilterListListener listListener;

    public SelectedCityFilterChipAdapter(Context context, List<FilterStateModel> filterList, FilterListListener listListener) {
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
    public SelectedCityFilterChipAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SelectedChipBinding binding = SelectedChipBinding.inflate(LayoutInflater.from(context), parent, false);
        return new SelectedCityFilterChipAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(SelectedCityFilterChipAdapter.ViewHolder holder, int position) {
        FilterStateModel filter = filterList.get(position);
        holder.binding.txt.setText(filter.getTitle());

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
        private final SelectedChipBinding binding;

        public ViewHolder(@NonNull SelectedChipBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

// Interface for handling removal actions
interface FilterListListener {
    void onItemRemoved(int id);
}

