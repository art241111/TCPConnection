package ru.art241111.tcpconnection.client.connection

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

/**
 * Create connection class.
 * @author Artem Gerasimov.
 */
class Connection: LiveData<Socket>(),
                  ConnectInt {
    // Connect status.
    private val connectStatus: MutableLiveData<Status> = MutableLiveData()
    override fun getConnectStatus(): LiveData<Status> = connectStatus

    /**
     * Create empty socket and set status DISCONNECTED
     */
    init {
        connectStatus.value = Status.DISCONNECTED
        this.postValue(Socket())
    }

    /**
     * Connect to server by TCP/IP.
     * If the connection did not occur within 2 seconds,
     * the error status is displayed.
     */
    override fun connect(address: String, port: Int){
        thread {
            if(connectStatus.value!! == Status.DISCONNECTED ||
                connectStatus.value!! == Status.ERROR){
                try {
                    // Set connecting status
                    connectStatus.postValue(Status.CONNECTING)

                    // Try to connect
                    this.value!!.connect(InetSocketAddress(address, port), 2000)

                    // If the connection is successful, we notify you about it
                    connectStatus.postValue(Status.COMPLETED)
                } catch (e: ConnectException){
                    Log.e("connection", "Fail connection", e)
                    connectStatus.postValue(Status.ERROR)
                } catch (e: SocketTimeoutException){
                    connectStatus.postValue(Status.ERROR)
                    this.postValue(Socket())
                }
            }
        }
    }

    /**
     * Disconnect from server.
     */
    override fun disconnect(){
        thread {
            if(connectStatus.value!! == Status.COMPLETED){
                this.value!!.close()
                connectStatus.postValue(Status.DISCONNECTED)
                this.postValue(Socket())
            }
        }
    }
}