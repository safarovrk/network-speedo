package com.example.networkspeedo.presentation.settings

sealed class StorageResponseStates<T>() {
    class Loading<T> : StorageResponseStates<T>()

    class Success<T>(val data: T) : StorageResponseStates<T>()

    class Failure<T>(val e: Exception) : StorageResponseStates<T>()
}