plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)

}

android {
    namespace = "com.example.mealsplanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mealsplanner"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_23
        targetCompatibility = JavaVersion.VERSION_23
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //lottie
    implementation(libs.lottie)

    // Retrofit
    implementation (libs.retrofit2.retrofit)
    implementation (libs.converter.gson)
    implementation (libs.adapter.rxjava3)


    // Glide
    implementation (libs.glide)
    annotationProcessor (libs.compiler)
    //Firebase
    implementation (libs.firebase.storage)

    //Room
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")


//    implementation ('com.squareup.retrofit2:retrofit:2.9.0')


    // Add RxJava3 and RxAndroid
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("androidx.room:room-rxjava3:2.6.1")


    // Add RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
}