package com.example.mealsplanner.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "weekly_plan")
public class WeeklyPlanMeal {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "meal_id")
    private String mealId;

    @ColumnInfo(name = "meal_name")
    private String mealName;

    @ColumnInfo(name = "meal_image")
    private String mealImage;

    @ColumnInfo(name = "planned_date")
    private String plannedDate;

    public WeeklyPlanMeal(Meal meal, String date) {
        this.mealId = meal.getId();
        this.mealName = meal.getName();
        this.mealImage = meal.getImageUrl();
        this.plannedDate = date;
    }
    public WeeklyPlanMeal() {

    }



    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getMealId() { return mealId; }
    public void setMealId(String mealId) { this.mealId = mealId; }
    public String getMealName() { return mealName; }
    public void setMealName(String mealName) { this.mealName = mealName; }
    public String getMealImage() { return mealImage; }
    public void setMealImage(String mealImage) { this.mealImage = mealImage; }
    public String getPlannedDate() { return plannedDate; }
    public void setPlannedDate(String plannedDate) { this.plannedDate = plannedDate; }
}