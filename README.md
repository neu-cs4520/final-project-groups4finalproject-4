# RecipeRite
Meal Planning App
By Mahika Chadha
chadha.ma@northeastern.edu

## Description
This meal planner application for Android helps users discover and plan meals based on their dietary preferences, or ingredients they have in their pantry. The app provides personalized recipe recommendations by using user input collected during onboarding / search and integrating with the Spoonacular API to fetch relevant recipes.

## Flow
1. The user is prompted to sign-in or register. (authenticated using Firebase Authentication)
2. User is presented with a questionnaire that helps us learn more about their needs and preferences.
3. User is taken to the home screen, where they see a navigation bar with four icons: Home, Shopping List, Favorites, Settings.
4. Home Screen: In the home screen, the user will be able to search for ingredients or cuisines and will be presented with a RecyclerView containing the recipe name and an image of the recipe.
5. Shopping List: Users can add items to shopping list with a Floating Action Button which opens a Dialog. Each shopping list item is checkable.
6. Favorites: This activity contains a recycler view of all of the user's favorite recipes!
7. Recipe Details: upon clicking on an item in any recycler view, the user will be brought to the details page. This will include an image of the recipe, name of recipe, description, ingredients needed, and detailed instructions.
8. Settings: User will be able to view and update preferences here.
