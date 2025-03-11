package com.example.coroutineflow.cryptoApp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.coroutineflow.databinding.ActivityCryptoBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CryptoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCryptoBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[CryptoViewModel::class.java]
    }

    private val adapter = CryptoAdapter()


    private var job : Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        observeViewModel()
    }

    override fun onPause() {
        super.onPause()
        stopObserving()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCurrencyPriceList.adapter = adapter
        binding.recyclerViewCurrencyPriceList.itemAnimator = null
    }

    private fun observeViewModel() {
        job = lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {
                    is State.Initial -> {
                        binding.progressBarLoading.isVisible = false
                    }

                    is State.Loading -> {
                        binding.progressBarLoading.isVisible = true
                    }

                    is State.Content -> {
                        binding.progressBarLoading.isVisible = false
                        adapter.submitList(it.currencyList)
                    }
                }
            }
        }
    }

    private fun stopObserving() {
        job?.cancel()
    }

    companion object {

        fun newIntent(context: Context) = Intent(context, CryptoActivity::class.java)
    }
}