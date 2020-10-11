package ru.art241111.tcpconnection.client.reader

import android.util.Log
import ru.art241111.tcpconnection.client.reader.handlers.HandlerImp
import java.io.BufferedReader
import java.lang.Exception
import java.net.Socket
import kotlin.concurrent.thread

class RemoteReader: RemoteReaderImp {
    private val handlers: MutableList<HandlerImp> = mutableListOf()

    private var isReading = false
    private lateinit var reader: BufferedReader

    override fun addHandlers(handlers: List<HandlerImp>){
        this.handlers.addAll(handlers)
    }

    /**
     * Stop reading track
     */
    override fun stop(){
        isReading = false
        handlers.clear()

        if(::reader.isInitialized && isReading){
            thread {
                reader.close()
            }
        }
    }

    /**
     * Start reading from InputStream
     */
    override fun start(socket: Socket) {
        reader = socket.getInputStream().bufferedReader()
        isReading = true

        thread {
            startTrackingInputString(reader)
        }
    }

    private fun startTrackingInputString(reader: BufferedReader){
        while (isReading){
            try {
                if(reader.ready()){
                    val line = reader.readLine()
                    handlers.forEach {
                        it.handle(line)
                    }
                }
            } catch (e: NullPointerException) {
                Log.e("reader", "No elements come", e)
            } catch (e: Exception){
                Log.e("reader", "Unknown error", e)
            }
        }
    }
}