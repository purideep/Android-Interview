package com.example.surfaces.models

import com.example.surfaces.R

data class SurfaceModel(
    val index: Int,
    val name: String,
    val ip: String
) {
    fun getImage(): Int {
        return if (index % 2 == 0) R.drawable.black else R.drawable.gray
    }
}
