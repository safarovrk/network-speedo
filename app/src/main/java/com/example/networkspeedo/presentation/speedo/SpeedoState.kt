package com.example.networkspeedo.presentation.speedo

data class SpeedoState (
    val loadMomentSpeed: Float?,
    val loadMomentPercent: Int?,
    val loadMeasuredSpeed: Float?,
    val uploadMomentSpeed: Float?,
    val uploadMomentPercent: Int?,
    val uploadMeasuredSpeed: Float?
)