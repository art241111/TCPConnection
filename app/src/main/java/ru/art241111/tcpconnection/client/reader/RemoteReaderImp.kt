package ru.art241111.tcpconnection.client.reader

import ru.art241111.tcpconnection.client.protocols.StartAndCloseInt
import ru.art241111.tcpconnection.client.reader.handlers.HandlerImp
import java.net.Socket

interface RemoteReaderImp: StartAndCloseInt {
    override fun stop(){ }
    override fun start(socket: Socket) { }

    fun addHandlers(handlers: List<HandlerImp>)
}