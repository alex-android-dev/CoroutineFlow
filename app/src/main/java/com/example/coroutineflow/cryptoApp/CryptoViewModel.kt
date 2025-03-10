package com.example.coroutineflow.cryptoApp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class CryptoViewModel : ViewModel() {

    private val repository = CryptoRepository

    private val _state = MutableLiveData<State>(State.Initial)
    val state: LiveData<State> = _state

    init {
        loadData()
    }

    private fun loadData() {
        repository.getCurrencyFlow()
            .filter { it.isNotEmpty() }
            .map { State.Content(it) as State}
            .onStart { emit(State.Loading) }
            .onEach { _state.value = it }
            .launchIn(viewModelScope)
    }

}