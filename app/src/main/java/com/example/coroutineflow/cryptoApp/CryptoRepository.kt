package com.example.coroutineflow.cryptoApp

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

object CryptoRepository {

    private val currencyNames = listOf("BTC", "ETH", "USDT", "BNB", "USDC")
    private val currencyList = mutableListOf<Currency>()

    fun getCurrencyFlow(): Flow<List<Currency>> = flow {
        while (true) {
            delay(3000) // иммитация долгой загрузки

            generateCurrencyFlow()
            emit(currencyList.toList())

            delay(3000) // условие бизнес логики. Автообновление должно происходить каждые 3 секунды
        }
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
