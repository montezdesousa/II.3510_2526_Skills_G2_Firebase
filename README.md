# Firebase Skills Demo App

A simple Android demo app built with **Jetpack Compose**, **Firebase Authentication**, and **Cloud Firestore**.  
This app allows users to:

- Sign in using Firebase Authentication (email/password).
- Log out of their account.
- Add, view, and delete skills stored in Cloud Firestore (user-specific).

It demonstrates a clean **MVVM architecture**, reactive state management with `StateFlow`, and Firebase integration in a Compose app.

---

## Features

- **Login & Logout**: Secure authentication using Firebase (email/password)
- **Firestore CRUD**: Add and remove user-specific skill items.
- **Reactive UI**: Compose updates automatically with changes in Firestore.
- **Error handling**: Displays errors when operations fail or permissions are denied.

---

## Setup

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/firebase-skills-demo.git
cd firebase-skills-demo
```

### 2. Create a Firebase project
1. Go to [Firebase Console](https://console.firebase.google.com/).  
2. Click **Add Project** and follow the steps.

### 3. Enable Firebase Authentication
1. In your Firebase project, go to **Authentication → Sign-in method**.  
2. Enable **Email/Password** sign-in.

### 4. Enable Firestore
1. Go to **Firestore Database → Create Database**.  
2. Start in **Test mode** (or configure security rules).

### 5. Add Firebase configuration to your project
1. In Firebase Console, go to **Project Settings → Your Apps → Android**.  
2. Register your app with your package name (e.g., `com.example.firebaseskillsapp`).  
3. Download the generated `google-services.json`.  
4. Place it in your app module’s root directory: app/google-services.json

### 6 Create a test user in Firebase Authentication
1. In Firebase Console, go to **Authentication → Users**.  
2. Click **Add user**.  
3. Enter an **email** and **password** for testing (e.g., `test@example.com`).  
4. Save the user.  

> You can now use this email/password to log in from the app and add/view/remove user skills.
