package com.example.mealsplanner.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Meal {
	@SerializedName("idMeal")
	private String id;

	@SerializedName("strMeal")
	private String name;

	@SerializedName("strMealThumb")
	private String imageUrl;

	@SerializedName("strYoutube")
	private String youtubeUrl;

	@SerializedName("strCategory")
	private String category;

	@SerializedName("strArea")
	private String area;

	@SerializedName("strInstructions")
	private String instructions;

	@SerializedName("strTags")
	private String tags;

	// For Room Database
	private boolean isFavorite;

	// Ingredients
	@SerializedName("strIngredient1") private String strIngredient1;
	@SerializedName("strIngredient2") private String strIngredient2;
	@SerializedName("strIngredient3") private String strIngredient3;
	@SerializedName("strIngredient4") private String strIngredient4;
	@SerializedName("strIngredient5") private String strIngredient5;
	@SerializedName("strIngredient6") private String strIngredient6;
	@SerializedName("strIngredient7") private String strIngredient7;
	@SerializedName("strIngredient8") private String strIngredient8;
	@SerializedName("strIngredient9") private String strIngredient9;

	// Measures
	@SerializedName("strMeasure1") private String strMeasure1;
	@SerializedName("strMeasure2") private String strMeasure2;
	@SerializedName("strMeasure3") private String strMeasure3;
	@SerializedName("strMeasure4") private String strMeasure4;
	@SerializedName("strMeasure5") private String strMeasure5;
	@SerializedName("strMeasure6") private String strMeasure6;
	@SerializedName("strMeasure7") private String strMeasure7;
	@SerializedName("strMeasure8") private String strMeasure8;
	@SerializedName("strMeasure9") private String strMeasure9;

	public List<Ingredient> getIngredientsList() 
	{
		List<Ingredient> ingredients = new ArrayList<>();

		addIngredientToList(ingredients, strIngredient1, strMeasure1);
		addIngredientToList(ingredients, strIngredient2, strMeasure2);
		addIngredientToList(ingredients, strIngredient3, strMeasure3);
		addIngredientToList(ingredients, strIngredient4, strMeasure4);
		addIngredientToList(ingredients, strIngredient5, strMeasure5);
		addIngredientToList(ingredients, strIngredient6, strMeasure6);
		addIngredientToList(ingredients, strIngredient7, strMeasure7);
		addIngredientToList(ingredients, strIngredient8, strMeasure8);
		addIngredientToList(ingredients, strIngredient9, strMeasure9);

		return ingredients;
	}

	private void addIngredientToList(List<Ingredient> ingredients, String ingredient, String measure) {
		if (ingredient != null && !ingredient.trim().isEmpty() &&
				measure != null && !measure.trim().isEmpty()) {
			ingredients.add(new Ingredient(ingredient.trim(), measure.trim()));
		}
	}

	public static class Ingredient {
		private final String name;
		private final String measure;

		public Ingredient(String name, String measure) {
			this.name = name;
			this.measure = measure;
		}

		public String getName() { return name; }
		public String getMeasure() { return measure; }

		public String getImageUrl()
		{
			return "https://www.themealdb.com/images/ingredients/" + name + ".png";
		}
	}

	public String getId() { return id; }
	public String getName() { return name; }
	public String getImageUrl() { return imageUrl; }
	public String getYoutubeUrl() { return youtubeUrl; }
	public String getCategory() { return category; }
	public String getArea() { return area; }
	public String getInstructions() { return instructions; }
	public String getTags() { return tags; }

	//Room Database
	public boolean isFavorite() { return isFavorite; }
	public void setFavorite(boolean favorite) { isFavorite = favorite; }
}