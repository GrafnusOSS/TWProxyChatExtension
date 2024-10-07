package de.grafnus.typewriter

import com.github.retrooper.packetevents.event.PacketEvent
import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.event.PacketSendEvent
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_19_3
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChatMessageListener: PacketListener, KoinComponent {

    private val handler: MessagingHandler by inject()
//    private val plugin: VelocityChat by inject()
//
//    override fun onPacketReceive(event: PacketReceiveEvent?) {
//        if (event == null) return
//        plugin.logger.info("Received Packet ${event.packetType.toString()}")
//
//        if (event.packetType == PacketType.Play.Server.CHAT_MESSAGE) {
//            plugin.logger.info("Received Packet ${event.packetType.toString()}")
//        }
//
//        if (event.packetType == PacketType.Play.Server.SYSTEM_CHAT_MESSAGE) {
//            plugin.logger.info("Received Packet ${event.packetType.toString()}")
//        }
//        if (event.packetType == PacketType.Play.Client.CHAT_MESSAGE) {
//            plugin.logger.info("Received Packet ${event.packetType.toString()}")
//        }
//
//    }

    // When the server sends a message to the player
    override fun onPacketSend(event: PacketSendEvent?) {
        if (event == null) return

        val component = findMessage(event) ?: return
        if (component is TextComponent && component.content() == "no-index") return

        handler.handleNewMessage(event.user.uuid, component)
    }
    private fun findMessage(event: PacketSendEvent): Component? {
        return when (event.packetType) {
            PacketType.Play.Server.CHAT_MESSAGE -> {
                val packet = WrapperPlayServerChatMessage(event)
                val message = packet.message as? ChatMessage_v1_19_3 ?: return packet.message.chatContent
                message.unsignedChatContent.orElseGet { message.chatContent }
            }

            PacketType.Play.Server.SYSTEM_CHAT_MESSAGE -> {
                val packet = WrapperPlayServerSystemChatMessage(event)
                if (packet.isOverlay) return null
                packet.message
            }

            else -> null
        }
    }
}