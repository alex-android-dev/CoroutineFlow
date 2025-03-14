package com.example.coroutineflow.cryptoApp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.coroutineflow.databinding.ActivityCryptoBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class CryptoActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCryptoBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[CryptoViewModel::class.java]
    }

    private val adapter = CryptoAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setPaddings()

        lifecycleScope

        setupRecyclerView()
        observeViewModel()

        binding.buttonRefreshList.setOnClickListener {
            viewModel.refreshList()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewCurrencyPriceList.adapter = adapter
        binding.recyclerViewCurrencyPriceList.itemAnimator = null
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state
                    .collect {
                        when (it) {
                            is State.Initial -> {
                                binding.progressBarLoading.isActivated = false
                            }

                            is State.Loading -> {
                                binding.buttonRefreshList.isEnabled = false
                                binding.progressBarLoading.isVisible = true
                            }

                            is State.Content -> {
                                binding.buttonRefreshList.isEnabled = true
                                binding.progressBarLoading.isVisible = false
                                adapter.submitList(it.currencyList)
                            }
                        }
                    }
            }

        }


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.state2
                    .collect {
                        when (it) {
                            is State.Content -> {
                                Log.d("CryptoActivity", it.currencyList.joinToString())
                            }
                            else -> ""
                        }
                    }
            }
        }


    }

    private fun setPaddings() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, CryptoActivity::class.java)
    }
}
