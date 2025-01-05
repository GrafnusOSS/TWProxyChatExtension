plugins {
    kotlin("jvm") version "2.0.20"
    id("com.typewritermc.module-plugin") version "1.0.0"
}

group = "de.GrafnusOSS"
version = "1.0.0"

typewriter {
    engine {
        version = "0.7.0"
        //channel = com.typewritermc.moduleplugin.ReleaseChannel.BETA
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