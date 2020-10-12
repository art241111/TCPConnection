package ru.art241111.tcpconnection.client.writer

import java.net.Socket

/**
 * Interface that should implement the class
 * that will connect to send data to the server.
 * @author Artem Gerasimov.
 */
interface RemoteWriterImp {
    fun send(text: String)

    fun createWriter(socket: Socket)
    fun destroyWriter(stopCommand: String = "q")
}