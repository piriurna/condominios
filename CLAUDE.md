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

Run all commands via PowerShell (batch files don't work from bash):

```shell
# Android
powershell.exe -Command "Set-Location 'C:\Users\franc\Documents\Projetos Android\condominios'; .\gradlew.bat :composeApp:assembleDebug"

# Desktop
powershell.exe -Command "Set-Location 'C:\Users\franc\Documents\Projetos Android\condominios'; .\gradlew.bat :composeApp:run"

# Tests (platform-agnostic, JVM target — use jvmTest for KMP modules)
powershell.exe -Command "Set-Location 'C:\Users\franc\Documents\Projetos Android\condominios'; .\gradlew.bat :features:login:ui:jvmTest :features:login:domain:jvmTest :features:login:data:jvmTest"
powershell.exe -Command "Set-Location 'C:\Users\franc\Documents\Projetos Android\condominios'; .\gradlew.bat :features:condominio:domain:jvmTest :features:condominio:data:jvmTest"
```

> **Note:** `org.gradle.java.home` is set in `local.properties` to use OpenJDK 25 (`~/.jdks/openjdk-25`). Gradle 9 requires JVM 17+; the system default is JDK 8.

## Workflow

- **Always use the dev agent** (`subagent_type: dev`) for implementing features, writing code, running builds, and running tests. Only do quick reads/exploration in the main conversation to gather context for the agent prompt.

## Key conventions

- **CPF masking** is a UI concern — domain stores CPF as plain string; `maskCpf()` lives in the UI layer
- **ID generation** happens in the repository implementation, not in use cases or UI
- **Navigation events** are modelled as sealed classes in `UiState`; cleared after handling via `onNavigationHandled()`
- **MoradorTipo:** `PROPRIETARIO` (max 2 per apt), `RESIDENTE`, `RESIDENTE_TEMPORARIO` — business rule enforced in `AddMoradorUseCaseImpl`
- **DebugRoute** is kept throughout V1 development for quick feature access; replaced with real app shell in T9 (V1 final task)
