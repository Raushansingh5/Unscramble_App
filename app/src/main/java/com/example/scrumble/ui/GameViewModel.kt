package com.example.scrumble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.scrumble.data.MAX_NO_OF_WORDS
import com.example.scrumble.data.SCORE_INCREASE
import com.example.scrumble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    private lateinit var  currentWord: String


    private val usedWords = mutableSetOf<String>()

    init {
        resetGame()
    }

    fun updateUserGuess(guess: String) {
        userGuess = guess
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updatedScore=_uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)

        } else {
            _uiState.value = _uiState.value.copy(isGuessWrong = true)
        }
        updateUserGuess("")
    }

    fun skipWord() {
        updateGameState(_uiState.value.score)
        updateUserGuess("")
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    private fun updateGameState(newScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            _uiState.value = _uiState.value.copy(
                isGuessWrong = false,
                isGameOver = true,
                score = newScore
            )
        } else {
            _uiState.value = _uiState.value.copy(
                isGuessWrong = false,
                currentScrambledWord = pickRandomWordAndShuffle(),
                currentWordCount = _uiState.value.currentWordCount + 1,
                score = newScore
            )
        }
    }

    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        return if (usedWords.add(currentWord)) shuffleCurrentWord(currentWord) else pickRandomWordAndShuffle()
    }

    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray().apply { shuffle() }
        return if (String(tempWord) == word) shuffleCurrentWord(word) else String(tempWord)
    }


}
