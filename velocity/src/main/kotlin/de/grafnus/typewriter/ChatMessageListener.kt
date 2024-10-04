package de.grafnus.typewriter

import com.github.retrooper.packetevents.event.PacketListener
import com.github.retrooper.packetevents.event.PacketReceiveEvent
import com.github.retrooper.packetevents.protocol.packettype.PacketType
import com.github.retrooper.packetevents.protocol.player.User
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientChatMessage

class ChatMessageListener: PacketListener {
    override fun onPacketReceive(event: PacketReceiveEvent) {
        var user: User = event.user;
        if (event.packetType != PacketType.Play.Server.CHAT_MESSAGE)
            return
        var chatMessage: WrapperPlayClientChatMessage = WrapperPlayClientChatMessage(event)
        var message: String = chatMessage.message
        if (message.equals("ping", ignoreCase = true)) {
            user.sendMessage("pong")
            user.uuid.toString();
        }
    }
}