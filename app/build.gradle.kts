plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-android")
    id("kotlin-kapt")
    id("jacoco")
}

android {
    namespace = "com.example.rickandmorty"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.example.rickandmorty"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    kapt {
        generateStubs = true
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

jacoco {
    toolVersion = "0.8.8"
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named("testDebugUnitTest")) // Substitua pelo nome correto do task de teste, se necessário

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileTree = fileTree("${buildDir}/intermediates/javac/debug/classes") {
        exclude("**/R.class", "**/R$*.class", "**/BuildConfig.*", "**/Manifest*.*")
    }

    classDirectories.setFrom(fileTree)
    sourceDirectories.setFrom(files("src/main/java"))
    executionData.setFrom(files("${buildDir}/jacoco/testDebugUnitTest.exec"))
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:adapter-rxjava:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.10")
    implementation("com.google.dagger:dagger:2.24")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.google.dagger:dagger-android:2.24")
    implementation("com.google.dagger:dagger-android-support:2.24")
    kapt("com.google.dagger:dagger-compiler:2.24")
    kapt("com.google.dagger:dagger-android-processor:2.24")
    implementation("io.reactivex.rxjava2:rxjava:2.2.11")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")ci
    androidTestImplementation("androidx.test:rules:1.5.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.robolectric:robolectric:4.10.3")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("androidx.test:core:1.5.0")
    testImplementation("io.reactivex.rxjava2:rxjava:2.2.21")
    testImplementation("io.reactivex.rxjava2:rxandroid:2.1.1")
}

tasks.withType<Test> {
    useJUnitPlatform() // Certifique-se de que está usando a plataforma correta
    finalizedBy("jacocoTestReport") // Garante que o relatório seja gerado após os testes
}