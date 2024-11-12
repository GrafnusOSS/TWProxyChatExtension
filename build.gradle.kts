plugins {
    kotlin("jvm") version "2.0.20"
}

allprojects {
    repositories {
        mavenCentral()
    }
}

tasks.jar {
    enabled = false
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
}