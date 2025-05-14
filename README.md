# RickAndMorty.Android.App

[![Build Status](https://github.com/erickbogarin/RickAndMorty.Android.App/actions/workflows/main.yml/badge.svg)](https://github.com/erickbogarin/RickAndMorty.Android.App/actions/workflows/main.yml)

This project is an Android application developed in Kotlin that consumes the Rick and Morty API. Below you’ll find key information about the project’s architecture, main technologies, and CI setup.

---

## Architecture

- **MVVM (Model-View-ViewModel):**  
  The application uses the MVVM pattern to separate presentation logic from business logic, improving maintainability and testability. ViewModels handle state and lifecycle, keeping the UI layer simple.

- **Dependency Injection with Dagger 2:**  
  Dagger 2 is used for dependency injection across Activities, Fragments, ViewModels, and modules such as networking and storage, providing decoupling and easier testability.

- **Layered & Modular Structure:**  
  - `data`: Handles remote and local data access.
  - `domain`: Contains business rules and use cases.
  - `presentation`: Manages UI and user interaction.
  - Feature-specific Dagger modules enhance modularity and scalability.

- **Navigation Component:**  
  Uses Android Jetpack Navigation for safe and flexible screen navigation.

---

## Main Technologies

- **Language:**  
  - Kotlin

- **Frameworks & Libraries:**  
  - **AndroidX:** Modern Android components (ViewModel, LiveData, Navigation, etc.).
  - **Retrofit 2:** REST client for API communication with Gson for JSON conversion.
  - **OkHttp:** HTTP client with logging interceptor for debugging.
  - **RxJava 2 / RxAndroid:** For asynchronous and reactive programming.
  - **Dagger 2:** Dependency injection, including custom modules for network and storage.
  - **Picasso:** Image loading and caching.
  - **JUnit Jupiter, MockK, Robolectric:** Unit and instrumented testing frameworks.
  - **Jacoco:** Test coverage reporting.
  - **Spotless & Detekt:** Code linting and formatting tools.

- **Project Configuration:**  
  - Gradle plugins for Kotlin, Jacoco, Detekt, and Spotless.
  - Java 17 support.
  - ViewBinding enabled.

---

## Continuous Integration (CI)

This project uses a modern CI pipeline integrated with GitHub Actions and Gradle build tools to ensure code quality and reliability at every change.

- **Automated Build & Lint:**  
  Every push or pull request triggers automated builds, code formatting checks (Spotless), and static analysis (Detekt) to enforce coding standards and prevent issues early.

- **Automated Testing & Coverage:**  
  All unit and instrumentation tests run automatically with each build, using JUnit, MockK, and Robolectric. Test coverage is measured with Jacoco, and coverage reports are generated in both HTML and XML formats.

- **Code Quality & Dependency Checks:**  
  The pipeline checks for outdated dependencies (Ben Manes Versions plugin) and enforces code quality with static analysis tools.

- **Consistent Environment:**  
  The CI builds use Java 17 and align with the local development environment to prevent “works on my machine” problems.

- **Secure & Reliable:**  
  Security dependencies are managed and locked to specific versions to ensure a safe build process.

---

Let me know if you’d like to add usage instructions or more details about any aspect of the project!