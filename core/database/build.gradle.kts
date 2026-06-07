plugins {
    alias(libs.plugins.android.library)
    // alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.omni.quiz.core.database"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
