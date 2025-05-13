plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
        sourceCompatibility = JavaVersion.VERSION_17 // Align with CI (Java 17)
        targetCompatibility = JavaVersion.VERSION_17
        // If local runtime uses Java 21, update to:
        // sourceCompatibility = JavaVersion.VERSION_21
        // targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "17" // Align with CI
        // If using Java 21, update to:
        // jvmTarget = "21"
    }
}

jacoco {
    toolVersion = "0.8.11" // Supports Java 17 and 21
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
    jvmArgs("-XX:+EnableDynamicAgentLoading") // Suppress Byte Buddy agent warning
    finalizedBy("jacocoTestReport")
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(file("$buildDir/reports/jacoco/html")) // Explicit output path
    }
    val fileFilter = listOf(
        "**/di/**",
        "**/BuildConfig.*",
        "**/R.class",
        "**/R$*.class",
        "**/Manifest*.*",
        "**/*Test*.*",
        "**/databinding/**",
        "**/*Binding.class",
        "**/serialization/**",
        "**/exceptions/**",
        "**/*\$InjectAdapter.*",
        "**/*\$ModuleAdapter.*",
        "**/Dagger*.*",
        "**/*_Factory.*",
        "**/*_MembersInjector.*",
        "**/*Fragment*.*", // Exclude Fragments
        "**/*Adapter*.*", // Exclude Adapters
        "**/*ViewHolder*.*", // Exclude ViewHolders
        "**/*Activity*.*", // Exclude Activities
        "**/ui/**", // Exclude UI package (adjust if needed)
        "**/*Companion*.*" // Exclude Kotlin Companion objects
    )
    val javaClasses = fileTree("$buildDir/intermediates/javac/debug/classes") {
        exclude(fileFilter)
    }
    val kotlinClasses = fileTree("$buildDir/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    classDirectories.setFrom(files(javaClasses, kotlinClasses))
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(
        files("$buildDir/jacoco/testDebugUnitTest.exec")
            .filter { it.exists() } // Only include existing files
    )
    // Add logging for debugging
    doLast {
        println("JaCoCo report generated at: ${reports.html.outputLocation.get()}")
        println("Execution data files: ${executionData.files}")
        println("Class directories: ${classDirectories.files}")
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.11.0")
    implementation("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.dagger:dagger:2.50")
    kapt("com.google.dagger:dagger-compiler:2.50")
    implementation("com.google.dagger:dagger-android:2.50")
    implementation("com.google.dagger:dagger-android-support:2.50")
    kapt("com.google.dagger:dagger-android-processor:2.50")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.squareup.picasso:picasso:2.8")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.3")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("net.bytebuddy:byte-buddy:1.15.7")
    testImplementation("net.bytebuddy:byte-buddy-agent:1.15.7")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.robolectric:robolectric:4.13")
    androidTestImplementation("androidx.test.ext:junit-ktx:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test:core:1.6.1")
    implementation("org.bouncycastle:bcprov-jdk18on:1.76")
}

configurations.all {
    resolutionStrategy {
        force("org.bouncycastle:bcprov-jdk18on:1.76")
    }
}