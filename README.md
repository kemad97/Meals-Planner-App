# Meals Planner Application

Meals Planner is an Android mobile application that helps users plan their weekly meals efficiently. It provides meal suggestions, browsing by category or country, meal searches, and the ability to save favorite meals for offline access. The app integrates Firebase for authentication and data backup, while local storage using Room Database handles offline favorites.

---

## Features

* **Meal of the Day:** Get a random meal suggestion for inspiration every day.
* **Meal Search:** Search meals by:

  * Country
  * Ingredient
  * Category
  * Meal name
* **Categories & Countries:** Browse meal categories and explore popular meals by country.
* **Favorites:**

  * Add or remove meals from favorites.
  * View favorites offline.
  * Sync favorites with Firebase for cloud backup.
* **Meal Planning:**

  * Add meals to your current week's meal plan.
  * Add planned meals to your mobile calendar as events.
  * View weekly meal plans offline and sync with Firebase.
* **Authentication:**

  * User registration and login.
  * Google Authentication via Firebase.
  * Guest mode with restricted access (view-only).
* **Meal Details Page:**

  * Display meal name, image, origin country.
  * Show ingredients with images and measurements.
  * Step-by-step preparation instructions.
  * Embedded video tutorial.

---

## Technologies Used

* Android (Java)
* XML-based UI with Material Design Components
* MVP Architecture Pattern
* RxJava for reactive programming
* Retrofit for API requests
* Room Database for local storage
* Glide for image loading
* Firebase Authentication & Cloud Storage
* API Integration with [TheMealDB API](https://www.themealdb.com/api.php)

---


## API

This app uses [TheMealDB API](https://www.themealdb.com/api.php) to fetch meal data.

---


