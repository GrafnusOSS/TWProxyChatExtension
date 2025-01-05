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

    /**
     * Initializes the PluginMessageListener for the main channel.
     *
     * This registers the listener for incoming plugin messages on the "playerchatchanel:main" channel.
     * This channel is used for general communication between the proxy and the server for player chat history.
     */
    fun initialize() {
        Bukkit.getServer().messenger.registerIncomingPluginChannel(plugin, "playerchatchanel:main", this)
    }

    /**
     * Destroys the PluginMessageListener for the main channel.
     * This is used when the plugin is disabled to prevent any memory leaks.
     */
    fun destroy() {
        Bukkit.getServer().messenger.unregisterIncomingPluginChannel(plugin)
    }

    /**
     * Handles incoming plugin messages on the specified channel.
     *
     * @param channel the name of the channel the message was received on
     * @param player the player associated with the message
     * @param message the byte array containing the message data
     *
     * The method checks if the message is null and returns early if so. It then reads the sub-channel
     * from the message. If the sub-channel is "ProxyChat", it proceeds to read and deserialize the
     * message content as a Component and attempts to insert the chat message into the player's history
     * if not already present.
     */
    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray?) {
        if (message == null) {
            return
        }
        val input: ByteArrayDataInput = ByteStreams.newDataInput(message)
        val subChannel: String = input.readUTF()
        if (subChannel.equals("ProxyChat")) {
            val messageStr: String = input.readUTF()
            val messageComp: Component = JSONComponentSerializer.json().deserialize(messageStr)

            insertChatMessageIfNotPresent(player, messageComp)
        }
    }

    /**
     * Insert a chat message into the player's chat history, but only if that message
     * isn't already present.
     *
     * @param player the player whose chat history we're modifying
     * @param chatMessage the message to add to the player's chat history
     */
    private fun insertChatMessageIfNotPresent(player: Player, chatMessage: Component) {
        val chatHistory = chatHistoryHandler.getHistory(player.uniqueId)

        if(!chatHistory.hasMessage(chatMessage)) {
            chatHistory.addMessage(chatMessage)
        }
    }
}