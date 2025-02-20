package com.example.mealsplanner.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorite_meals")
public class FavoriteMeal {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "meal_id")
    private String id;

    @ColumnInfo(name = "meal_name")
    private String name;

    @ColumnInfo(name = "meal_image")
    private String imageUrl;

    @ColumnInfo(name = "category")
    private String category;

    //from API Meal to FavoriteMeal
    public FavoriteMeal(Meal meal) {
        this.id = meal.getId();
        this.name = meal.getName();
        this.imageUrl = meal.getImageUrl();
        this.category = meal.getCategory();
    }

    public FavoriteMeal() {

    }


    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}