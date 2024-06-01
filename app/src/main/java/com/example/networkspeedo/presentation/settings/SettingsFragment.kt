package com.example.networkspeedo.presentation.settings

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.example.networkspeedo.databinding.FragmentSettingsBinding
import com.example.networkspeedo.domain.ThemeState
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class SettingsFragment : Fragment() {

    companion object {
        private const val THEME_VARIANT_SYSTEM = 0
        private const val THEME_VARIANT_DARK = 1
        private const val THEME_VARIANT_LIGHT = 2

        private const val VIEW_FLIPPER_CONTENT = 0
        private const val VIEW_FLIPPER_LOADING = 1
        private const val VIEW_FLIPPER_ERROR = 2
    }

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var isSpinnerInitialized = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by createViewModelLazy(
        SettingsViewModel::class,
        { this.viewModelStore },
        factoryProducer = { viewModelFactory })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setupState()
        setStateObserver()
        setListeners()
    }

    private fun setStateObserver() {
        viewModel.settingsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is StorageResponseStates.Success -> {
                    when (state.data.themeState) {
                        ThemeState.System -> binding.themeSpinner.setSelection(THEME_VARIANT_SYSTEM)
                        ThemeState.Dark -> binding.themeSpinner.setSelection(THEME_VARIANT_DARK)
                        ThemeState.Light -> binding.themeSpinner.setSelection(THEME_VARIANT_LIGHT)
                    }
                    binding.textLoadEndpoint.setText(state.data.loadEndpoint)
                    binding.textUploadEndpoint.setText(state.data.uploadEndpoint)
                    binding.loadSpeedCheckbox.isChecked = state.data.loadCheckboxState
                    binding.uploadSpeedCheckbox.isChecked = state.data.uploadCheckboxState

                    binding.viewFlipper.displayedChild = VIEW_FLIPPER_CONTENT
                }

                is StorageResponseStates.Failure -> {
                    binding.errorText.text = state.e.message
                    binding.viewFlipper.displayedChild = VIEW_FLIPPER_ERROR
                }

                is StorageResponseStates.Loading -> {
                    binding.viewFlipper.displayedChild = VIEW_FLIPPER_LOADING
                }
            }
        }
    }

    private fun setListeners() {
        binding.themeSpinner.setOnItemSelectedListener(
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    itemSelected: View?,
                    selectedItemPosition: Int,
                    selectedId: Long
                ) {
                    if (!isSpinnerInitialized) {
                        isSpinnerInitialized = true
                        return
                    }
                    when (selectedItemPosition) {
                        THEME_VARIANT_SYSTEM -> {
                            viewModel.onThemeSelected(ThemeState.System)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                        }

                        THEME_VARIANT_DARK -> {
                            viewModel.onThemeSelected(ThemeState.Dark)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        }

                        THEME_VARIANT_LIGHT -> {
                            viewModel.onThemeSelected(ThemeState.Light)
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                    }

                }

                override fun onNothingSelected(parent: AdapterView<*>?) { }
            }
        )
        binding.textLoadEndpoint.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                binding.layoutLoadEndpoint.clearFocus()
                viewModel.onSaveLoadEndpointButtonClicked(binding.textLoadEndpoint.text.toString())
                true
            } else false
        }
        binding.textUploadEndpoint.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                binding.layoutUploadEndpoint.clearFocus()
                viewModel.onSaveUploadEndpointButtonClicked(binding.textUploadEndpoint.text.toString())
                true
            } else false
        }

        binding.loadSpeedCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onLoadCheckboxChanged(isChecked)
        }
        binding.uploadSpeedCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onUploadCheckboxChanged(isChecked)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}