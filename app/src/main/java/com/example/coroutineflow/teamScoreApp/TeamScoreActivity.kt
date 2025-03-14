package com.example.coroutineflow.teamScoreApp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.coroutineflow.databinding.ActivityTeamScoreBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TeamScoreActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityTeamScoreBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[TeamScoreViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        observeViewModel()
        setupListeners()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect { currentState ->
                when (currentState) {
                    is TeamScoreState.Game -> {
                        binding.team1Score.text = currentState.score1.toString()
                        binding.team2Score.text = currentState.score2.toString()
                    }

                    is TeamScoreState.Winner -> {
                        binding.team1Score.text = currentState.score1.toString()
                        binding.team2Score.text = currentState.score2.toString()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@TeamScoreActivity,
                                "Winner: ${currentState.winnerTeam}",
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.team1Logo.setOnClickListener {
            Log.d("TeamScoreActivity", "team1Logo clicked")
            viewModel.increaseScore(Team.TEAM_1)

        }
        binding.team2Logo.setOnClickListener {
            Log.d("TeamScoreActivity", "team2Logo clicked")
            viewModel.increaseScore(Team.TEAM_2)
        }

    }

    companion object {
        fun newIntent(context: Context) = Intent(context, TeamScoreActivity::class.java)
    }

}