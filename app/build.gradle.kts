plugins {
    alias(libs.plugins.android.application)
    id("com.chaquo.python")
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" // this version matches your Kotlin version
}

android {
    namespace = "com.example.test3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.test3"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        ndk {
            abiFilters += setOf("armeabi-v7a", "x86", "arm64-v8a", "x86_64")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions {
        resources {
            excludes.add("META-INF/*")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.1.1"
    }
    kotlinOptions {
        //jvmTarget = "17"
        jvmTarget = "1.8"

    }
}

dependencies {
    //imp lementation("com.chaquo.python:android:15.0.1")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation("com.atilika.kuromoji:kuromoji-ipadic:0.9.0")
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation("androidx.compose.ui:ui:1.6.8")
    implementation("androidx.compose.material:material:1.6.8")
    implementation("androidx.compose.ui:ui-tooling:1.6.8")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(libs.core.ktx)
    implementation ("androidx.compose.animation:animation:1.6.8")
    implementation(libs.navigation.compose)
    //implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.material3:material3:1.3.0-beta03")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.1")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.3.0-beta03")
    implementation(libs.benchmark.macro)
    implementation("com.andree-surya:moji4j:1.0.0")


    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2") // Check for the latest version
    implementation("androidx.compose.ui:ui:1.6.8") // Ensure you have the right version of Compose
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity-compose:1.9.0")

    implementation ("com.google.code.gson:gson:2.8.6")

}
// Chaquopy configuration block
chaquopy {
    defaultConfig {
        pip {
            install("youtube-transcript-api")
            install("unidic-lite")
        }
    }
}