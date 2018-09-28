package saintdev.kr.dsteamproject.libs.gps

import android.location.Location

interface GPSUpdateListener {
    fun onInit() { }
    fun onFirstChange(location: Location) { }
    fun onChange(location: Location)
}