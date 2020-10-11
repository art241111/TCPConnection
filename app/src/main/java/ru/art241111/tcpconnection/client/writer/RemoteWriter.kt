package ru.art241111.tcpconnection.client.writer

import java.io.PrintWriter
import java.net.Socket
import java.util.*
import kotlin.concurrent.thread

class RemoteWriter: RemoteWriterImp {
    private var isWriting = false
    private lateinit var writer: PrintWriter
    private val commandsQueue: Queue<String> = LinkedList()

    override fun send(text: String) {
        commandsQueue.add(text)
    }

    override fun start(socket: Socket) {
        writer = PrintWriter(socket.getOutputStream(), true);
        isWriting = true
        queueHandler()
    }

    override fun stop(stopCommand: String) {
        if(::writer.isInitialized && isWriting){
            send(stopCommand)
            Thread.sleep(50L)
            writer.close()
        }

        isWriting = false
    }

    private fun queueHandler(){
        thread {
            while (isWriting){
                if(commandsQueue.isNotEmpty()){
                    val text = commandsQueue.poll()
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
