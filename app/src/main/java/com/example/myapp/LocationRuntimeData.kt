package com.example.myapp

import java.io.Serializable

data class LocationRuntimeData(
    var id: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    val time: Long? = null
): Serializable
