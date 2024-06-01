package com.example.networkspeedo.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.networkspeedo.R
import com.example.networkspeedo.databinding.ActivityMainBinding
import com.example.networkspeedo.domain.ThemeState
import dagger.android.AndroidInjection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<MainViewModel>(
        factoryProducer = { viewModelFactory })

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        setupTheme()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        setupWithNavController(binding.bottomNavigationView, navController)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
    }

    private fun setupTheme() {
        lifecycleScope.launch {
            val themeState = withContext(Dispatchers.IO) {
                viewModel.getTheme()
            }
            withContext(Dispatchers.Main) {
                AppCompatDelegate.setDefaultNightMode(
                    when (themeState) {
                        ThemeState.System -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        ThemeState.Dark -> AppCompatDelegate.MODE_NIGHT_YES
                        ThemeState.Light -> AppCompatDelegate.MODE_NIGHT_NO
                    }
                )
            }
        }
    }
}