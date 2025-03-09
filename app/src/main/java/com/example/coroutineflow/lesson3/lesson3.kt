package com.example.coroutineflow.lesson3

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

suspend fun main() {

    val result = getFlowByBuilderFlow().filter { it.isPrime() }
        .filter { it > 20 }
        .map {
            println("map")
            "number: $it"
        }
        .first()

    println(result)
}

fun getFlowByFlowOfBuilder(): Flow<Int> {
    return flowOf(3, 4, 8, 16, 5, 17, 11, 32, 41, 28, 43, 47, 84, 116, 53, 29, 61)
}

fun getFlowByBuilderFlow(): Flow<Int> {
    val fisrtFlow = getFlowByFlowOfBuilder()
    return flow {
//        fisrtFlow.collect {
//            println("Emitted from first flow: $it")
//            emit(it)
//        }
        emitAll(fisrtFlow)
    }
}

suspend fun Int.isPrime(): Boolean {
    if (this <= 1) return false

    for (i in 2..this / 2) {
        delay(50)
        if (this % i == 0) return false
    }
    return true
}