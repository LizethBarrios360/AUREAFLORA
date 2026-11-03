# AureaFlora (avanceuno) – Project Documentation

This document describes the current state of the `avanceuno` project: the first working slice of the TryKeeper codebase, reduced to **Login**, **Register** and **Home**.

## 1. Overview

`avanceuno` is a Kotlin Multiplatform (KMP) project with Compose Multiplatform (CMP) UI. It is a trimmed copy of the TryKeeper project, kept buildable and runnable so that new features can be added on top of a working base.

Only the following features are implemented:

| Feature | Screen route | Status |
|---|---|---|
| Login | `/login` | Working (Android) |
| Register | `/register` | Working (Android) |
| Home (product grid) | `/home` | Working (Android) |

Everything else from TryKeeper (order details, purchases, clients, invoices, materials) was removed.

## 2. Targets and status

| Target | Source set | Build status | Runtime status |
|---|---|---|---|
| Android | `androidMain` | Compiles (`assembleDebug`) | Verified on emulator: login, register, home |
| Desktop (JVM) | `desktopMain` | Compiles (`compileKotlinDesktop`) | Not functional: no Koin start, database driver is a `TODO` stub |
| wasmJs | `wasmJsMain` | **Fails** to compile | Not functional |
| iOS (simulator arm64) | `iosMain`, `nativeMain` | **Fails** to compile | Not functional: database driver is a `TODO` stub |

### Known build blockers

- **wasmJs:** SQLDelight `2.0.2` does not publish a wasm variant (`app.cash.sqldelight:runtime:2.0.2` cannot be resolved for `wasmJsCompileClasspath`).
- **iOS:** Ktor `3.1.1` klibs are built with Kotlin `2.1.10`, while the project uses Kotlin `2.0.20`. The Kotlin/Native compiler skips them with an ABI version error.

Both are dependency version mismatches. Fixing them requires upgrading Kotlin, SQLDelight and the Compose plugin together. This has not been done yet.

## 3. Technology stack

Versions are taken from `gradle/libs.versions.toml` and `composeApp/build.gradle.kts`.

| Area | Technology | Version |
|---|---|---|
| Language | Kotlin Multiplatform | 2.0.20 |
| UI | Compose Multiplatform (material, foundation, ui, resources) | 1.6.11 (plugin) |
| Build | Android Gradle Plugin | 8.2.2 |
| Gradle wrapper | Gradle | 8.7 |
| Navigation | PreCompose (`NavHost`, `scene`) | 1.6.2 |
| ViewModels | PreCompose ViewModel (`moe.tlaster.precompose.viewmodel`) | 1.6.2 |
| DI | Koin (`koin-bom`) | 4.0.2 |
| Database | SQLDelight (Android driver, JVM desktop stub) | 2.0.2 |
| Serialization plugin | `kotlin("plugin.serialization")` | 2.0.20 |
| Android SDK | compileSdk / targetSdk / minSdk | 34 / 34 / 24 |
| Java target (Android) | JVM | 11 |

Ktor, kotlinx-serialization and coroutines are declared in the build file but are not used by the current code (there is no network layer).

## 4. Project structure

