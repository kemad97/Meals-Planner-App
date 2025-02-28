plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    id("androidx.navigation.safeargs")
    // Add the dependency for the Google services Gradle plugin


}

android {
    namespace = "com.example.mealsplanner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mealsplanner"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
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
    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    implementation("com.google.firebase:firebase-analytics")

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-auth")

    implementation ("com.google.android.gms:play-services-auth:20.7.0")


    //Room
    implementation ("androidx.room:room-runtime:2.6.1")
    annotationProcessor ("androidx.room:room-compiler:2.6.1")

    // Add Navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)


//    implementation ('com.squareup.retrofit2:retrofit:2.9.0')


    // Add RxJava3 and RxAndroid
    implementation("io.reactivex.rxjava3:rxjava:3.1.8")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("androidx.room:room-rxjava3:2.6.1")


    // Add RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // RxBinding
    implementation("com.jakewharton.rxbinding4:rxbinding:4.0.0")

}