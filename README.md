# Recipe Planner App
Mahika Chadha (individual)
Group: Final Project 4

## Description
This meal planner application for Android helps users discover and plan meals based on their dietary preferences, or ingredients they have in their pantry. The app provides personalized recipe recommendations by using user input collected during onboarding / search and integrating with the Spoonacular API to fetch relevant recipes.

## Features 

### User Authentication

1. Sign-In/Registration: Users are prompted to sign in or register using Firebase Authentication.
### Onboarding
2. Questionnaire: After signing in, users complete a questionnaire to provide information about their dietary needs and preferences.
   
### Screens and Functionalities

#### Navigation
- Navigation Bar: The home screen includes a navigation bar with four icons:
  - Home
  - Shopping List
  - Favorites
  - Settings
  
#### Home Screen
- **Search Functionality**: Users can search for ingredients or cuisines.
- **Recipe Display**: A RecyclerView displays the recipe name and an image of the recipe based on the search results.

#### Shopping List Screen
- **Add Items**: Users can add items to the shopping list using a Floating Action Button, which opens a Dialog.
- **Checkable Items**: Each item in the shopping list can be checked off once acquired.

#### Favorites Screen
- **Favorite Recipes**: A RecyclerView displays all of the user's favorite recipes.

#### Recipe Details Screen
- **Recipe Information**: Clicking on a recipe in any RecyclerView brings the user to a detailed page, including:
  - Image of the recipe
  - Name of the recipe
  - Description
  - Ingredients needed
  - Detailed instructions
    
#### Settings Screen
10. **User Preferences**: Users can view and update their preferences.

## Core Subjects from Syllabus

### Data Storage and Retrieval
- **SharedPreferences**: For storing user preferences from the onboarding questionnaire.
- **Firebase Authentication**: For user authentication and registration.

### Networking and API Integration
- **Retrofit**: For making network requests to the Spoonacular API to fetch recipes.
- **AsyncTask**: For handling background operations and network requests asynchronously.

### User Interface Design
- **Navigation Components**: For managing navigation and user flow in the app.
- **Material Design**: For implementing user interface components like buttons, dialogs, and navigation bars.

#### So far I have completed all of the UI, and the log-in, register, onboarding and home screen implementation. 
