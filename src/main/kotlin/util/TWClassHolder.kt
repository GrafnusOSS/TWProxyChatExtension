package util

import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin

class TWClassHolder {
    companion object {
        //Any ideas on how to get the TW plugin instance is welcome (I am a Kotlin noob)
        fun get(): Plugin = Bukkit.getPluginManager().getPlugin("Typewriter")!!
    }
}