package com.example.coroutineflow.teamScoreApp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class TeamScoreViewModel : ViewModel() {

    private val _state = MutableSharedFlow<TeamScoreState>(replay = 1)
    val state = _state.asSharedFlow()

    init {
        val initState = TeamScoreState.Game(0, 0)
        viewModelScope.launch {
            _state.emit(initState)
        }
        viewModelScope.launch {
            _state.collect {
                Log.d("TeamScoreViewModel", "init state: $it")
            }
        }
    }

    suspend fun increaseScore(team: Team) {

        Log.d("TeamScoreViewModel", "increaseScore before collect team: $team")

        _state.replayCache.firstOrNull().let { currentState ->

            Log.d(
                "TeamScoreViewModel",
                "increaseScore after collect currentState: $currentState"
            )

            if (currentState is TeamScoreState.Game) {
                if (team == Team.TEAM_1) {
                    val oldValue = currentState.score1
                    val newValue = oldValue + 1

                    if (newValue >= WINNER_SCORE) {
                        val stateWinner = TeamScoreState.Winner(
                            winnerTeam = Team.TEAM_1,
                            newValue,
                            currentState.score2
                        )
                        _state.emit(stateWinner)
                    } else {
                        _state.emit(currentState.copy(score1 = newValue))
                    }

                } else {
                    val oldValue = currentState.score2
                    val newValue = oldValue + 1

                    if (newValue >= WINNER_SCORE) {
                        val stateWinner = TeamScoreState.Winner(
                            winnerTeam = Team.TEAM_2,
                            currentState.score1,
                            newValue
                        )
                        _state.emit(stateWinner)
                    } else {
                        _state.emit(currentState.copy(score2 = newValue))
                    }

                }
            }

        }

    }

    companion object {
        private const val WINNER_SCORE = 7
    }
}