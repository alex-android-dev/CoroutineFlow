package com.example.coroutineflow.cryptoApp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CryptoViewModel : ViewModel() {

    private val repository = CryptoRepository

    private val _state = MutableLiveData<State>(State.Initial)
    val state: LiveData<State> = _state

    init {
        loadData()
    }

    private fun loadData() {
        repository.getCurrencyFlow()
            .onStart {
                val currentState = _state.value

                if (currentState !is State.Content || currentState.currencyList.isEmpty()) {
                    _state.value = State.Loading
                }
            }
            // При старте устанавливаем стейт загрузки
            .filter { listOfCurrency ->
                listOfCurrency.isNotEmpty()
            }
            // Если прилетит пустой список, то он будет отфильтрован и метод onEach вызван не будет
            .onEach { listOfCurrency ->
                _state.value = State.Content(listOfCurrency)
            }
            .launchIn(viewModelScope)
    }
}