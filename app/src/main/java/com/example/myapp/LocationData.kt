package com.example.myapp

data class LocationData(
    var id: String,
    var date: String,
    var timeStar: Long,
    var latitudeStart: Double,
    var longitudeStart: Double,
    var locationRuntime: ArrayList<LocationRuntimeData>?
)