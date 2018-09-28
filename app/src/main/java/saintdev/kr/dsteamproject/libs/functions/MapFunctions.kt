package saintdev.kr.dsteamproject.libs.functions

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

object MapFunctions {
    object Marker {
        fun createMakrer(title: String = "Test Makrer",
                         content: String = "The marker test",
                         position: LatLng = LatLng(37.56, 126.97)) : MarkerOptions {
            val marker = MarkerOptions()
            marker.title(title)
            marker.snippet(content)
            return marker.position(position)
        }
    }
}