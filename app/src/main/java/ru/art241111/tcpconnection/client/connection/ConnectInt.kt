package ru.art241111.tcpconnection.client.connection

import androidx.lifecycle.LiveData

interface ConnectInt {
    fun connect(address: String, port: Int)
    fun disconnect()
    fun getConnectStatus(): LiveData<Status>
}
