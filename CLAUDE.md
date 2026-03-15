# Condominios — Project Guide

## What this app is

A **condominium management tool** for **doormen** at residential buildings. The doorman is the primary user. Admin setup (adding buildings, apartments, residents) is a one-time task done by an administrator and is not part of the daily workflow.

**Doorman's daily use:**
- Look up residents by apartment or name
- Register visitors (who, which apartment, arrival/departure)
- Log deliveries and mark them as delivered

**Admin's use (setup only):**
- Create condominios and apartments
- Add and manage residents (moradores) with their types

## Architecture

**Clean Architecture with Domain-Driven Design.** The domain layer is the heart of the codebase.

- Business concepts live as domain models: `Condominio`, `Apartamento`, `Morador`, `Visitante`, `Entrega`
- Use cases encode business rules
- Repository interfaces are defined in domain, implemented in data
- UI adapts to domain — never the other way around
- Feature modules: `condominio`, `login`, `pessoa`, `database`, `di`, `navigation`, `common`

## Testing philosophy

**TDD — tests come first.** Every use case, repository, and ViewModel must have tests.

- **Test stack:** mockmp (Kodein Mock) for mocking, `kotlinx-coroutines-test` for async
- **No UI tests.** Maximum coverage through fast unit tests only.
- All classes with logic must be injectable — no hard-wired dependencies, no static state, no untestable singletons
- Integration-style tests use lightweight in-memory fakes

## Tech stack

- **Kotlin Multiplatform** — Android + Desktop (JVM) targets
- **Compose Multiplatform** — shared UI
- **Room** (KMP) — local database
- **Koin** — dependency injection (`factory`, `single`, `viewModelOf`)
- **Compose Navigation** — type-safe routes with `@Serializable` objects

## Build commands

```shell
# Android
.\gradlew.bat :composeApp:assembleDebug

# Desktop
.\gradlew.bat :composeApp:run

# Tests
.\gradlew.bat :features:condominio:domain:testDebugUnitTest
.\gradlew.bat :features:condominio:data:testDebugUnitTest
.\gradlew.bat :features:condominio:ui:testDebugUnitTest
```

## Key conventions

- **CPF masking** is a UI concern — domain stores CPF as plain string; `maskCpf()` lives in the UI layer
- **ID generation** happens in the repository implementation, not in use cases or UI
- **Navigation events** are modelled as sealed classes in `UiState`; cleared after handling via `onNavigationHandled()`
- **MoradorTipo:** `PROPRIETARIO` (max 2 per apt), `RESIDENTE`, `RESIDENTE_TEMPORARIO` — business rule enforced in `AddMoradorUseCaseImpl`
- **DebugRoute** is kept throughout V1 development for quick feature access; replaced with real app shell in T9 (V1 final task)
