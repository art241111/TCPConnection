package ru.art241111.tcpconnection.client.writer

import java.net.Socket

interface RemoteWriterImp: WriteImp {
    override fun send(text: String)
    fun start(socket: Socket)
    fun stop(stopCommand: String = "q")
}