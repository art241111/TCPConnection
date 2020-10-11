package ru.art241111.tcpconnection.client.reader.handlers

import android.util.Log
import androidx.lifecycle.MutableLiveData

class ExampleHandler(val position: MutableLiveData<String>): HandlerImp {
    override fun handle(text: String) {
        Log.d("example_log_handler", text)
//        position.postValue(text)
    }
}