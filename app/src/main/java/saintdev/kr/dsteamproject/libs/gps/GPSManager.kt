package saintdev.kr.dsteamproject.libs.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat

class GPSManager(val context: Context) : LocationListener {
    private val locationMgr: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as
            LocationManager
    private var isListening = false
    private var callback: GPSUpdateListener? = null

    fun listen(callback: GPSUpdateListener) : Boolean {
        this.callback = callback
        callback.onInit()

        return if(GPSManagerFunction.checkPermission(context)) {
            locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100L, 1F, this)
            locationMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100L, 1F, this)
            this.isListening = true
            true
        } else
            false
    }

    fun release() {
        this.isListening = false
        this.callback = null
        this.locationMgr.removeUpdates(this)
    }

    private var isFristUpdate = true
    override fun onLocationChanged(location: Location) {
        if(isFristUpdate) {
            this.callback?.onFirstChange(location)
            isFristUpdate = false
        } else {
            this.callback?.onChange(location)
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}
}

object GPSManagerFunction {
    fun checkPermission(context: Context)
            = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}