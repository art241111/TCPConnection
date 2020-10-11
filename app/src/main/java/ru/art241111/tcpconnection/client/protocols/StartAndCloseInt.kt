package ru.art241111.tcpconnection.client.protocols

import java.net.Socket

interface StartAndCloseInt {
    fun start(socket: Socket)
    fun stop()
}