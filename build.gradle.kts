// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.chaquo.python") version "15.0.1" apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    kotlin("plugin.serialization") version "1.7.10" apply false

}
buildscript {
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://chaquo.com/maven")
        }
//        maven {
//            url = uri("https://www.atilika.org/nexus/content/repositories/atilika/")
//        }

    }
//    dependencies {
//        classpath("com.android.tools.build:gradle:7.0.0")
//        classpath("com.chaquo.python:gradle:11.0")
//    }
}
