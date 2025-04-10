package com.example.fllistapp.MVP.profileFilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fllistapp.databinding.ListItemBinding;
import com.example.fllistapp.model.FilterStateModel;
import java.util.List;
import java.util.ArrayList;

public class CheckboxFilterAdapter extends RecyclerView.Adapter<CheckboxFilterAdapter.ViewHolder> {
    private final Context context;
    private ArrayList<FilterStateModel> originalList; // Stores the full unfiltered list
    private ArrayList<FilterStateModel> filteredList; // Stores the filtered list
    private final OnFilterCheckListener checkListener;

    public CheckboxFilterAdapter(Context context, List<FilterStateModel> filterList, OnFilterCheckListener checkListener) {
        this.context = context;
        this.originalList = new ArrayList<>(filterList); // Keep a copy of the original list
        this.filteredList = new ArrayList<>(filterList);
        this.checkListener = checkListener;
    }

    public void updateList(List<FilterStateModel> newFilterList) {
        this.originalList = new ArrayList<>(newFilterList);
        this.filteredList = new ArrayList<>(newFilterList);
        notifyDataSetChanged();
    }

    public void filterList(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (FilterStateModel item : originalList) {
                if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ListItemBinding binding = ListItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FilterStateModel filter = filteredList.get(position);
        holder.binding.chkText.setText(filter.getTitle());

        // Avoid triggering listener during binding
        holder.binding.chkBox.setOnCheckedChangeListener(null);
        holder.binding.chkBox.setChecked(filter.isSelected());

        // Handle checkbox toggle
        holder.binding.chkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (filter.isSelected() != isChecked) {
                filter.setSelected(isChecked);
                checkListener.onFilterChecked(filter.getId(), isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ListItemBinding binding;

        public ViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface OnFilterCheckListener {
        void onFilterChecked(int id, boolean isChecked);
    }
}
