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

### 1. Add UMP dependency in your `libs.versions.toml`:

#### Dependency:
```kotlin dsl
ump = { group = "com.google.android.ump", name = "user-messaging-platform", version.ref = "ump_version" }
```

### 2. Add Dagger/Hilt support


