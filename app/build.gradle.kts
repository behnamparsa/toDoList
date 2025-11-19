import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.techmania.todolistx"
    compileSdk = 36
//    compileSdk {
//        version = release(36)
//    }

    defaultConfig {
        applicationId = "com.techmania.todolistx"
        minSdk = 21
        targetSdk = 36
        versionCode = 2
        versionName = "1.1"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    testOptions {
        animationsDisabled = true

        managedDevices {
            allDevices {
                create<ManagedVirtualDevice>("pixel2Api32") {
                    device = "Pixel 2"
                    apiLevel = 32             // my minSDK is 31
                    systemImageSource = "aosp-atd" // uses an Android Test Device (ATD) system image from Google for API 32.

                    require64Bit = false
                }
            }
        }
    }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}