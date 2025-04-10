package com.example.fllistapp.model;

import java.io.Serializable;

public class FilterStateModel implements Serializable {
    private int id;
    private String title;
    private boolean isSelected;

    // Constructor with explicit isSelected parameter
    public FilterStateModel(int id, String title, boolean isSelected) {
        this.id = id;
        this.title = title;
        this.isSelected = isSelected;
    }

    // Constructor with default isSelected value (false)
    public FilterStateModel(int id, String title) {
        this(id, title, false);
    }

    // Copy constructor for deep copying
    public FilterStateModel(FilterStateModel other) {
        this.id = other.id;
        this.title = other.title;
        this.isSelected = other.isSelected;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
}
