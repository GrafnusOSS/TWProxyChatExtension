rootProject.name = "TWVelocityAdapter"
pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.typewritermc.com/beta")
    }
}
include("ProxyChatExtension")
include("VelocityProxyPlugin")
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}