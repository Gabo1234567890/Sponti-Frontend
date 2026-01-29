# Sponti-Frontend

Frontend mobile application for **Sponti** – a platform for short, spontaneous challenges designed to help users diversify their free time through light activities based on preferences such as duration, budget, and location.

This repository contains the **client-side Android application**, developed using **Kotlin Multiplatform** with **Compose Multiplatform**, currently targeting **Android only**.

## Overview

The Sponti mobile application allows users to:

- register and authenticate
- browse available challenges
- filter challenges by preferences (time, budget, location)
- participate in challenges
- track personal progress
- upload completion images
- manage profile settings

An **administrator interface** is also included, allowing:
- challenge approval
- challenge deletion
- user role management
- user deletion

The application communicates with a real backend API via REST.

## Architecture

The frontend follows a **clean MVVM-based architecture**:

```
UI (Compose Screens)
↓
ViewModel
↓
Repository
↓
API Service (Retrofit)
```

### Key architectural principles:
- **Unidirectional data flow**
- **State-driven UI**
- **Separation of concerns**
- **Lifecycle-aware ViewModels**

## Tech Stack

### Core technologies
- **Kotlin Multiplatform (KMP)**
- **Compose Multiplatform**
- **Android SDK**

### Android & UI
- Jetpack Compose
- Material 3
- Navigation Compose
- Adaptive launcher icons

### State & Lifecycle
- ViewModel
- State holders
- Kotlin Coroutines
- Flow

### Networking
- Retrofit
- OkHttp
- Moshi (JSON serialization)
- REST API communication

### Dependency Injection
- Manual DI (constructor-based)

## Project Structure

```
androidMain/
├── kotlin/
│ ├── ui/
│ │ ├── screens/      # With UI, View Models and States
│ │ ├── components/   # Reusable generic components
│ │ └── theme/        # Colors, fonts and text styles definition
| |── core/
│ ├── data/
│ ├── navigation/
│ ├── MainActivity.kt/
│ └── SpontiApp.kt/
│
├── res/
│ ├── mipmap-*/
│ ├── drawable/
| ├── font/
| ├── values/
│ └── xml/
│
└── AndroidManifest.xml
```

### Source sets
- `androidMain` – main Android-specific implementation  
- No `commonMain` is used at this stage (Android-only target)

## Navigation

Navigation is implemented using **Navigation Compose** with a centralized route system.

- Bottom navigation for main sections
- Stack-based navigation
- `launchSingleTop` and `restoreState` used to preserve screen state
- Screens react to lifecycle events to refresh data when needed

## Authentication & Roles

The application supports:
- authenticated users
- administrator users

Role-based UI behavior:
- admin-only screens and actions
- protected API calls
- backend-enforced permissions

## Backend Integration

The app communicates with the Sponti backend via REST.

### Features:
- JWT-based authentication
- Authorized requests via interceptors
- Error handling with mapped UI messages
- Network and server error differentiation

Backend implementation:
- **NestJS**
- **PostgreSQL**
- **Docker**
- **TypeORM**
- **Swagger**
- Exposed locally via **ngrok**

## UI & UX

- Fully declarative UI with Compose
- Material 3 design system
- State-driven recomposition
- Snackbar-based global error feedback
- Loading indicators and disabled states

## Running the Project

### Requirements
- Android Studio
- JDK 17+
- Android SDK
- Emulator or physical Android device

### Steps
1. Clone the repository
2. Open the project in **Android Studio**
3. Sync Gradle
4. Run the app on an emulator or device

## Current Limitations

- Android-only target (no iOS/Desktop yet)
- No automatic challenge generation algorithm
- Limited offline support

## Future Improvements

- Shared `commonMain` logic
- iOS target
- Offline caching
- Advanced recommendation algorithms

## License

This project is developed for **educational purposes** as part of a diploma thesis.
