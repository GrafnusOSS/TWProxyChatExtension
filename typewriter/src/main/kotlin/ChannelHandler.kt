import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteStreams
import com.google.gson.Gson
import com.typewritermc.engine.paper.TypewriterPaperPlugin
import com.typewritermc.engine.paper.interaction.ChatHistory
import com.typewritermc.engine.paper.interaction.ChatHistoryHandler
import com.typewritermc.engine.paper.logger
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class ChannelHandler : PluginMessageListener, KoinComponent {
    private val chatHistoryHandler: ChatHistoryHandler by inject()

    fun initialize() {
        val plugin: TypewriterPaperPlugin = get(named("plugin"))
        Bukkit.getServer().messenger.registerIncomingPluginChannel(plugin, "playerchatchanel:main", this)
    }

    fun destroy() {
        val plugin: TypewriterPaperPlugin = get(named("plugin"))
        Bukkit.getServer().messenger.unregisterIncomingPluginChannel(plugin)
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray?) {
        logger.info("Channel used: $channel")
        if (message == null) {
            return
        }
        val input: ByteArrayDataInput = ByteStreams.newDataInput(message)
        val subChannel: String = input.readUTF()
        if (subChannel.equals("ProxyChat")) {
            val chatHistory = chatHistoryHandler.getHistory(player.uniqueId)
            val messageStr: String = input.readUTF()
            val messageComp: Component = JSONComponentSerializer.json().deserialize(messageStr)

            val plugin: TypewriterPaperPlugin = get(named("plugin"))
            plugin.logger.info("Received Proxy Chat Message:")
            plugin.logger.info("JSON: $messageStr")
            plugin.logger.info("Comp: $messageComp")


            chatHistory.addMessage(messageComp)
        }
    }

}