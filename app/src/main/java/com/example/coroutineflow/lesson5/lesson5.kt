package com.example.coroutineflow.lesson5

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

val coroutineScope = CoroutineScope(Dispatchers.IO)

suspend fun main() {
//    val flow = getFlow()
    val flow = MutableSharedFlow<Int>()

    coroutineScope.launch {
        repeat(5) {
            println("Emmited $it")
            flow.emit(it)
            delay(1000)
        }
    }

    val job1 = coroutineScope.launch {
        flow.collect {
            println("First collect $it")
        }
    }

    delay(5000)

    val job2 = coroutineScope.launch {
        flow.collect {
            println("Second collect $it")
        }
    }

    job1.join()
    job2.join()
}

fun getFlow(): Flow<Int> = flow {
    repeat(5) {
        println("Emmited $it")
        emit(it)
        delay(1000)
    }
}