package com.ponykamni.astronomy.di

import com.ponykamni.astronomy.api.di.AstronomyFeature
import com.ponykamni.astronomy.di.module.AstronomyModule
import com.ponykamni.astronomy.di.module.NetworkModule
import dagger.Component

class AstronomyFeatureImpl() : AstronomyFeature by DaggerAstronomyFeatureComponent.factory()
    .create()

@Component(
    modules = [
        AstronomyModule::class,
        NetworkModule::class
    ],
)
internal interface AstronomyFeatureComponent : AstronomyFeature {

    @Component.Factory
    interface Factory {

        fun create(
        ): AstronomyFeature
    }
}