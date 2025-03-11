package com.example.coroutineflow.lesson4

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

val coroutineScope = CoroutineScope(Dispatchers.IO)

suspend fun main() {
    val flow = getFlow()

    // Flow прекратит своё существование после того как выведется первый элемент
    val job1 = coroutineScope.launch {
        flow.first().let {
            println(it)
        }
    }

    delay(5000)

    val job2 = coroutineScope.launch {
        flow.collect {
            println(it)
        }
    }

    job1.join()
    job2.join()
}

fun getFlow(): Flow<Int> = flow {
    repeat(100) {
        println("Emmited $it")
        emit(it)
        delay(1000)
    }
}