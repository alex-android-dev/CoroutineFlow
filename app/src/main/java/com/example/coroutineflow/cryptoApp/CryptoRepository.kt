package com.example.coroutineflow.cryptoApp

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.random.Random

object CryptoRepository {

    private val currencyNames = listOf("BTC", "ETH", "USDT", "BNB", "USDC")
    private val currencyList = mutableListOf<Currency>()

    private val _currencyListFlow = MutableSharedFlow<List<Currency>>()
    val currencyListFlow = _currencyListFlow.asSharedFlow()


    suspend fun loadData() {
        delay(3000) // иммитация долгой загрузки

        generateCurrencyFlow()

        _currencyListFlow.emit(currencyList.toList())
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
