package ru.art241111.tcpconnection.client.writer

import java.io.PrintWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class RemoteWriter: RemoteWriterImp {
    private var isWriting = false
    private lateinit var writer: PrintWriter
    private val textQueue: Queue<String> = LinkedList()

    override fun send(text: String) {
        textQueue.add(text)
    }

    override fun start(socket: Socket) {
        textQueue.clear()
        writer = PrintWriter(socket.getOutputStream(), true)
        isWriting = true
        queueHandler()
    }

    override fun stop(stopCommand: String) {
        if(::writer.isInitialized && isWriting){
            send(stopCommand)
            Thread.sleep(50L)
            writer.close()
        }

        textQueue.clear()
        isWriting = false
    }

    private fun queueHandler(){
        thread {
            while (isWriting){
                if(textQueue.isNotEmpty()){
                    val text = textQueue.poll()
                    if(text != null)
                        sendCommand(text)
                }
            }
        }
    }

    private fun sendCommand(text: String) {
        if(isWriting){
            writer.println(text)
        }
    }
}
