package saintdev.kr.dsteamproject.views.fragments

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.location.Location
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import saintdev.kr.dsteamproject.R
import saintdev.kr.dsteamproject.libs.functions.MapFunctions
import saintdev.kr.dsteamproject.libs.gps.GPSManager
import saintdev.kr.dsteamproject.libs.gps.GPSUpdateListener
import android.graphics.drawable.Drawable
import android.graphics.Typeface



class MainMapFragment : Fragment() {
    private lateinit var rootView: View
    private lateinit var mapView: MapView
    private lateinit var gpsManager: GPSManager
    private var googleMap: GoogleMap? = null
    private lateinit var sharedPrep: SharedPreferences

    private lateinit var gpsTrackButton: FloatingActionButton
    private lateinit var lookUpButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.rootView = inflater.inflate(R.layout.fragmn_map_main, container, false)
        this.mapView = this.rootView.findViewById<View>(R.id.main_map) as MapView
        this.mapView.onCreate(savedInstanceState)
        this.mapView.onResume()
        this.mapView.getMapAsync(onMapReadyListener)

        this.gpsManager = GPSManager(context!!)
        this.sharedPrep = context!!.getSharedPreferences("DSMAD", MODE_PRIVATE)

        // find button.
        this.gpsTrackButton = this.rootView.findViewById<View>(R.id.main_track_me) as FloatingActionButton
        this.lookUpButton = this.rootView.findViewById<View>(R.id.main_look_up) as FloatingActionButton

        // add event listener
        this.gpsTrackButton.setOnClickListener(onGPSTrackClickListener)

        // check Tutorials
        if(!sharedPrep.getBoolean("isTutorialRun", false)) {
            showTutorials()
        }

        return this.rootView
    }

    override fun onStop() {
        super.onStop()
        this.gpsManager.release()
    }

    /**
     * Google map ready callback
     */
    private val onMapReadyListener = OnMapReadyCallback {
        // Map loaded. Start gps
        this.gpsManager.listen(onGPSUpdated)
        this.googleMap = it

        // 기본 위치로 카메라 이동
        it.moveCamera(CameraUpdateFactory.newLatLng(LatLng(37.56, 126.97)))
        it.animateCamera(CameraUpdateFactory.zoomTo(8F))
    }

    private var lastLocation: Location? = null
    private val onGPSUpdated = object: GPSUpdateListener {

        override fun onFirstChange(location: Location) {
            // 첫 위치 업데이트 시 카메라 줌 이동
            lastLocation = location     // 위치 정보 전역으로 승격
            moveLastLocation()
        }

        override fun onChange(location: Location) {
            lastLocation = location     // 마지막 위치를 전역으로 승격

            if(googleMap != null) {
                // TODO 최근 위치 값을 가져왔으므로 나머지 작업을 처리 한다.
                // 서버에 연결해서 근처 자전거 위치를 가져오거나, 뷁

            }
        }
    }

    private val onGPSTrackClickListener = View.OnClickListener {
        if(lastLocation == null) {
            Toast.makeText(context, "위치를 추적하고 있습니다...", Toast.LENGTH_SHORT).show()
        } else {
            moveLastLocation()
        }
    }

    /**
     * 가장 최근 위치로 카메라를 이동합니다.
     */
    private fun moveLastLocation() {
        if(lastLocation != null) {
            val latlng = LatLng(lastLocation!!.latitude, lastLocation!!.longitude)
            googleMap?.moveCamera(CameraUpdateFactory.newLatLng(latlng))
            googleMap?.animateCamera(CameraUpdateFactory.zoomTo(12F))
        }
    }

    private fun showTutorials() {
        TapTargetView.showFor(activity, // `this` is an Activity
                TapTarget.forView(this.rootView.findViewById(R.id.main_look_up), "자전거 자동 대여", "터치하면 가장 가까운 대여소의 최적의 자전거를 자동으로 선택한 후 대여 합니다.")
                        // All options below are optional
                        .outerCircleColor(R.color.colorAccent)      // Specify a color for the outer circle
                        .outerCircleAlpha(1f)            // Specify the alpha amount for the outer circle
                        .titleTextSize(24)                  // Specify the size (in sp) of the title text
                        .titleTextColor(R.color.colorWhite)      // Specify the color of the title text
                        .descriptionTextSize(16)            // Specify the size (in sp) of the description text
                        .textColor(R.color.colorWhite)            // Specify a color for both the title and description text
                        .cancelable(false)                  // Whether tapping outside the outer circle dismisses the view
                        .tintTarget(true)                   // Whether to tint the target view's color
                        .targetRadius(20) // Specify the target radius (in dp)
                        .transparentTarget(true)
                        .drawShadow(true)
                        .targetCircleColor(R.color.colorWhite),
                object : TapTargetView.Listener() {          // The listener can listen for regular clicks, long clicks or cancels
                    override fun onTargetClick(view: TapTargetView) {
                        super.onTargetClick(view)      // This call is optional

                        // 튜토리얼 완료
                        sharedPrep.edit().putBoolean("isTutorialRun", true).apply()
                    }
                })
    }
}