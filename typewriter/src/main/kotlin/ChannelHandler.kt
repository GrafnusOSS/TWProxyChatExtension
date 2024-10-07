import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage
import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteStreams
import com.typewritermc.engine.paper.TypewriterPaperPlugin
import com.typewritermc.engine.paper.interaction.ChatHistory
import com.typewritermc.engine.paper.interaction.ChatHistoryHandler
import com.typewritermc.engine.paper.interaction.OldMessage
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
import com.typewritermc.engine.paper.plugin
import com.typewritermc.engine.paper.utils.plainText
import java.util.concurrent.ConcurrentLinkedQueue

class ChannelHandler : PluginMessageListener, KoinComponent {
    private val chatHistoryHandler: ChatHistoryHandler by inject()

    fun initialize() {
        Bukkit.getServer().messenger.registerIncomingPluginChannel(plugin, "playerchatchanel:main", this)
    }

    fun destroy() {
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
            val messageStr: String = input.readUTF()
            val messageComp: Component = JSONComponentSerializer.json().deserialize(messageStr)
            plugin.logger.info("Received Proxy Chat Message:")
            plugin.logger.info("JSON: $messageStr")
            plugin.logger.info("========================")
            plugin.logger.info("Comp: $messageComp")

            insertChatMessageIfNotPresent(player, messageComp)
            TODO("Only unique messages are saved")
        }
    }

    private fun insertChatMessageIfNotPresent(player: Player, chatMessage: Component) {
        val chatHistory = chatHistoryHandler.getHistory(player.uniqueId)

        val field = chatHistory.javaClass.declaredFields.toList().filter { it.name == "messages" }.first()
        field.isAccessible = true
        val queue = field.get(chatHistory) as ConcurrentLinkedQueue<OldMessage>

        //val queue: ConcurrentLinkedQueue<OldMessage> = chatHistory.getPrivateProperty("messages") as ConcurrentLinkedQueue<OldMessage>

        val list: List<OldMessage> = queue.toList()

        if (!list.contains(OldMessage(chatMessage))) {
            chatHistory.addMessage(chatMessage)
            plugin.logger.info("Chat Message (${chatMessage.plainText()}) has been saved!")
        } else {
            plugin.logger.info("Chat Message (${chatMessage.plainText()}) has not been saved!")
        }
    }

    fun <T : Any> T.getPrivateProperty(variableName: String): Any? {
        return javaClass.getDeclaredField(variableName).let { field ->
            field.isAccessible = true
            return@let field.get(this)
        }
    }
}