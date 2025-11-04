# 📝 To-Do List Application

A modern, feature-rich Android To-Do List application built with Java, following the MVVM architecture pattern and using Room Database for local data persistence.

## ✨ Features

- ✅ **Add Tasks**: Create new tasks quickly with a beautiful bottom sheet dialog
- ✏️ **Edit Tasks**: Tap on any task to edit its content
- ☑️ **Mark Complete**: Check/uncheck tasks to track completion status
- 🗑️ **Delete Tasks**: Long-press on tasks to delete them with confirmation dialog
- 💾 **Persistent Storage**: All tasks are saved locally using Room Database
- 🎨 **Material Design 3**: Modern UI with Material Design components
- 🌊 **Splash Screen**: Smooth splash screen experience on app launch
- 🔄 **Real-time Updates**: LiveData ensures UI updates automatically
- 🎯 **Input Validation**: Save button only enabled when task text is entered

## 🏗️ Architecture

This project follows the **MVVM (Model-View-ViewModel)** architecture pattern:

```
┌─────────────┐
│     View    │  (MainActivity, Adapter)
│   (UI Layer)│
└──────┬──────┘
       │
       ↓
┌─────────────┐
│  ViewModel  │  (ToDoViewModel)
└──────┬──────┘
       │
       ↓
┌─────────────┐
│ Repository  │  (ToDoRepository)
└──────┬──────┘
       │
       ↓
┌─────────────┐
│   Database  │  (Room Database)
│ (Data Layer)│
└─────────────┘
```

### Components:

- **View Layer**: `MainActivity`, `ToDoAdapter`
- **ViewModel Layer**: `ToDoViewModel`
- **Repository Layer**: `ToDoRepository`
- **Database Layer**: `ToDoDatabase`, `ToDoDao`, `ToDoEntity`

## 🛠️ Technologies Used

- **Language**: Java
- **Minimum SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 14 (API 34)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Database**: Room Persistence Library
- **UI Components**: Material Design 3 Components
- **Asynchronous Operations**: ExecutorService with Handler
- **Reactive Programming**: LiveData
- **Lifecycle Management**: ViewModel, AndroidViewModel

## 📚 Libraries & Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| AndroidX AppCompat | 1.7.1 | Backward compatibility |
| Material Components | 1.12.0 | Material Design UI |
| ConstraintLayout | 2.2.1 | Flexible layouts |
| Room Runtime | 2.6.1 | Database |
| Room Compiler | 2.6.1 | Annotation processing |
| Core SplashScreen | 1.0.1 | Splash screen API |
| Activity | 1.8.0 | Activity components |

## 📁 Project Structure

```
com.shivamsingh.practiceto_dolist/
│
├── adapter/
│   └── ToDoAdapter.java              # RecyclerView adapter
│
├── database/
│   ├── ToDoDao.java                  # Room DAO interface
│   ├── ToDoDatabase.java             # Room database instance
│   └── ToDoEntity.java               # Room entity class
│
├── repository/
│   └── ToDoRepository.java           # Data repository layer
│
├── ui/
│   └── MainActivity.java             # Main activity
│
└── viewmodel/
    └── ToDoViewModel.java            # ViewModel class
```

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17
- Android SDK with API 34
- Gradle 8.3.0

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Harendra-70/To-Do-List.git
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned repository

3. **Sync Gradle**
   - Android Studio will automatically sync Gradle
   - Wait for the build to complete

4. **Run the app**
   - Connect an Android device or start an emulator
   - Click the "Run" button or press `Shift + F10`

## 💡 Key Features Explanation

### 1. Add New Task
- Tap the floating action button (FAB)
- Enter task description
- Save button is disabled until text is entered
- Task is saved to database and appears in the list

### 2. Edit Task
- Tap on any task item
- Modify the task text in the bottom sheet dialog
- Changes are saved immediately to the database

### 3. Mark as Complete
- Check the checkbox to mark task as complete
- Uncheck to mark as incomplete
- Status is persisted in the database

### 4. Delete Task
- Long-press on any task
- Confirm deletion in the alert dialog
- Task is removed from database and UI

## 🗄️ Database Schema

### Table: `todo_table`

| Column | Type | Constraints |
|--------|------|-------------|
| id | INTEGER | PRIMARY KEY, AUTO_INCREMENT |
| task | TEXT | NOT NULL |
| status | INTEGER | NOT NULL (0 = incomplete, 1 = complete) |

## 🎨 UI Components

### Main Screen
- **TextView**: Displays app title
- **RecyclerView**: Lists all tasks
- **FloatingActionButton**: Opens add task dialog

### Task Item Layout
- **MaterialCardView**: Card container for each task
- **CheckBox**: Task text with completion toggle
- **LinearLayout**: Click/long-press handlers

### Add/Edit Task Dialog
- **BottomSheetDialog**: Modal bottom sheet
- **TextInputLayout**: Material text field
- **MaterialButton**: Save button with validation

## 🔄 Data Flow

1. **Adding a Task**:
   ```
   User Input → MainActivity → ViewModel → Repository → Database
   Database → Repository (callback) → ViewModel (LiveData) → MainActivity → Adapter
   ```

2. **Updating a Task**:
   ```
   User Edit → Adapter → ViewModel → Repository → Database
   Database → Repository → ViewModel (reload) → LiveData → UI Update
   ```

3. **Deleting a Task**:
   ```
   Long Press → Adapter → Confirmation Dialog → ViewModel → Repository → Database
   List Update → Adapter.notifyItemRemoved()
   ```

## ⚙️ Configuration

### Changing Database Name
In `ToDoDatabase.java`:
```java
private static final String DB_NAME = "your_database_name";
```

### Adjusting Splash Screen Duration
In `MainActivity.java`:
```java
splashScreen.setKeepOnScreenCondition(() ->
    System.currentTimeMillis() - splashStartTime < 2000  // Change duration (ms)
);
```


## 📱 Screenshots




## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Material Design guidelines by Google
- Android Developers documentation
- Room Persistence Library documentation

---

**Note**: This is a learning project demonstrating Android development best practices with MVVM architecture and Room Database. Feel free to use it as a reference or starting point for your own projects!



---

Made with ❤️ by Harendra Singh
