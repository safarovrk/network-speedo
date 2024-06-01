package com.example.networkspeedo.domain

sealed class ThemeState {
    object System : ThemeState()
    object Dark : ThemeState()
    object Light : ThemeState()
}