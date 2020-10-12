package ru.art241111.tcpconnection.client.writer

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.PrintWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class RemoteWriter: RemoteWriterImp {
    private var isWriting = false
    private lateinit var writer: PrintWriter

    override fun send(text: String) {
        GlobalScope.launch {
            sendCommand(text)
        }
    }

    override fun start(socket: Socket) {
        writer = PrintWriter(socket.getOutputStream(), true)
        isWriting = true
    }

    override fun stop(stopCommand: String) {
        if(::writer.isInitialized && isWriting){
            send(stopCommand)
            Thread.sleep(50L)
            writer.close()
        }

        isWriting = false
    }

    private fun sendCommand(text: String) {
        if(isWriting){
            writer.println(text)
        }
    }
}
