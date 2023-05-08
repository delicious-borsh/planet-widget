package com.example.usefulwidgetapp

import androidx.annotation.DrawableRes
import com.ponykamni.astronomy.api.domain.Planet

@DrawableRes
fun Planet.getIcon(): Int {
    return when (this) {
        Planet.MARS -> R.drawable.ic_mars
        Planet.VENUS -> R.drawable.ic_venus
        Planet.MERCURY -> R.drawable.ic_mercury
    }
}
