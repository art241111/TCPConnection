package ru.art241111.tcpconnection.client.connection

import androidx.lifecycle.LiveData

/**
 * Interface that should implement the
 * class that will connect to the server.
 * @author Artem Gerasimov.
 */
interface ConnectInt {
    suspend fun connect(address: String, port: Int)
    fun disconnect()

    fun getConnectStatus(): LiveData<Status>
}
