plugins {
    kotlin("jvm") version "2.0.20"
    id("com.typewritermc.module-plugin") version "1.0.0"
}

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven { url = uri("https://jitpack.io/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")
    compileOnly("com.github.gabber235:typewriter:develop-SNAPSHOT")
}

typewriter {
    engine {
        version = "0.6.0"
        channel = com.typewritermc.moduleplugin.ReleaseChannel.BETA
    }
    namespace = "grafnusoss"

    extension {
        name = "ProxyChat"
        shortDescription = "Allows messages from the proxy to be saved in TypeWriter"
        description = """
            |The ProxyChat extension allows TypeWriter to safe the chat messages from the proxy as well.
            |This extension uses the PluginMessaging feature between BungeeCord/Velocity and the server TypeWriter is installed on.
            |For this extension to work a counterpart on the proxy must be installed as well. 
        """.trimMargin()

        paper {
            // Optional - If you want to make sure a plugin is required to be installed to use this extension
//            dependency("<plugin name>")
        }
    }
}

kotlin {
    jvmToolchain(21)
}