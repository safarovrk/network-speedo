package com.example.networkspeedo.presentation.speedo

sealed class SpeedResponseStates<T>() {
    class Empty<T> : SpeedResponseStates<T>()

    class Success<T>(val data: T) : SpeedResponseStates<T>()

    class Failure<T>(val e: Exception) : SpeedResponseStates<T>()
}