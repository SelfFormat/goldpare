plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-android")
}

val composeVersion = "1.0.0-alpha08"
val kotlinVersion = "1.4.20"
val coroutinesAndroidVersion = "1.3.9"
val coreKTXVersion = "1.3.1"
val materialVersion = "1.2.1"
val constraintLayoutVersion = "2.0.2"
val cardViewVersion = "1.0.0"
val appCompatVersion = "1.2.0"
val recyclerViewVersion = "1.1.0"
val swipeRefreshVersion = "1.1.0"

dependencies {
    implementation(project(":shared"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesAndroidVersion")
    implementation("androidx.core:core-ktx:$coreKTXVersion")
    implementation("com.google.android.material:material:$materialVersion")
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("androidx.constraintlayout:constraintlayout:$constraintLayoutVersion")
    implementation("androidx.recyclerview:recyclerview:$recyclerViewVersion")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:$swipeRefreshVersion")
    implementation("androidx.cardview:cardview:$cardViewVersion")
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.selfformat.goldpare.androidApp"
        minSdkVersion(24)
        targetSdkVersion(30)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerVersion = kotlinVersion
        kotlinCompilerExtensionVersion = composeVersion
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>() {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs = listOf(
            *kotlinOptions.freeCompilerArgs.toTypedArray(),
            "-Xallow-jvm-ir-dependencies",
            "-Xskip-prerelease-check")
        kotlinOptions.useIR = true
    }
}
