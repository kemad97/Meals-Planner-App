package com.example.mealsplanner.model.response;

import com.example.mealsplanner.model.CategoriesItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse {
    @SerializedName("categories")
    private List<CategoriesItem> categories;

    public List<CategoriesItem> getCategories() {
        return categories;
    }
}
