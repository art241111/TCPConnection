package ru.art241111.tcpconnection.client

import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.art241111.tcpconnection.client.connection.Connection
import ru.art241111.tcpconnection.client.connection.Status
import ru.art241111.tcpconnection.client.reader.RemoteReader
import ru.art241111.tcpconnection.client.reader.RemoteReaderImp
import ru.art241111.tcpconnection.client.reader.handlers.HandlerImp
import ru.art241111.tcpconnection.client.writer.RemoteWriter
import ru.art241111.tcpconnection.client.writer.RemoteWriterImp
import java.net.Socket

/**
 * TCP client.
 * @author Artem Gerasimov.
 */
class Client(): WriteImp{
    private val connection = Connection()
    private val remoteReader: RemoteReaderImp = RemoteReader()
    private val remoteWriter: RemoteWriterImp = RemoteWriter()

    private val handlers: MutableList<HandlerImp> = mutableListOf()

    private val connectStatus: LiveData<Status> = connection.getConnectStatus()
    /**
     * @return connect status
     */
    fun getConnectStatus(): LiveData<Status> = connectStatus

    @Suppress("unused")
    fun addHandlers(handlers: List<HandlerImp>) {
        this.handlers.addAll(handlers)
    }

    @Suppress("unused")
    fun removeHandlers(handlers: List<HandlerImp>) {
        this.handlers.removeAll(handlers)
    }

    /**
     * Connect to TCP sever.
     * @param address - server ip port,
     * @param port - server port.
     */
    fun connect(address: String, port: Int){
        GlobalScope.launch {
            // Connect to tcp server
            connection.connect(address, port)

            // When the device connects to the server, it creates Reader and Writer
            if(connection.value != null && connection.value!!.isConnected){
                startReadingAndWriting(socket = connection.value!!)

                // Add handlers to Reader
                remoteReader.addHandlers(handlers)
            }
        }
    }

    /**
     * Disconnect from TCP sever.
     */
    fun disconnect(){
        remoteReader.destroyReader()
        remoteWriter.destroyWriter("EXIT")

        Thread.sleep(50L)
        connection.disconnect()
    }

    /**
     * Send text to the server.
     */
    override fun send(text: String) {
        remoteWriter.send(text)
    }

    private fun startReadingAndWriting(socket: Socket) {
        remoteReader.createReader(socket)
        remoteWriter.createWriter(socket)
    }
}