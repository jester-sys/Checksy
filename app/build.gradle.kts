plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
}

android {
    compileSdk = 34
    namespace = "com.jaixlabs.checksy"

    defaultConfig {
        applicationId = "com.jaixlabs.checksy"
        minSdk = 21
        targetSdk = 34
        versionCode = 29
        versionName = "1.0.5"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.6"
    }
    bundle {
        language {
            enableSplit = false
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    flavorDimensions += listOf("version")
    productFlavors {
        create("dev") {
            dimension = "version"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
        }
        create("prod") {
            dimension = "version"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // compose dependency
    implementation(libs.androidx.core.coreKtx)
    implementation(libs.androidx.appCompat)
    implementation(libs.activity.compose)
    implementation(libs.google.android.material.material)
    implementation(libs.google.compose.material.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.google.compose.material.material3)
    implementation(libs.system.controller)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.viewmodel.compose)
    implementation(libs.glance.appwidget)
    //biometric
    implementation(libs.biometric)
    //work manager
    implementation(libs.workManager)
    implementation(libs.review.ktx)
    //testing
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso)
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.test.tooling)
    debugImplementation(libs.compose.ui.test.manifest)
    // Room components
    implementation(libs.androidx.room.roomKtx)
    ksp(libs.androidx.room.roomCompiler)
    implementation(libs.androidx.room.core.testing)
    // Lifecycle components
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    //hilt
    implementation(libs.google.hiltAndroid)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hiltCompiler)
    implementation(libs.androidx.datastorePref)
    // Play core library
    implementation(libs.android.play.core)
    //review
    debugImplementation(libs.leak.canery)
    implementation ("androidx.compose.material:material-icons-extended:1.5.4")
    //coil image library
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation ("org.jetbrains.kotlin:kotlin-parcelize-runtime:1.8.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7") // ✅ Required for hiltViewModel()
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
}