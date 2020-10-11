package ru.art241111.tcpconnection.client.connection

/**
 * ENUM class that describes
 * the possible connection status.
 *
 * @author Artem Gerasimov.
 */
enum class Status {
    DISCONNECTED,
    CONNECTING,
    COMPLETED,
    ERROR
}
