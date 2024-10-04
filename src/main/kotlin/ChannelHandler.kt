import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import com.typewritermc.engine.paper.interaction.ChatHistoryHandler
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.messaging.PluginMessageListener
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import util.TWClassHolder

class ChannelHandler : PluginMessageListener, KoinComponent {
    private val chatHistoryHandler: ChatHistoryHandler by inject()

    fun initialize() {
        Bukkit.getServer().messenger.registerIncomingPluginChannel(TWClassHolder.get(), "PlayerChatChanel", this)
    }

    fun destroy() {
        Bukkit.getServer().messenger.unregisterIncomingPluginChannel(TWClassHolder.get())
    }

    override fun onPluginMessageReceived(channel: String, player: Player, message: ByteArray?) {
        if (!channel.equals("PlayerChatChanel")) {
            return;
        }
        if (message == null) {
            return
        }
        val input: ByteArrayDataInput = ByteStreams.newDataInput(message)
        val subChannel: String = input.readUTF()
        if (subChannel.equals("ProxyChat")) {
            val chatHistory = chatHistoryHandler.getHistory(player.uniqueId)
            chatHistory.addMessage(Component.text(input.readUTF()))
        }
    }

}