//import com.x930073498.Libs.AndroidX

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
}
afterEvaluate {
    publishing() {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.x930073498.rstore"
                artifactId = "lifecycle"
                version = Versions.publish
            }


        }
    }
}

android {
    compileSdk = Versions.Environment.compile

    defaultConfig {
        minSdk = Versions.Environment.min
        targetSdk = Versions.Environment.target


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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(Libs.AndroidX.core)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.fragment)
    implementation(Libs.AndroidX.startup)
    implementation(Libs.AndroidX.processLifecycle)
}