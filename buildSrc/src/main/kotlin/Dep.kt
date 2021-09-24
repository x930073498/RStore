object Versions {

    object Environment {
        const val compile = 30
        const val min=21
        const val target= compile
    }

    const val publish = "0.3.3"
}

object Libs {

    object AndroidX {
        const val core = "androidx.core:core-ktx:1.6.0"
        const val appcompat = "androidx.appcompat:appcompat:1.3.1"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.1.0"
        const val lifecycleJava8 = "androidx.lifecycle:lifecycle-common-java8:2.3.1"
        const val fragment = "androidx.fragment:fragment-ktx:1.3.6"
        const val viewpager2 = "androidx.viewpager2:viewpager2:1.1.0-beta01"
        const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:2.3.1"
        const val kotlinxCoroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1"
        const val startup = "androidx.startup:startup-runtime:1.1.0"
        const val processLifecycle="androidx.lifecycle:lifecycle-process:2.3.1"

    }

    object Material {
        const val material = "com.google.android.material:material:1.4.0"
    }


}