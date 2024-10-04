package de.grafnus.typewriter

import java.util.UUID

class MessageStore(private val historyLimit: Int = 50) {
    private val playerMessageHistories: MutableMap<UUID, PlayerChatHistory> = mutableMapOf()

    fun addMessage(playerUUID: UUID, message: String) {
        getPlayerHistory(playerUUID).addMessage(message)
    }

    fun getHistory(playerUUID: UUID): List<String> {
        return getPlayerHistory(playerUUID).history
    }

    private fun getPlayerHistory(playerUUID: UUID): PlayerChatHistory {
        return playerMessageHistories.computeIfAbsent(playerUUID) { PlayerChatHistory(historyLimit) }
    }

    private inner class PlayerChatHistory(private val limit: Int) {
        private val messages: MutableList<String> = mutableListOf()

        fun addMessage(message: String) {
            messages.add(message)
            if (messages.size > limit) {
                messages.removeAt(0)
            }
        }

        val history: List<String>
            get() = messages
    }
}