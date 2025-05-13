// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.diffplug.spotless") version "6.21.0"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("com.github.ben-manes.versions") version "0.48.0"
    id("org.jetbrains.kotlinx.kover") version "0.7.3"
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint("0.50.0")
    }
}

detekt {
    config = files("detekt-config.yml")
}