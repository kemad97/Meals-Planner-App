package com.example.mealsplanner.model.response;

import com.example.mealsplanner.model.Area;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AreaResponse {
    @SerializedName("meals")
    private List<Area> areas;

    public List<Area> getAreasResponse() {
        return areas;
    }


}