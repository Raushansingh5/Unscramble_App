package com.example.scrumble.ui

data class GameUiState (
    val currentScrambledWord:String="",
    val currentWordCount:Int=1,
    val score:Int=0,
    val isGuessWrong:Boolean=false,
    val isGameOver:Boolean=false
)