```
avanceuno/
├── build.gradle.kts                  Root build (plugin aliases)
├── settings.gradle.kts               rootProject.name = "TryKeeper", includes :composeApp
├── gradle.properties
├── gradle/
│   ├── libs.versions.toml            Version catalog
│   └── wrapper/                      Gradle wrapper (8.7)
├── gradlew / gradlew.bat
├── local.properties                  Local Android SDK path (machine-specific, gitignored)
├── README.md                         Original template readme
├── DOCUMENTATION.md                  This file
├── iosApp/                           Xcode host project (SwiftUI wrapper for Compose)
└── composeApp/
    ├── build.gradle.kts              KMP targets, dependencies, SQLDelight config
    └── src/
        ├── commonMain/
        │   ├── kotlin/com/compose/trykeeper/
        │   │   ├── App.kt                    Root composable (PreComposeApp, KoinContext, theme, navigation)
        │   │   ├── AppTheme.kt               Material theme + color palette (getColorsTheme)
        │   │   ├── di/appModule.kt           Koin module (repositories and ViewModels)
        │   │   ├── navigation/Navigator.kt   Routes: /login, /register, /home
        │   │   ├── model/                    Users, Products
        │   │   ├── domain/                   Repository interfaces (Login, Register, Home)
        │   │   ├── data/                     Repository implementations + DatabaseDriverFactory (expect)
        │   │   ├── presentacion/             ViewModels and sealed result types (login, register, home)
        │   │   └── ui/                       Composables (LoginScreen, RegisterScreen, HomeScreen)
        │   ├── kotlin/androidx/compose/desktop/ui/tooling/preview/Preview.kt
        │   ├── sqldelight/com/compose/trykeeper/db/aureaDB.sq   Schema, seed data, queries
        │   └── composeResources/
        │       ├── drawable/            ic_product1..6.png, compose-multiplatform.xml
        ├── androidMain/
        │   ├── kotlin/com/compose/trykeeper/MainActivity.kt
        │   ├── kotlin/com/compose/trykeeper/MainApplication.kt   Starts Koin with the Android DB driver
        │   ├── kotlin/com/compose/trykeeper/data/DatabaseDriverFactory.android.kt
        │   ├── AndroidManifest.xml
        │   └── res/                     Launcher icons, strings.xml (app_name = "AureaApp")
        ├── desktopMain/
        │   ├── kotlin/com/compose/trykeeper/main.kt             Desktop window ("TryKeeper")
        │   └── kotlin/com/compose/trykeeper/data/DatabaseDriverFactory.desktop.kt  TODO stub
        ├── wasmJsMain/
        │   ├── kotlin/com/compose/trykeeper/main.kt             ComposeViewport("composeApplication")
        │   ├── kotlin/com/compose/trykeeper/data/DatabaseDriverFactory.wasmJs.kt  TODO stub
        │   └── resources/index.html, styles.css
        ├── iosMain/
        │   └── kotlin/com/compose/trykeeper/MainViewController.kt   ComposeUIViewController { App() }
        └── nativeMain/
            └── kotlin/com/compose/trykeeper/data/DatabaseDriverFactory.native.kt  TODO stub
```

Package root: `com.compose.trykeeper`. Application ID: `com.compose.trykeeper`.

## 5. Architecture

The code follows a layered structure inside `commonMain`:

```
Screen (ui/)  →  ViewModel (presentacion/)  →  Repository interface (domain/)  →  RepositoryImpl (data/)  →  SQLDelight queries
```

