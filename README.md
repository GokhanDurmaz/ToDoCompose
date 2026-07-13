# To-Do Compose

A modern, modular, and AI-integrated To-Do application built with **Jetpack Compose**, following the latest Android development best practices.

[![AI Integrated](https://img.shields.io/badge/AI-Task%20Generation-blue?style=for-the-badge&logo=google-gemini&logoColor=white)](https://github.com/GokhanDurmaz/ToDoCompose)
[![Tests](https://img.shields.io/badge/Tests-Passing-brightgreen?style=for-the-badge&logo=github-actions&logoColor=white)](https://github.com/GokhanDurmaz/ToDoCompose)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.0+-purple?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Architecture](https://img.shields.io/badge/Architecture-Clean--MVVM-success)](https://developer.android.com/topic/architecture)

---

## Tech Stack

| Category | Technology |
| :--- | :--- |
| **UI** | Jetpack Compose, Material 3 |
| **Concurrency** | Kotlin Coroutines, Flow |
| **Dependency Injection** | Hilt |
| **Navigation** | Compose Navigation (Type-safe) |
| **Database** | Room |
| **Networking** | Retrofit, Supabase (Custom Integration) |
| **Data Storage** | Jetpack DataStore (Proto/Preferences) |
| **AI** | Groq AI (LLM Engine for smart task extraction) |
| **Build System** | Gradle Kotlin DSL + Convention Plugins |

---

## Architecture & Modularization

This project follows a highly modularized architecture to ensure scalability, testability, and maintainability.

### Module Structure
- **`:app`**: The entry point of the application, connecting all feature modules.
- **`:feature:*`**: Contains isolated features like `:auth`, `:profile`, and `:settings`.
- **`:core:navigation`**: Centralized, type-safe navigation logic.
- **`:core:network`**: API services, LLM engines, and JNI-based crypto configurations.
- **`:core:common`**: Shared models, utilities, and base use cases.
- **`:data`**: Implementation of repositories and local/remote data sources.
- **`:test`**: Centralized testing utilities, fakes, and integration tests.

---

## Key Features

*   **AI-Powered Task Generation:** Automatically extract tasks and deadlines from natural language input using integrated LLM engines.
*   **Secure Authentication:** Full sign-in, sign-up, and password recovery flows powered by Supabase/Firebase logic.
*   **State Management:** Strict **Unidirectional Data Flow (UDF)** using `StateFlow` and `UiState` patterns.
*   **Security:** JNI-based API key protection and encrypted data storage for sensitive tokens.
*   **Comprehensive Testing:** High test coverage including Unit, ViewModel, and Instrumented UI tests.

---

## Testing

The project emphasizes quality through a robust testing setup located in the `:test` module.

- **Unit Tests:** Business logic and UseCase verification.
- **ViewModel Tests:** State transition and UI logic validation using `MainDispatcherRule`.
- **Instrumented Tests:** Compose UI tests to verify screen components and navigation.

Run tests using:
```bash
./gradlew :test:testDebugUnitTest
./gradlew :test:connectedDebugAndroidTest
```

---

## Getting Started

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/GokhanDurmaz/ToDoCompose.git
    ```
2.  **Configuration:**
    -   API keys are managed via the `NativeConfig` through JNI. Ensure your secrets are correctly configured in the native layer or `secrets.h`.
3.  **Build & Run:**
    -   Open the project in Android Studio (Ladybug or newer).
    -   Select the `app` configuration and click **Run**.

---

## 📜 License

Distributed under the Apache License 2.0. See `LICENSE` for more information.
