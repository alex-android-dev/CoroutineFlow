package com.example.coroutineflow.cryptoApp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

object CryptoRepository {

    private val currencyNames = listOf("BTC", "ETH", "USDT", "BNB", "USDC")
    private val currencyList = mutableListOf<Currency>()

    private val refreshEvents = MutableSharedFlow<Unit>()

    private val scope = CoroutineScope(Dispatchers.IO)

    val сurrencyListFlow : Flow<List<Currency>> = flow {
        delay(3000) // иммитация долгой загрузки
        generateCurrencyFlow()
        emit(currencyList.toList())

        refreshEvents.collect {
            delay(3000) // иммитация долгой загрузки
            generateCurrencyFlow()
            emit(currencyList.toList())
        }
    }.stateIn(
        scope = scope,
        started = SharingStarted.Lazily,
        initialValue = currencyList.toList()
    )

    suspend fun refreshList() {
        refreshEvents.emit(Unit)
    }

    private fun generateCurrencyFlow() {
        val prices: List<Int> = buildList {
            repeat(currencyNames.size) {
                add(Random.nextInt(1000, 2000))
            }
        }

        val list = buildList {
            for ((index, currencyName) in currencyNames.withIndex()) {
                val price = prices[index]
                val currency = Currency(name = currencyName, price = price)
                add(currency)
            }
        }

        currencyList.clear()
        currencyList.addAll(list)
    }
}
