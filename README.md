# Expense Tracker


Expense Tracker is an Android application developed using Kotlin, Jetpack Compose, and the MVVM architecture. It allows users to manage their expenses efficiently, with features such as adding expenses, viewing notifications for each expense, and visualizing data using Vico graph.


## Features

- **Add Expenses:** Users can add their expenses by specifying the amount, type, and name.
- **Notifications:** The app sends notifications whenever a new expense is added and stores these notifications for later viewing.
- **View Expenses:** Users can view their list of expenses.
- **Graphical Representation:** Expenses are visualized using Vico graph for better understanding.
## Tech stack

- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM (Model-View-ViewModel)
- **Database:** Room
- **Notifications:** Android NotificationTracker
- **Graphs:** Vico graph
## Getting started


### Prerequisites

- Android Studio Dolphin | 2021.3.1 or later
- Android SDK 28 or higher
- Kotlin 1.9.0 or later

### Installation

1. **Clone the repository:**
    ```sh
    https://github.com/Rudresh07/ExpenseTracker.git
    ```
2. **Open the project in Android Studio.**

3. **Build the project:**
    - Click on `Build > Rebuild Project` or use the shortcut `Ctrl+Shift+F9`.

4. **Run the app:**
    - Click on `Run > Run 'app'` or use the shortcut `Shift+F10`.
## Project strucuture

The project follows the MVVM architecture:

- **Model:** Handles data and business logic (Room database entities and DAOs).
- **View:** UI components built with Jetpack Compose.
- **ViewModel:** Manages UI-related data and business logic for the UI.

## Usage

1. Launch the app.
2. Create an account or log in using Firebase Authentication.
3. Explore recipes fetched from the API.
4. Upload your own recipes through the app interface.
5. Use the ingredient-based recipe generator powered by Gemini.
## Screenshots
<img src="https://github.com/Rudresh07/ExpenseTracker/assets/97966593/bf52e90b-2f70-4bc6-be7a-8709512a3a8d" width="200" height="400" />
<img src="https://github.com/Rudresh07/ExpenseTracker/assets/97966593/9067c02e-430b-4667-b002-8a055945b4d4" width="200" height="400" />
<img src="https://github.com/Rudresh07/ExpenseTracker/assets/97966593/990fe7ce-9eca-446b-aaf6-873749b50803" width="200" height="400" />
<img src="https://github.com/Rudresh07/ExpenseTracker/assets/97966593/c512f8d0-d0b0-46e1-b143-00ea52a8368b" width="200" height="400" />
<img src="https://github.com/Rudresh07/ExpenseTracker/assets/97966593/7a72d55d-dc9c-4baf-8943-c3b1469e480c" width="200" height="400" />
<img src="https://github.com/Rudresh07/ExpenseTracker/assets/97966593/a0c84a7c-0533-458d-b1b6-9839d7dad044" width="200" height="400" />



## Important Files

### `NotificationHandler.kt`

Handles the creation and management of notifications. It creates notification channels and sends notifications when a new expense is added.

### `NotificationViewModel.kt`

Manages the notification data. It interacts with the `NotificationDao` to fetch and add notifications to the database.

### `NotificationActivity.kt`

The main activity that displays the list of notifications using Jetpack Compose.

## Adding a New Expense

To add a new expense, use the `ExpenseViewModel` to insert the expense into the database. This will trigger a notification which is handled by `NotificationHandler`.

## Viewing Notifications

Notifications are stored in the Room database and displayed in `NotificationActivity` using Jetpack Compose. Each notification is shown in a card with a white background and includes a timestamp.

## Visualization with Vico Graph

The app uses Vico graph to provide graphical representations of expenses. Ensure you have added the Vico graph dependency in your `build.gradle` file.



## License

[MIT](https://choosealicense.com/licenses/mit/)

