package com.example.usefulwidgetapp

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ponykamni.astronomy.api.domain.Planet
import com.ponykamni.astronomy.di.AstronomyFeatureImpl
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getDistanceFromEarthUseCase = AstronomyFeatureImpl().getGetDistanceFromEarthUseCase()

        val textView = findViewById<TextView>(R.id.main_text)

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val distance = getDistanceFromEarthUseCase(Planet.MARS)


            withContext(Dispatchers.Main) {
                textView.text = distance.toString()
            }
        }
    }
}

private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.e("MainActivity", "Something went wronge", throwable)
}