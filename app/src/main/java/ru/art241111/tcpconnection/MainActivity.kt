package ru.art241111.tcpconnection

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.art241111.tcpconnection.client.Client

class MainActivity : AppCompatActivity(){
    lateinit var client: Client
    private val address = "192.168.31.62"
    private val port = 9999

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        client = Client()

        client.getConnectStatus().observe(this, {
            Log.d("change_socket_status", it.toString())
        })
    }

    fun disconnect(view: View) {
        client.disconnect()
    }

    fun connect(view: View) {
        client.connect(address, port)

    }

    fun sendMessage(view: View) {
        client.send("Hello")
    }
}