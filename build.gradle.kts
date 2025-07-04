// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    val kotlinVersion = "1.9.20"

    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2")
        maven("https://jitpack.io")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.7.3")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
        classpath("com.google.gms:google-services:4.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.19.0")
    id("com.github.hierynomus.license").version("0.16.1")
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2")
        maven("https://jitpack.io")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
