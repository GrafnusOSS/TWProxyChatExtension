package de.grafnus.typewriter

import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.ServerConnection
import com.velocitypowered.api.proxy.messages.ChannelIdentifier
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.ComponentSerializer
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import java.util.Optional
import java.util.UUID
import java.lang.reflect.Modifier.TRANSIENT

class MessagingHandler: KoinComponent {
    private val plugin: VelocityChat by inject()
    private val store: MessageStore by inject()


    fun handleNewMessage(player: UUID, message: Component) {
        store.addMessage(player, message)
        sendMessageToBackendServer(player, message)
    }

    fun handleServerSwitch(player: UUID) {
        val playerMessageHistory: List<Component> = store.getHistory(player)
        playerMessageHistory.forEach { message -> sendMessageToBackendServer(player, message) }
    }

    @Subscribe
    private fun sendMessageToBackendServer(playerUUID: UUID, message: Component) {
        val proxyServer: ProxyServer = plugin.proxy
        val player: Player = proxyServer.getPlayer(playerUUID).orElse(null) ?: return

        val data: ByteArrayDataOutput = ByteStreams.newDataOutput()
        data.writeUTF("ProxyChat")

        //var gson: Gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().excludeFieldsWithModifiers(TRANSIENT).create()

        val jsonMessage = JSONComponentSerializer.json().serialize(message)
//        plugin.logger.info("Sending Message:")
//        plugin.logger.info("Original: $message")
//        plugin.logger.info("Original: $jsonMessage")
        data.writeUTF(jsonMessage)


        if (player.currentServer == null) {
            plugin.logger.warn("Player $playerUUID should be online but isn't!")
            return
        }

        var connection: Optional<ServerConnection> = player.currentServer
        if (connection.isPresent) {
            if (!connection.get().sendPluginMessage(plugin.identifier, data.toByteArray())) {
                plugin.logger.warn("Could not transmit message from player $playerUUID (${player.username})!")
            }
        }
    }
}