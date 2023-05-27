package com.example.planetwidget.presentation

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.planetwidget.R
import com.example.planetwidget.di.PlanetWidgetInjector
import com.ponykamni.astronomy.api.domain.Planet
import javax.inject.Inject

class PlanetWidgetConfigurationActivity : AppCompatActivity() {

    @Inject
    lateinit var planetWidgetController: PlanetWidgetController

    private var chosenPlanet: Planet = Planet.VENUS

    override fun onCreate(savedInstanceState: Bundle?) {
        PlanetWidgetInjector.component?.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_planet_widget_configuration)

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(Activity.RESULT_CANCELED, resultValue)

        findViewById<Button>(R.id.btn_ok).setOnClickListener {
            planetWidgetController.updateCurrentPlanet(appWidgetId, chosenPlanet)
            planetWidgetController.onDataUpdated(this, appWidgetId)
            success(appWidgetId)
        }

        val marsButton = findViewById<ImageButton>(R.id.btn_mars)
        val venusButton = findViewById<ImageButton>(R.id.btn_venus)
        val mercuryButton = findViewById<ImageButton>(R.id.btn_mercury)

        fun chooseVenus() {
            chosenPlanet = Planet.VENUS

            marsButton.alpha = INACTIVE_ALPHA
            venusButton.alpha = ACTIVE_ALPHA
            mercuryButton.alpha = INACTIVE_ALPHA
        }

        marsButton.setOnClickListener {
            chosenPlanet = Planet.MARS

            marsButton.alpha = ACTIVE_ALPHA
            venusButton.alpha = INACTIVE_ALPHA
            mercuryButton.alpha = INACTIVE_ALPHA
        }

        venusButton.setOnClickListener {
            chooseVenus()
        }

        mercuryButton.setOnClickListener {
            chosenPlanet = Planet.MERCURY

            marsButton.alpha = INACTIVE_ALPHA
            venusButton.alpha = INACTIVE_ALPHA
            mercuryButton.alpha = ACTIVE_ALPHA
        }

        chooseVenus()
    }

    private fun success(id: Int) {
        val resultValue = Intent().putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
        setResult(Activity.RESULT_OK, resultValue)
        finish()
    }
}

private const val ACTIVE_ALPHA = 1f
private const val INACTIVE_ALPHA = 0.3f