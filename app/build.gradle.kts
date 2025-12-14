plugins {
    alias(libs.plugins.android.application)
    // Nếu bạn dùng Realm, cần plugin của nó, nhưng tạm thời chỉ thêm thư viện như yêu cầu
}

android {
    namespace = "com.example.docsachln"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.docsachln"
        minSdk = 28
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "SUPABASE_URL", "\"https://elpenksctdnqrywdtase.supabase.co\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVscGVua3NjdGRucXJ5d2R0YXNlIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU1NDQ5OTIsImV4cCI6MjA4MTEyMDk5Mn0.IEsd3DI0I8YZaGiacRvwX88XJaQpnI40j3iIz2IUXNY\"")
    }

    // Bật ViewBinding để code UI Java dễ dàng hơn (thay thế findViewById)
    buildFeatures {
        viewBinding = true
        buildConfig = true  // ← THÊM DÒNG NÀY

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

    // Cập nhật lên Java 17 để tương thích tốt nhất với các thư viện mới (OkHttp, Retrofit)
    // Android Studio 2024 mặc định dùng JDK 17 hoặc 21
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    // --- Android Core & UI ---
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // --- Lifecycle & ViewModel ---
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")

    // --- Navigation ---
    implementation("androidx.navigation:navigation-fragment:2.7.6")
    implementation("androidx.navigation:navigation-ui:2.7.6")

    // --- Networking (Retrofit & OkHttp) ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // --- Authentication ---
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // --- Image Loading (Glide) ---
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.activity)
    // Lưu ý: annotationProcessor dùng cho Java, nếu dùng Kotlin thì đổi thành ksp
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // --- Database (MongoDB Realm - Optional nếu bạn chỉ dùng API) ---
    // Lưu ý: Realm Kotlin thường dành cho Kotlin project, nhưng bạn vẫn có thể import
    implementation("io.realm.kotlin:library-base:1.13.0")
    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}