plugins {
    id("com.android.application")
    id("kotlin-android")
    kotlin("android")
}

val composeVersion = "1.0.0-alpha09"
val coroutinesAndroidVersion = "1.3.9"
val coreKTXVersion = "1.5.0-alpha05"
val materialVersion = "1.2.1"
val constraintLayoutVersion = "2.0.2"
val cardViewVersion = "1.0.0"
val appCompatVersion = "1.3.0-alpha02"
val activityVersion = "1.1.0"
val composeGlideVersion = "0.4.1"
val lifecycleVersion = "2.3.0-beta01"
val fragmentVersion = "1.2.5"

dependencies {
    implementation(project(":shared"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesAndroidVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${rootProject.extra["kotlinVersion"]}")
    implementation("com.google.android.material:material:$materialVersion")
    implementation("com.github.mvarnagiris:compose-glide-image:$composeGlideVersion")
    implementation("androidx.core:core-ktx:$coreKTXVersion")
    implementation("androidx.core:core:$coreKTXVersion")
    implementation("androidx.appcompat:appcompat:$appCompatVersion")
    implementation("androidx.fragment:fragment-ktx:$fragmentVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.activity:activity-ktx:$activityVersion")

    androidTestImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test:rules:1.3.0")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.compose.ui:ui-test:$composeVersion")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")

    implementation("androidx.compose.runtime:runtime:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.foundation:foundation-layout:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.animation:animation:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")
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
        kotlinCompilerVersion = "${rootProject.extra["kotlinVersion"]}"
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
            "-Xskip-prerelease-check"
        )
        kotlinOptions.useIR = true
    }
}
