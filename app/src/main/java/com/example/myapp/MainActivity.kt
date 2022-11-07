package com.example.myapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val nav = findNavController(R.id.frameLayout    )
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.historyFragment
            )
        )
        setupActionBarWithNavController(nav, appBarConfiguration)
        binding.bnvMain.setupWithNavController(nav)
    }
}