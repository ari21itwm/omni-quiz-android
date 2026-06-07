plugins {
    alias(libs.plugins.android.application)
    // alias(libs.plugins.kotlin.android) // Temporarily commented out to test if it's already applied
}

android {
    namespace = "com.omni.quiz"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.omni.quiz"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
