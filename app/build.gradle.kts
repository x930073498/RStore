plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdk = Versions.Environment.compile

    defaultConfig {
        applicationId = "com.x930073498.sample"
        minSdk = Versions.Environment.min
        targetSdk = Versions.Environment.target
        versionCode = 1
        versionName = "1.0"

    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {

//        release {
//            minifyEnabled=false
//            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
//        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {
    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.Material.material)
    implementation(Libs.AndroidX.constraintlayout)
    implementation(Libs.AndroidX.lifecycleJava8)
    implementation(Libs.AndroidX.fragment)
    implementation(Libs.AndroidX.viewpager2)
    implementation(Libs.AndroidX.lifecycleRuntime)
    implementation(Libs.AndroidX.liveData)
    implementation(Libs.AndroidX.kotlinxCoroutines)
    implementation(project(":rstore"))
    implementation(project(":features"))

}