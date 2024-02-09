
plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}


android {
    testOptions {
        unitTests.isIncludeAndroidResources = true;
    }
    namespace = "com.application.netflix"
    compileSdk = 34
    compileOptions{
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    defaultConfig {
        applicationId = "com.application.netflix"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-firestore:24.10.1")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("androidx.media3:media3-ui:1.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.razorpay:checkout:1.6.36")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    androidTestImplementation("androidx.test:runner:1.5.2")
    implementation ("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("io.reactivex.rxjava2:rxjava:2.2.6")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:2.3.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.android.volley:volley:1.2.1")
    implementation ("com.github.bumptech.glide:glide:4.11.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.11.0")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation ("io.reactivex.rxjava2:rxjava:2.2.6")
    implementation ("com.google.android.exoplayer:exoplayer-core:2.19.1")

    androidTestImplementation ("androidx.test:runner:1.5.2")
    testImplementation ("androidx.test:rules:1.5.0")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.ext:junit:1.1.5")
    testImplementation ("androidx.test.ext:junit:1.1.3")

    testImplementation ("junit:junit:4.13.2")

    testImplementation ("androidx.test:core-ktx:1.5.0")
    testImplementation ("androidx.test.ext:junit-ktx:1.1.5")











}