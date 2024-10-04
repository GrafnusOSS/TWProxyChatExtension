package util

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import com.typewritermc.engine.paper.TypewriterPaperPlugin

class TWClassHolder {
    companion object {
        //Any ideas on how to get the TW plugin instance is welcome (I am a Kotlin noob)
        fun get(): TypewriterPaperPlugin {
            var plugin: Plugin = Bukkit.getPluginManager().getPlugin("Typewriter")!!
            return plugin as TypewriterPaperPlugin
        }
    }
}