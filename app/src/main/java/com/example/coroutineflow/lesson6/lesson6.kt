package com.example.coroutineflow.lesson6

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    val scope = CoroutineScope(Dispatchers.Default)

    val job = scope.launch {
        val flow: Flow<Int> = flow {
            repeat(10) {
                println("Emitted $it")
                emit(it)
                println("After emit")
                delay(200)
            }
        }.buffer(1, BufferOverflow.DROP_LATEST)
        flow.collect {
            println("Collected: $it")
            delay(1000)
        }
    }
    job.join()
}