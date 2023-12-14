plugins {
    id("com.android.application")
}


android {
    namespace = "com.jnu.student"
    compileSdk = 33


    testOptions {
        unitTests.isReturnDefaultValues  = true
    }

    defaultConfig {
        testInstrumentationRunner ="androidx.test.runner.AndroidJUnitRunner"
    }
    defaultConfig {
        applicationId = "com.jnu.student"
        minSdk = 24
        targetSdk = 33
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

  //  compileOptions {
   //   sourceCompatibility = JavaVersion.VERSION_1_80
   //   targetCompatibility = JavaVersion.VERSION_1_8
   // }


}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.tencent.map:tencent-map-vector-sdk:4.3.4")

    implementation("androidx.test:monitor:1.6.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20231013")

    androidTestImplementation ("androidx.tracing:tracing:1.7.0-beta02")
    androidTestImplementation ("androidx.test.ext:junit:1.2.0-alpha02")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.6.0-alpha02")
    androidTestImplementation ("androidx.test.espresso:espresso-contrib:3.6.0-alpha02")

}