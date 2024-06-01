package com.example.networkspeedo.presentation.speedo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.createViewModelLazy
import androidx.lifecycle.ViewModelProvider
import com.example.networkspeedo.R
import com.example.networkspeedo.databinding.FragmentSpeedoBinding
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject


class SpeedoFragment : Fragment() {

    companion object {
        private const val VIEW_FLIPPER_CONTENT = 0
        private const val VIEW_FLIPPER_ERROR = 1
    }

    private var _binding: FragmentSpeedoBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by createViewModelLazy(
        SpeedoViewModel::class,
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
        _binding = FragmentSpeedoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
        setStateObserver()
    }

    private fun setStateObserver() {
        viewModel.speedoState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SpeedResponseStates.Success -> {
                    if (state.data.loadMomentPercent != null) {
                        binding.loadSpeedPercentsText.setText(
                            resources.getString(
                                R.string.full_percents,
                                state.data.loadMomentPercent.toString()
                            )
                        )
                    }
                    if (state.data.loadMomentSpeed != null) {
                        binding.loadSpeedMomentText.setText(
                            resources.getString(
                                R.string.full_mbytes,
                                state.data.loadMomentSpeed.toString()
                            )
                        )
                    }
                    if (state.data.loadMeasuredSpeed != null) {
                        binding.loadSpeedMeasuredText.setText(
                            resources.getString(
                                R.string.full_mbytes,
                                state.data.loadMeasuredSpeed.toString()
                            )
                        )
                    }

                    if (state.data.uploadMomentPercent != null) {
                        binding.uploadSpeedPercentsText.setText(
                            resources.getString(
                                R.string.full_percents,
                                state.data.uploadMomentPercent.toString()
                            )
                        )
                    }
                    if (state.data.uploadMomentSpeed != null) {
                        binding.uploadSpeedMomentText.setText(
                            resources.getString(
                                R.string.full_mbytes,
                                state.data.uploadMomentSpeed.toString()
                            )
                        )
                    }
                    if (state.data.uploadMeasuredSpeed != null) {
                        binding.uploadSpeedMeasuredText.setText(
                            resources.getString(
                                R.string.full_mbytes,
                                state.data.uploadMeasuredSpeed.toString()
                            )
                        )
                    }
                    binding.viewFlipper.displayedChild = VIEW_FLIPPER_CONTENT
                }

                is SpeedResponseStates.Failure -> {
                    binding.errorText.text = state.e.message
                    binding.viewFlipper.displayedChild = VIEW_FLIPPER_ERROR
                }

                is SpeedResponseStates.Empty -> {

                    binding.loadSpeedPercentsText.setText(resources.getString(R.string.empty_percents))

                    binding.loadSpeedMomentText.setText(resources.getString(R.string.empty_mbites))

                    binding.loadSpeedMeasuredText.setText(resources.getString(R.string.empty_mbites))

                    binding.uploadSpeedPercentsText.setText(resources.getString(R.string.empty_percents))

                    binding.uploadSpeedMomentText.setText(resources.getString(R.string.empty_mbites))

                    binding.uploadSpeedMeasuredText.setText(resources.getString(R.string.empty_mbites))

                    binding.viewFlipper.displayedChild = VIEW_FLIPPER_CONTENT
                }
            }
        }
    }

    private fun setListeners() {
        binding.measureButton.setOnClickListener {
            viewModel.onMeasureClicked()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}