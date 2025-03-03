plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.proyecto2t_pmdm"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.proyecto2t_pmdm"
        minSdk = 24
        this.targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding{
        enable = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    //Navigation
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.8.0"))
    //Agregar la dependencia para Firebase Authentication
    implementation ("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    //Analytics
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    //Agregar la dependencia para Google Play services si vas a usar autenticación con Google
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    // Agregar la dependencia para Credential Manager
    implementation ("androidx.credentials:credentials:1.5.0-rc01")
    implementation ("androidx.credentials:credentials-play-services-auth:1.5.0-rc01")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    //implementation("androidx.activity:activity-compose:1.10.0")
    implementation("com.google.firebase:firebase-crashlytics-buildtools:3.0.2")

    // When using the BoM, you don't specify versions in Firebase library dependencies

    // Add the dependency for the Firebase SDK for Google Analytics
    implementation("com.google.firebase:firebase-analytics")


}