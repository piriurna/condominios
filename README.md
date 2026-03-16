# Condominios

## What is this?

A **condominium management tool** built with Kotlin Multiplatform (Android + Web/Desktop), designed to be used daily by **doormen** at a residential building.

## Who uses it and how

The primary user is the **doorman** at the front desk. They log in and use the app to manage the building's day-to-day operations:

- **See who lives where** — look up residents by apartment, view names and contact info (sensitive fields like CPF are partially masked unless the user has elevated privileges)
- **Manage visitors** — register who is visiting, which apartment they are going to, and when they arrived/left
- **Manage deliveries** — log packages and deliveries that arrive for residents
- **Resident directory** — browse apartments and their current occupants

## What it is NOT for (daily use)

Adding condominiums and apartments is a **setup/admin task**, done once when onboarding a new building. It is not part of the daily workflow. The daily focus is on:

- Residents (moradores)
- Visitors
- Deliveries

## Development philosophy

### Domain-Driven Design (DDD)
The domain is the heart of the codebase. Business concepts live in the domain layer as first-class models — `Condominio`, `Apartamento`, `Morador`, `Visitante`, `Entrega` — and the rest of the app (data, UI) adapts to them, never the other way around. Use cases encode the actual business rules. Repository interfaces are defined in the domain and implemented in the data layer.

### Test-Driven Development (TDD)
New features are built test-first:
1. Write a failing test that describes the expected behaviour
2. Write the minimum code to make it pass
3. Refactor

Every use case, repository, and ViewModel must have tests. The test stack is **mockmp (Kodein Mock)** for mocking interfaces and **kotlinx-coroutines-test** for async code. Integration-style tests use lightweight in-memory fakes that share state to verify full flows end-to-end before touching the data layer.

We do not write UI tests. The goal is maximum coverage through fast unit tests instead. To make this possible, all classes with logic must be injectable — no hard-wired dependencies, no static state, no singletons that can't be replaced. If a class cannot be tested without a real database, a real network, or a running Compose tree, it is not designed correctly.

## Tech stack

- **Kotlin Multiplatform** — shared business logic and UI targeting Android and Web (Compose Multiplatform)
- **Clean Architecture** — domain / data / UI layers separated by feature modules
- **Room** — local database (KMP)
- **Koin** — dependency injection
- **Compose Navigation** — type-safe routes with `@Serializable` route objects

---



* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run Desktop (JVM) Application

To build and run the development version of the desktop app, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…