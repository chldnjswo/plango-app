import java.util.Properties

val envFile = rootProject.file(".env")
val envProps = Properties().apply {
    if (envFile.exists()) {
        load(envFile.inputStream())
    }
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)


}

android {
    namespace = "com.plango.app"
    compileSdk = 36
    buildFeatures {
        viewBinding=true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.plango.app"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "MAPS_API_KEY", "\"${envProps["MAPS_API_KEY"] ?: ""}\"")

        // ✅ Manifest 내 meta-data로도 전달
        manifestPlaceholders["MAPS_API_KEY"] = envProps["MAPS_API_KEY"] ?: ""

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
    kotlinOptions {
        jvmTarget = "11"
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
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // ✅ JSON 변환용 (Gson Converter)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // ✅ (선택) OkHttp 로깅 인터셉터 - 네트워크 로그 확인용
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("androidx.fragment:fragment-ktx:1.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
    implementation("androidx.gridlayout:gridlayout:1.0.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.airbnb.android:lottie:6.3.0") //ai가 생각하는 느낌 효과

    implementation("androidx.datastore:datastore-preferences:1.1.1") // 데이터 스토어 의존성

    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation ("com.google.android.libraries.places:places:3.5.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
}