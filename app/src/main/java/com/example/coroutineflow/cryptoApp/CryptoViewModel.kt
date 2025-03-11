package com.example.coroutineflow.cryptoApp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class CryptoViewModel : ViewModel() {

    private val repository = CryptoRepository

    val state: Flow<State> = loadData()

    private fun loadData() =
        repository.getCurrencyFlow()
            .filter { it.isNotEmpty() }
            .map { State.Content(it) as State }
            .onStart { emit(State.Loading) }
            .onEach { Log.d("CryptoViewModel", "onEach") }
            .onCompletion { Log.d("CryptoViewModel", "onCompletion") }

}