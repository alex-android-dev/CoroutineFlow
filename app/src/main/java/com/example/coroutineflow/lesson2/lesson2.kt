package com.example.coroutineflow.lesson2

import com.example.coroutineflow.lesson3.getFlowByBuilderFlow
import com.example.coroutineflow.lesson3.isPrime
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

suspend fun main() {

    getFlowByBuilderFlow().filter { it.isPrime() }
        .filter { it > 20 }
        .map {
            println("map")
            "number: $it"
        }
        .collect {
            println(it)
        }

}

fun getFlowByFlowOfBuilder(): Flow<Int> {
    return flowOf(3, 4, 8, 16, 5, 17, 11, 32, 41, 28, 43, 47, 84, 116, 53, 29, 61)
}

fun getFlowByBuilderFlow(): Flow<Int> {
    val numbers = listOf(3, 4, 8, 16, 5, 17, 11, 32, 41, 28, 43, 47, 84, 116, 53, 29, 61)
    return flow {
        numbers.forEach {
            emit(it)
        }
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