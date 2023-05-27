package com.example.planetwidget.di;

import android.content.Context
import com.example.planetwidget.presentation.PlanetWidget
import com.example.planetwidget.presentation.PlanetWidgetConfigurationActivity
import com.ponykamni.astronomy.api.di.AstronomyFeature
import dagger.BindsInstance
import dagger.Component


class PlanetWidgetFeatureImpl internal constructor(
    private val component: PlanetWidgetFeatureComponent,
) : PlanetWidgetFeature by component {

    constructor(
        context: Context,
        astronomyFeature: AstronomyFeature,
    ) : this(
        DaggerPlanetWidgetFeatureComponent.factory().create(
            context,
            astronomyFeature,
        ) as PlanetWidgetFeatureComponent
    )

    init {
        PlanetWidgetInjector.component = component
    }
}

@Component(
    modules = [PlanetWidgetModule::class],
)
internal interface PlanetWidgetFeatureComponent : PlanetWidgetFeature {

    fun inject(planetWidget: PlanetWidget)
    fun inject(planetWidgetConfigurationActivity: PlanetWidgetConfigurationActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
            @BindsInstance astronomyFeature: AstronomyFeature,
        ): PlanetWidgetFeature
    }
}