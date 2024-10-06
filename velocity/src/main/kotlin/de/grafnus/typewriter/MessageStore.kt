package de.grafnus.typewriter

import net.kyori.adventure.text.Component
import java.util.UUID

class MessageStore(private val historyLimit: Int = 50) {
    private val playerMessageHistories: MutableMap<UUID, PlayerChatHistory> = mutableMapOf()

    fun addMessage(playerUUID: UUID, message: Component) {
        getPlayerHistory(playerUUID).addMessage(message)
    }

    fun getHistory(playerUUID: UUID): List<Component> {
        return getPlayerHistory(playerUUID).history
    }

    private fun getPlayerHistory(playerUUID: UUID): PlayerChatHistory {
        return playerMessageHistories.computeIfAbsent(playerUUID) { PlayerChatHistory(historyLimit) }
    }

    private inner class PlayerChatHistory(private val limit: Int) {
        private val messages: MutableList<Component> = mutableListOf()

        fun addMessage(message: Component) {
            messages.add(message)
            if (messages.size > limit) {
                messages.removeAt(0)
            }
        }

        val history: List<Component>
            get() = messages
    }
}