- **ui/** – Stateless-ish Composables. They receive state and callbacks.
- **presentacion/** – One ViewModel per feature (`LoginViewModel`, `RegisterViewModel`, `HomeViewModel`). Each has a sealed `*Results` type for repository outcomes.
- **domain/** – Repository interfaces (`LoginRepository`, `RegisterRepository`, `HomeRepository`). They return the `*Results` types from `presentacion/`.
- **data/** – Repository implementations that query the SQLDelight database through `AureaFloraDatabase.aureaDBQueries`.
- **model/** – Plain data classes used by the UI and repositories.

There are no use cases, no service layer and no HTTP client. The current features use a local database only.

### Dependency injection

`appModule(database)` in `di/appModule.kt` registers:

| Binding | Scope |
|---|---|
| `LoginRepository` → `LoginRepoImpl` | `single` |
| `LoginViewModel` | `factory` |
| `RegisterRepository` → `RegisterRepoImpl` | `single` |
| `RegisterViewModel` | `factory` |
| `HomeRepository` → `HomeRepoImpl` | `single` |
| `HomeViewModel` | `factory` |

Koin is started in `MainApplication.onCreate()` on Android. It is **not** started on desktop, wasmJs or iOS yet.

### Navigation

`navigation/Navigator.kt` defines a `NavHost` with `initialRoute = "/login"`:

| Route | Screen | Transitions |
|---|---|---|
| `/login` | `LoginScreen` | "Registrate" link → `/register`. When `LoginViewModel.isLogin` becomes true → `/home`. |
| `/register` | `RegisterScreen` | On submit, calls `RegisterViewModel.register`. When `isRegister` is true → `/login`. |
| `/home` | `HomeScreen` | Shows products from `HomeViewModel.products`. |

The ViewModels are obtained with `koinViewModel` in `Navigation()`. `App()` wraps everything in `PreComposeApp`, `KoinContext` and `AppTheme`.

## 6. Features

### 6.1 Login

- **UI:** `ui/login/LoginScreen.kt`
  - Title "AureaFlora".
  - "Usuario" text field and "Contraseña" field with a show/hide toggle.
  - "Iniciar Sesión" button.
  - "¿No tienes cuenta? Registrate" link.
  - Red snackbar "Usuario o contraseña incorrectos" for 3 seconds on failure.
- **ViewModel:** `presentacion/login/LoginViewModel.kt`
  - `login(username, password)` runs in `viewModelScope`.
  - Sets `isLogin` on success and `isError` on failure.
- **Repository:** `data/LoginRepoImpl.kt`
  - Looks up the user by `usuario` (`validateUsername`).
  - Compares the stored `contrasena` with the given password in plain text.
  - Returns `LoginResults.Success` or `LoginResults.Error`.

### 6.2 Register

- **UI:** `ui/register/RegisterScreen.kt`
  - Seven fields: Nombre, Apellido, Cedula, Celular, Codigo Vendedor, Usuario, Contraseña.
  - "Registrar" button is enabled only when all fields are non-empty.
  - The column scrolls vertically.
- **ViewModel:** `presentacion/register/RegisterViewModel.kt`
  - `register(user)` runs in `viewModelScope` and sets `isRegister` on success or failure.
- **Repository:** `data/RegisterRepoImpl.kt`
  - Rejects the registration if the username already exists.
  - Otherwise inserts a row into `Usuario_Vendedor`.
- **Model:** `model/Users.kt` (`name`, `lastName`, `cedula`, `celular`, `codeSale`, `userName`, `password`).

Notes:
- There is no format validation (cedula, phone, etc.).
- The error case is not shown to the user. The screen simply does not navigate.
- The database enforces `UNIQUE` on `cedula`, `codigo_vendedor` and `usuario`.

### 6.3 Home

- **UI:** `ui/home/HomeScreen.kt`
  - Title "Productos".
  - Horizontal row of filter chips: Todos, Azul, Ramos, Mariposa, Brazalete, Rojo.
  - Grid of 3 columns with product image, name and value (`$<value>`).
  - Product images are chosen by `idProduct` (1–6). Other IDs fall back to the Compose logo.
- **ViewModel:** `presentacion/home/HomeViewModel.kt`
  - Loads products in `init` through `HomeRepository.getProducts()`.
- **Repository:** `data/HomeRepoImpl.kt`
  - Reads all rows from `Producto` and maps them to `model/Products.kt`.
  - Returns `HomeResults.Error` when the table is empty.

Notes:
- The chip filter compares the chip's index with `idCategory`. The chip labels do not match the seed categories (for example, "Azul" maps to category 1, which is "Brazalete y Broche"). Treat the filter as a placeholder until the categories are defined.
- Product clicks are not wired to any action yet (the purchase screen was removed).

## 7. Database

SQLDelight schema: `composeApp/src/commonMain/sqldelight/com/compose/trykeeper/db/aureaDB.sq`. Generated database class: `com.compose.trykeeper.db.AureaFloraDatabase`.

### Tables

**`Usuario_Vendedor`** (sellers / app users)

| Column | Type | Constraints |
|---|---|---|
| `id_usuario` | INTEGER | PK, AUTOINCREMENT |
| `nombre` | TEXT | NOT NULL |
| `apellido` | TEXT | NOT NULL |
| `cedula` | TEXT | UNIQUE, NOT NULL |
| `celular` | TEXT | NOT NULL |
| `codigo_vendedor` | TEXT | UNIQUE, NOT NULL |
| `usuario` | TEXT | UNIQUE, NOT NULL |
| `contrasena` | TEXT | NOT NULL |

**`Producto`** (products)

| Column | Type | Constraints |
|---|---|---|
| `id_producto` | INTEGER | PK, AUTOINCREMENT |
| `nombre` | TEXT | NOT NULL |
| `valor` | REAL | NOT NULL |
| `id_categoria` | INTEGER | nullable |

### Seed data

- `Producto`: 6 products (Ramo 20 Rosas, Brazalete y Broche Pareja, Brazalete 1 Rosa, Ramo 10 Rosas, Broche Caballero, Ramo 30 Rosas).
- `Usuario_Vendedor`: one user `aurea` with password `1234`.

### Queries

| Query | Purpose |
|---|---|
| `validateUsername` | Find a user by `usuario` |
| `products` | List all products |
| `createUser` | Insert a new seller |

### Database driver per platform

| Platform | Factory | Status |
|---|---|---|
| Android | `AndroidSqliteDriver`, file `AureaFloraDatabase.db` | Working |
| Desktop | `DatabaseDriverFactory.desktop.kt` | `TODO()` stub |
| wasmJs | `DatabaseDriverFactory.wasmJs.kt` | `TODO()` stub |
| iOS / native | `DatabaseDriverFactory.native.kt` | `TODO()` stub |

## 8. Theme and resources

- **Theme:** `AppTheme` wraps `MaterialTheme` with a black primary color and square (zero-radius) shapes.
- **Colors:** `getColorsTheme()` returns a `DarkModeColors` object. `isDarkMode` is hard-coded to `false`.
- **Strings:** UI text is hard-coded in Spanish inside the composables. There is no string resource setup for this slice (`androidMain/res/values/strings.xml` only defines `app_name`).
- **Drawables:** `ic_product1.png` … `ic_product6.png` are used by the home grid.
- **Fonts:** the Baskerville font was removed because it is not used.

## 9. Build and run

From the `avanceuno` folder:

```sh
# Android debug APK
./gradlew :composeApp:assembleDebug

# Install and run on a connected device or emulator
./gradlew :composeApp:installDebug

# Desktop compile check
./gradlew :composeApp:compileKotlinDesktop

# Desktop run (not functional yet: database driver is a stub and Koin is not started)
./gradlew :composeApp:run

# Known failing targets (see section 2)
./gradlew :composeApp:compileKotlinWasmJs
./gradlew :composeApp:compileKotlinIosSimulatorArm64
```

Note: `gradlew` needed `chmod +x` after copying from TryKeeper. Keep the executable bit when copying the project.

### Manual test accounts

| Username | Password | Source |
|---|---|---|
| `aurea` | `1234` | Seed data |
| `ana` | `abc123` | Created during manual testing (exists only in the emulator database) |

## 10. Verification log

| Check | Result |
|---|---|
| `:composeApp:assembleDebug` | Passed |
| Login with `aurea` / `1234` on Android emulator | Passed, reaches Home |
| Register a new user on Android emulator | Passed, returns to Login |
| Login with the new user | Passed, reaches Home |
| `:composeApp:compileKotlinDesktop` | Passed |
| `:composeApp:compileKotlinWasmJs` | Failed (SQLDelight wasm variant missing) |
| `:composeApp:compileKotlinIosSimulatorArm64` | Failed (Kotlin/Ktor ABI mismatch). Same failure in the original TryKeeper project. |

## 11. Known limitations and open items

1. **Build targets:** wasmJs and iOS do not compile. Requires upgrading Kotlin, SQLDelight and the Compose plugin to compatible versions.
2. **Desktop and wasmJs runtime:** Koin is not started and the database drivers are stubs.
3. **iOS runtime:** the database driver is a stub. `MainViewController.kt` does not start Koin.
4. **Security:** passwords are stored and compared in plain text.
5. **Validation:** no input format validation on register.
6. **Error feedback:** register failures are silent.
7. **Home filters:** chip labels do not match the category data.
8. **Unused dependencies:** Ktor, kotlinx-serialization and coroutines are declared but unused.
9. **Side effects in composition:** `Navigation()` calls `navigator.navigate` directly inside scene composition for login and register. It works today but should move to an effect in a later refactor.
10. **Tests:** there are no automated tests.

## 12. Next steps (suggested)

- Decide on the version upgrade (Kotlin 2.1.x, SQLDelight with wasm support, matching Compose) to unblock wasmJs and iOS.
- Implement the database drivers for desktop, wasmJs and iOS, and start Koin on each entry point.
- Add input validation and visible error messages on Register.
- Replace plain-text password comparison with hashing.
- Define the real product categories and fix the Home filter.
