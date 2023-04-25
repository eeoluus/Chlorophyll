package com.example.chlorophyll.domain

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.example.chlorophyll.R
import com.example.chlorophyll.viewmodels.DayEvents

fun iconFromEvents(context: Context, events: DayEvents): Drawable {

    val sizeExtra = R.drawable.ic_four_plus_grid

    val sizeToXmlTemplate = mapOf(
        1 to R.drawable.ic_one_dot,
        2 to R.drawable.ic_two_dots,
        3 to R.drawable.ic_three_grid,
        4 to R.drawable.ic_four_grid
    )
    fun colorLayers(limit: Int, events: DayEvents, template: LayerDrawable): Drawable {

        for (i in 0 until limit) {
            val layer = template.getDrawable(i)
            val colorString = events[i].color
            DrawableCompat.setTint(
                DrawableCompat.wrap(layer),
                Color.parseColor(colorString)
            )
        }
        return template
    }
    return sizeToXmlTemplate[events.size]?.let {

        val template = ContextCompat.getDrawable(context, it) as LayerDrawable
        colorLayers(template.numberOfLayers, events, template)

    } ?: (ContextCompat.getDrawable(context, sizeExtra) as LayerDrawable).also {
        colorLayers(3, events, it)
    }
}