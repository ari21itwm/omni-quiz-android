plugins {
    alias(libs.plugins.android.library)
    // alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.omni.quiz.feature.quiz"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:data"))
    
    implementation(libs.androidx.core.ktx)
}
