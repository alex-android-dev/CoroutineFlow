package com.example.coroutineflow.teamScoreApp

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TeamScoreViewModel : ViewModel() {

    private val _state = MutableStateFlow<TeamScoreState>(TeamScoreState.Game(0, 0))
    val state = _state.asStateFlow()

    fun increaseScore(team: Team) {

        Log.d("TeamScoreViewModel", "increaseScore before collect team: $team")

        val currentState = _state.value

        Log.d(
            "TeamScoreViewModel",
            "increaseScore after collect currentState: $currentState"
        )

        if (currentState is TeamScoreState.Game) {
            if (team == Team.TEAM_1) {
                val oldValue = currentState.score1
                val newValue = oldValue + 1

                _state.value = currentState.copy(score1 = newValue)

                if (newValue >= WINNER_SCORE) {
                    val stateWinner = TeamScoreState.Winner(
                        winnerTeam = Team.TEAM_1,
                        newValue,
                        currentState.score2
                    )
                    _state.value = stateWinner
                }


            } else {
                val oldValue = currentState.score2
                val newValue = oldValue + 1

                _state.value = currentState.copy(score2 = newValue)

                if (newValue >= WINNER_SCORE) {
                    val stateWinner = TeamScoreState.Winner(
                        winnerTeam = Team.TEAM_2,
                        currentState.score1,
                        newValue
                    )
                    _state.value = stateWinner
                }
            }
        }

    }

    companion object {
        private const val WINNER_SCORE = 7
    }

}

