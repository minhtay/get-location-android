package com.example.myapp

import java.io.Serializable

data class LocationData(
    var id: String? = null,
    var date: String? = null,
    var timeStar: Long? = null,
    var latitudeStart: Double? = null,
    var longitudeStart: Double? = null,
    var locationRuntime: ArrayList<LocationRuntimeData>? = null
):Serializable