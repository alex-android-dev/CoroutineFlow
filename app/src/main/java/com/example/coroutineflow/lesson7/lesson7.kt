package com.example.coroutineflow.lesson7

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

suspend fun main() {

    val scope = CoroutineScope(Dispatchers.Default)

    val flow = MutableStateFlow(0)

    val reducer = scope.launch {
        delay(100)
        repeat(10) {
            println("Emitted $it")
            flow.emit(it)
            println("After emit")
            delay(200)
        }
    }

    val consumer = scope.launch {
        flow.collectLatest {
            println("Collecting started: $it")
            delay(5000) // Очень долгая загрузка
            println("Collecting finished: $it")
        }
    }

    reducer.join()
    consumer.join()

}