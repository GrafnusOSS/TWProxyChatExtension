rootProject.name = "TWVelocityAdapter"
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.typewritermc.com/beta")
        maven {
            name = "papermc"
            url = uri("https://repo.papermc.io/repository/maven-public/")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}