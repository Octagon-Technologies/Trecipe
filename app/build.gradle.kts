import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
//    id("com.google.devtools.ksp' // version '1.8.10-1.0.9")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.octagontechnologies.trecipe"
    compileSdk = 34

    val major_release = 1
    val default_release = 0
    val minor_release = 0

    defaultConfig {
        applicationId = "com.octagontechnologies.trecipe"
        minSdk = 21
        targetSdk = 34
        versionCode = (major_release * 100) + (default_release * 10) + minor_release
        versionName = "$major_release.$default_release.$minor_release"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val apiKeysFile = project.rootProject.file("api_keys.properties")
        val properties = Properties()
        properties.load(apiKeysFile.inputStream())

        val recipeApiKey = properties.getProperty("RECIPE_API_KEY")
        buildConfigField("String", "RECIPE_API_KEY", recipeApiKey)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.11.0")


    // Shimmer
    implementation("com.facebook.shimmer:shimmer:0.5.0")

    // KSP
//    implementation("com.google.devtools.ksp:symbol-processing-api:1.9.20-1.0.14")

    // Dexter
    implementation("com.karumi:dexter:6.2.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Test dependencies
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation("androidx.test:core-ktx:1.5.0")
    testImplementation("org.robolectric:robolectric:4.5.1")
    testImplementation("androidx.test.ext:junit-ktx:1.1.5")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // Android Test dependencies
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Room and Lifecycle dependencies
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    // Moshi
    implementation("com.squareup.moshi:moshi:1.15.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.20")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.6.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // CircleImageView
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Groupie
    implementation("com.github.lisawray.groupie:groupie:2.10.1")
    implementation("com.github.lisawray.groupie:groupie-viewbinding:2.10.1")

    // Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Sdp
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.0.6")

    // Star rating
    implementation("me.zhanghai.android.materialratingbar:library:1.4.0")

    // Okhttp Logging Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")

    // Splash Screen
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

    // Ads
//    implementation("com.google.android.gms:play-services-ads:22.6.0")
}

kapt {
    correctErrorTypes = true
}
