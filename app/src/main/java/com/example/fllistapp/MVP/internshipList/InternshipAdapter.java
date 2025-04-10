package com.example.fllistapp.MVP.internshipList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fllistapp.databinding.InternshipItemBinding;
import com.example.fllistapp.model.Internship;

import java.util.ArrayList;

public class InternshipAdapter extends RecyclerView.Adapter<InternshipAdapter.InternshipViewHolder> {

    private Context context;
    private ArrayList<Internship> internshipList;

    public InternshipAdapter(Context context, ArrayList<Internship> internshipList) {
        this.context = context;
        this.internshipList = internshipList;
    }

    public void updateList(ArrayList<Internship> newInternshipList) {
        this.internshipList = newInternshipList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public InternshipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        InternshipItemBinding binding = InternshipItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new InternshipViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InternshipViewHolder holder, int position) {
        Internship internship = internshipList.get(position);

        setTextOrHide(holder.binding.title, internship.title);
        setTextOrHide(holder.binding.companyName, internship.companyName);

        if (internship.locationNames != null && internship.locationNames.length > 0) {
            holder.binding.locationName.setText(String.join(", ", internship.locationNames));
            holder.binding.locationName.setVisibility(View.VISIBLE);
        } else {
            holder.binding.locationName.setVisibility(View.GONE);
        }

        setTextOrHide(holder.binding.startDateTxt, internship.startDate);
        setTextOrHide(holder.binding.DurationTxt, internship.duration);

        if (internship.stipend != null && internship.stipend.salary != null && !internship.stipend.salary.isEmpty()) {
            holder.binding.StipendTxt.setText(internship.stipend.salary);
            holder.binding.StipendTxt.setVisibility(View.VISIBLE);
        } else {
            holder.binding.StipendTxt.setVisibility(View.GONE);
        }

        holder.binding.partTimeTxt.setVisibility(internship.partTime ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return internshipList.size();
    }

    public static class InternshipViewHolder extends RecyclerView.ViewHolder {
        private final InternshipItemBinding binding;

        public InternshipViewHolder(@NonNull InternshipItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void setTextOrHide(View view, String text) {
        if (text != null && !text.isEmpty()) {
            ((android.widget.TextView) view).setText(text);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
}

