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
  - Search
  
#### Home Screen
- **Search Functionality**: Users can explore curated recipes (based on the the results of the questionnaire).
- **Recipe Display**: A RecyclerView displays the recipe name and an image of the recipe.
- **Clickable Recipes**: When pressed, the user will be brough to the **Recipe Details** screen, which will include detailed information about the recipe.

#### Shopping List Screen
- **Add Items**: Users can add items to the shopping list using a Floating Action Button, which opens a Dialog.
- **Checkable Items**: Each item in the shopping list can be checked off once acquired.
- **Deletable Items**: Each item in the shopping list can be deleted.

#### Favorites Screen
- **Favorite Recipes**: A RecyclerView displays all of the user's favorite recipes.
- **Deletable Items**: Users can remove items from their favorites.

#### Recipe Details Screen
- **Recipe Information**: Clicking on a recipe in any RecyclerView brings the user to a detailed page, including:
  - Image of the recipe
  - Name of the recipe
  - Description
  - Ingredients needed
  - Detailed instructions
- **Implicit Intent**: If the user clicks on the send arrow, they will be presented with the option to share the current recipe through email, messages, etc. The message includes:
   - Source URL (URL of the original recipe)
   - Recipe Name

#### Search Screen
- This activity allows users to search for recipes based on what they have in their pantry. Users can enter multiple items at once, or look for recipes that pertain to one specific item. When they click the search button, the recycler view is updated to match the entered search.

## Core Subjects from Syllabus

### Data Storage and Retrieval
- **SharedPreferences**: For storing user preferences from the onboarding questionnaire.
- **Firebase Authentication**: For user authentication and registration.
- **Firebase Firestore**: For storing user favorites and shopping list.

### Networking and API Integration
- **Retrofit**: For making network requests to the Spoonacular API to fetch recipes.
- **AsyncTask**: For handling background operations and network requests asynchronously.

### User Interface Design
- **Navigation Components**: For managing navigation and user flow in the app.
- **Material Design**: For implementing user interface components like buttons, dialogs, and navigation bars.

### Other Concepts
- **Fragments and Activities**: Each screen is a fragment, changes when different navigation icons are pressed.
- **Implicit Intent**: Recipes are shareable.
- **Recycler View**: Used for all recipe lists.
- **Radio Buttons, Image Buttons, and Checkboxes**
