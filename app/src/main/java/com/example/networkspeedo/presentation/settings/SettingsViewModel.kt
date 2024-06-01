package com.example.networkspeedo.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.networkspeedo.domain.GetLoadEndpointUseCase
import com.example.networkspeedo.domain.GetLoadSpeedStateUseCase
import com.example.networkspeedo.domain.GetUploadEndpointUseCase
import com.example.networkspeedo.domain.GetUploadSpeedStateUseCase
import com.example.networkspeedo.domain.ThemeState
import com.example.networkspeedo.domain.settings.GetThemeStateUseCase
import com.example.networkspeedo.domain.settings.SaveLoadEndpointUseCase
import com.example.networkspeedo.domain.settings.SaveLoadSpeedUseCase
import com.example.networkspeedo.domain.settings.SaveThemeUseCase
import com.example.networkspeedo.domain.settings.SaveUploadEndpointUseCase
import com.example.networkspeedo.domain.settings.SaveUploadSpeedUseCase
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val saveThemeUseCase: Lazy<SaveThemeUseCase>,
    private val saveLoadEndpointUseCase: Lazy<SaveLoadEndpointUseCase>,
    private val saveUploadEndpointUseCase: Lazy<SaveUploadEndpointUseCase>,
    private val saveLoadSpeedUseCase: Lazy<SaveLoadSpeedUseCase>,
    private val saveUploadSpeedUseCase: Lazy<SaveUploadSpeedUseCase>,
    private val getThemeStateUseCase: GetThemeStateUseCase,
    private val getLoadEndpointUseCase: GetLoadEndpointUseCase,
    private val getUploadEndpointUseCase: GetUploadEndpointUseCase,
    private val getLoadSpeedStateUseCase: GetLoadSpeedStateUseCase,
    private val getUploadSpeedStateUseCase: GetUploadSpeedStateUseCase
) : ViewModel() {

    private val _settingsState = MutableLiveData<StorageResponseStates<SettingsState>>()
    val settingsState: LiveData<StorageResponseStates<SettingsState>> = _settingsState

    fun setupState() {
        viewModelScope.launch {
            _settingsState.value = StorageResponseStates.Loading()
            try {
                var settingsState: SettingsState
                with(Dispatchers.IO) {
                    val themeState = getTheme()
                    val loadEndpoint = getLoadEndpoint()
                    val uploadEndpoint = getUploadEndpoint()
                    val loadCheckboxState = getLoadCheckboxState()
                    val uploadCheckboxState = getUploadCheckboxState()
                    settingsState =
                        SettingsState(
                            themeState,
                            loadEndpoint,
                            uploadEndpoint,
                            loadCheckboxState,
                            uploadCheckboxState
                        )
                }
                _settingsState.value = StorageResponseStates.Success(settingsState)
            } catch (e: Exception) {
                _settingsState.value = StorageResponseStates.Failure(e)
            }
        }
    }

    fun onThemeSelected(theme: ThemeState) {
        viewModelScope.launch { saveThemeUseCase.get().execute(theme) }
    }

    fun onSaveLoadEndpointButtonClicked(endpoint: String) {
        viewModelScope.launch { saveLoadEndpointUseCase.get().execute(endpoint) }
    }

    fun onSaveUploadEndpointButtonClicked(endpoint: String) {
        viewModelScope.launch { saveUploadEndpointUseCase.get().execute(endpoint) }
    }

    fun onLoadCheckboxChanged(value: Boolean) {
        viewModelScope.launch { saveLoadSpeedUseCase.get().execute(value) }
    }

    fun onUploadCheckboxChanged(value: Boolean) {
        viewModelScope.launch { saveUploadSpeedUseCase.get().execute(value) }
    }

    private suspend fun getTheme(): ThemeState = getThemeStateUseCase.execute()

    private suspend fun getLoadEndpoint(): String = getLoadEndpointUseCase.execute()

    private suspend fun getUploadEndpoint(): String = getUploadEndpointUseCase.execute()

    private suspend fun getLoadCheckboxState(): Boolean = getLoadSpeedStateUseCase.execute()

    private suspend fun getUploadCheckboxState(): Boolean = getUploadSpeedStateUseCase.execute()

}