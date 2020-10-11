package ru.art241111.tcpconnection.client

import android.app.Activity
import androidx.lifecycle.LiveData
import ru.art241111.tcpconnection.MainActivity
import ru.art241111.tcpconnection.client.connection.ConnectInt
import ru.art241111.tcpconnection.client.connection.Connection
import ru.art241111.tcpconnection.client.connection.Status
import ru.art241111.tcpconnection.client.reader.RemoteReader
import ru.art241111.tcpconnection.client.reader.RemoteReaderImp
import ru.art241111.tcpconnection.client.reader.handlers.HandlerImp
import ru.art241111.tcpconnection.client.writer.RemoteWriter
import ru.art241111.tcpconnection.client.writer.RemoteWriterImp
import ru.art241111.tcpconnection.client.writer.WriteImp
import java.net.Socket
import kotlin.concurrent.thread

class Client(private val activity: Activity): ConnectInt, WriteImp{
    private val socketLiveData = Connection()
    private val remoteReader: RemoteReaderImp = RemoteReader()
    private val remoteWriter: RemoteWriterImp = RemoteWriter()

    private val connectStatus: LiveData<Status> = socketLiveData.getConnectStatus()
    override fun getConnectStatus(): LiveData<Status> = connectStatus

    private val handlers: MutableList<HandlerImp> = mutableListOf()

    @Suppress("unused")
    fun addHandler(handler: HandlerImp) {
        handlers.add(handler)
    }

    @Suppress("unused")
    fun addHandlers(handlers: List<HandlerImp>) {
        this.handlers.addAll(handlers)
    }

    @Suppress("unused")
    fun removeHandler(handler: HandlerImp) {
        handlers.remove(handler)
    }

    @Suppress("unused")
    fun removeHandlers(handlers: List<HandlerImp>) {
        this.handlers.removeAll(handlers)
    }

    override fun connect(address: String, port: Int){
        socketLiveData.connect(address, port)

        connectStatus.observe(activity as MainActivity, {
            if(it == Status.COMPLETED
                && socketLiveData.value != null){
                startReadingAndWriting(socket = socketLiveData.value!!)
            }
        })

        remoteReader.addHandlers(handlers)
    }

    override fun disconnect(){
        thread {
            remoteReader.stop()
            remoteWriter.stop("EXIT")

            Thread.sleep(50L)
            socketLiveData.disconnect()
        }
    }

    override fun send(text: String) {
        thread {
            remoteWriter.send(text)
        }
    }

    private fun startReadingAndWriting(socket: Socket) {
        remoteReader.start(socket)
        remoteWriter.start(socket)
    }
}