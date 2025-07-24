# 📜 Google Consent Form Manager

This is a lightweight Kotlin-based utility class to easily implement **Google UMP (User Messaging Platform)** Consent Form for GDPR/EEA compliance in your Android apps.

Supports:
- ✅ Consent request using [Google UMP SDK](https://developers.google.com/admob/android/privacy)
- ✅ Debug mode for testing in EEA
- ✅ Callback support to load ads after consent
- ✅ Privacy options support
- ✅ Easy integration with Dagger Hilt

---

## 🛠️ Prerequisites

- ✅ User Messaging Platform Dependency
- ✅ Dagger/Hilt Integration

### 1. 📌 Add UMP dependency in your `libs.versions.toml`:

#### Dependency:
```kotlin dsl
ump = { group = "com.google.android.ump", name = "user-messaging-platform", version.ref = "ump_version" }
```

### 2. 📌 Add Dagger/Hilt support

Add following depedencies and plugins to `libs.versions.toml`

Dependencies:
```depdencies
dagger-hilt = { group = "com.google.dagger", name = "hilt-android", version.ref = "daggerHilt_version" }
dagger-ksp = { group = "com.google.dagger", name = "hilt-android-compiler", version.ref = "daggerHilt_version" }
```
Plugin:
```plugin
hilt = { id = "com.google.dagger.hilt.android", version.ref = "daggerHilt_version" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp_version" }
```
Versions:
```versions
daggerHilt_version = "2.55"
ksp_version = "2.1.0-1.0.29"
```

#### Now add add dependencies and plugins in `build.gradle` for both app and project level as below:
#### App Level:

Plugins:
```plugin
alias(libs.plugins.ksp)
alias(libs.plugins.hilt)
```
Dependencies:
```depdencies
implementation(libs.dagger.hilt)
ksp(libs.dagger.ksp)
```
#### Project Level:
Plugin:
```plugin
alias(libs.plugins.hilt) apply  false
alias(libs.plugins.ksp) apply  false
```

#### 📌 2. Annotate Your Application Class

```anotate
@HiltAndroidApp
class MyApp : Application()
```

#### 📌 3. Update Manifest file

```update
<application
    android:name=".MyApp"
    ... >
</application>
```


