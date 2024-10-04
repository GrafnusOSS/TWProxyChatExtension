import com.google.common.io.ByteArrayDataInput
import com.google.common.io.ByteStreams
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.plugin.messaging.PluginMessageListener
import org.koin.java.KoinJavaComponent
import util.TWClassHolder

class ChannelHandler : PluginMessageListener {


    fun initialize() {
        Bukkit.getServer().messenger.registerOutgoingPluginChannel(TWClassHolder.get(), "PlayerChatChanel")
        Bukkit.getServer().messenger.registerIncomingPluginChannel(TWClassHolder.get(), "PlayerChatChanel", this)

    }

    fun destroy() {
        Bukkit.getServer().messenger.unregisterOutgoingPluginChannel(TWClassHolder.get())
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
            TWClassHolder.get()
        }
    }

}