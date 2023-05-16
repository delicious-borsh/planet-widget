package com.example.usefulwidgetapp.di

import com.example.planetwidget.di.PlanetWidgetFeature
import com.ponykamni.astronomy.api.di.AstronomyFeature
import dagger.BindsInstance
import dagger.Component


@Component(
    modules = [],
)
internal interface ApplicationComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance astronomyFeature: AstronomyFeature,
            @BindsInstance planetWidgetFeature: PlanetWidgetFeature,
        ): ApplicationComponent
    }
}