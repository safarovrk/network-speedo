package com.example.networkspeedo.presentation.speedo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networkspeedo.domain.GetLoadEndpointUseCase
import com.example.networkspeedo.domain.GetLoadSpeedStateUseCase
import com.example.networkspeedo.domain.GetUploadEndpointUseCase
import com.example.networkspeedo.domain.GetUploadSpeedStateUseCase
import fr.bmartel.speedtest.SpeedTestReport
import fr.bmartel.speedtest.SpeedTestSocket
import fr.bmartel.speedtest.inter.ISpeedTestListener
import fr.bmartel.speedtest.model.SpeedTestError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SpeedoViewModel @Inject constructor(
    private val getLoadSpeedStateUseCase: GetLoadSpeedStateUseCase,
    private val getLoadEndpointUseCase: GetLoadEndpointUseCase,
    private val getUploadSpeedStateUseCase: GetUploadSpeedStateUseCase,
    private val getUploadEndpointUseCase: GetUploadEndpointUseCase
) : ViewModel() {

    companion object {
        private const val MEGA_BIT_DIV = 1000000
        private const val UPLOAD_FILE_SIZE_BYTES = 100000000
    }
    private val _speedoState = MutableLiveData<SpeedResponseStates<SpeedoState>>()
    val speedoState: LiveData<SpeedResponseStates<SpeedoState>> = _speedoState

    val loadSpeedTestSocket: SpeedTestSocket = SpeedTestSocket()
    val uploadSpeedTestSocket: SpeedTestSocket = SpeedTestSocket()

    init {
        loadSpeedTestSocket.addSpeedTestListener(object : ISpeedTestListener {
            override fun onCompletion(report: SpeedTestReport) {
                viewModelScope.launch(Dispatchers.Main) {
                    if (_speedoState.value is SpeedResponseStates.Success) {
                        _speedoState.value = SpeedResponseStates.Success(
                            (_speedoState.value as SpeedResponseStates.Success).data.copy(
                                loadMeasuredSpeed = report.transferRateBit.toFloat()
                                    .div(MEGA_BIT_DIV)
                            )
                        )
                    }
                    withContext(Dispatchers.IO) {
                        if (getUploadSpeedStateUseCase.execute()) {
                            uploadSpeedTestSocket.startUpload(
                                getUploadEndpointUseCase.execute(),
                                UPLOAD_FILE_SIZE_BYTES
                            )
                        }
                    }
                }
            }

            override fun onError(speedTestError: SpeedTestError, errorMessage: String) {
                viewModelScope.launch(Dispatchers.Main) {
                    _speedoState.value = SpeedResponseStates.Failure(
                        Exception(speedTestError.name + ": " + errorMessage)
                    )
                }
            }

            override fun onProgress(percent: Float, report: SpeedTestReport) {
                viewModelScope.launch(Dispatchers.Main) {
                    if (_speedoState.value is SpeedResponseStates.Success) {
                        _speedoState.value = SpeedResponseStates.Success(
                            (_speedoState.value as SpeedResponseStates.Success).data.copy(
                                loadMomentPercent = percent.toInt()
                            )
                        )
                    }
                    if (_speedoState.value is SpeedResponseStates.Success) {
                        _speedoState.value = SpeedResponseStates.Success(
                            (_speedoState.value as SpeedResponseStates.Success).data.copy(
                                loadMomentSpeed = report.transferRateBit.toFloat()
                                    .div(MEGA_BIT_DIV)
                            )
                        )
                    }
                }
            }
        })

        uploadSpeedTestSocket.addSpeedTestListener(object : ISpeedTestListener {
            override fun onCompletion(report: SpeedTestReport) {
                viewModelScope.launch(Dispatchers.Main) {
                    if (_speedoState.value is SpeedResponseStates.Success) {
                        _speedoState.value = SpeedResponseStates.Success(
                            (_speedoState.value as SpeedResponseStates.Success).data.copy(
                                uploadMeasuredSpeed = report.transferRateBit.toFloat()
                                    .div(MEGA_BIT_DIV)
                            )
                        )
                    }
                }
            }

            override fun onError(speedTestError: SpeedTestError, errorMessage: String) {
                viewModelScope.launch(Dispatchers.Main) {
                    _speedoState.value = SpeedResponseStates.Failure(
                        Exception(speedTestError.name + ": " + errorMessage)
                    )
                }
            }

            override fun onProgress(percent: Float, report: SpeedTestReport) {
                viewModelScope.launch(Dispatchers.Main) {
                    if (_speedoState.value is SpeedResponseStates.Success) {
                        _speedoState.value = SpeedResponseStates.Success(
                            (_speedoState.value as SpeedResponseStates.Success).data.copy(
                                uploadMomentPercent = percent.toInt()
                            )
                        )
                    }

                    if (_speedoState.value is SpeedResponseStates.Success) {
                        _speedoState.value = SpeedResponseStates.Success(
                            (_speedoState.value as SpeedResponseStates.Success).data.copy(
                                uploadMomentSpeed = report.transferRateBit.toFloat()
                                    .div(MEGA_BIT_DIV)
                            )
                        )
                    }
                }
            }
        })
    }

    fun onMeasureClicked() {
        _speedoState.value = SpeedResponseStates.Empty()
        _speedoState.value = SpeedResponseStates.Success(
            SpeedoState(
                null,
                null,
                null,
                null,
                null,
                null
            )
        )
        viewModelScope.launch(Dispatchers.IO) {
            try {
                loadSpeedTestSocket.forceStopTask()
                uploadSpeedTestSocket.forceStopTask()

                if (getLoadSpeedStateUseCase.execute()) {
                    loadSpeedTestSocket.startDownload(getLoadEndpointUseCase.execute())
                } else {
                    if (getUploadSpeedStateUseCase.execute()) {
                        uploadSpeedTestSocket.startUpload(
                            getUploadEndpointUseCase.execute(),
                            UPLOAD_FILE_SIZE_BYTES
                        )
                    }
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    _speedoState.value = SpeedResponseStates.Failure(e)
                }
            }
        }
    }
}