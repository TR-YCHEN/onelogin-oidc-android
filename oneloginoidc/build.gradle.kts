plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.onelogin.oidc"
    compileSdk = 35

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation("com.android.support:appcompat-v7:28.0.0")
    implementation("androidx.activity:activity:1.9.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("net.openid:appauth:0.11.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.jakewharton.timber:timber:5.0.1")
}
