package com.app.childlocationadmin

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationRequest:LocationRequest
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private  var currentMarker: Marker?=null
    private lateinit var mylocationRef:DatabaseReference
    private lateinit var dangerousArea:MutableList<LatLng>
//    private lateinit var listener: IOnLocationListener
    private lateinit var myCity:DatabaseReference
    private lateinit var lastLocation:Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_maps)


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Dexter.withActivity(this)
                .withPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        buildLocationRequest()
                        buildLocationCallback()
                        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this@MapsActivity)
                        val mapFragment = supportFragmentManager
                                .findFragmentById(R.id.map) as SupportMapFragment
                        mapFragment.getMapAsync(this@MapsActivity)
                        settingGeoFire()


                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?) {
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                        Toast.makeText(this@MapsActivity,"You Must Enable Permission Request.",Toast.LENGTH_SHORT).show()
                    }

                }).check()


    }



    private fun buildLocationCallback() {
        locationCallback= object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                if(mMap!=null)
                {
                   if(currentMarker!=null)
                   {
                    currentMarker?.remove()
                   }

                    currentMarker=mMap.addMarker(MarkerOptions()
                    .position(LatLng(p0!!.lastLocation.latitude,p0!!.lastLocation.longitude))
                     .title("current location"))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentMarker?.position,12.0f))



                }

            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun buildLocationRequest() {
    locationRequest= LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=5000
        locationRequest.fastestInterval=3000
        locationRequest.smallestDisplacement = 10f

    }

    private fun settingGeoFire()
    {
        mylocationRef=FirebaseDatabase.getInstance().getReference("Location")
//        geoFire=GeoFire(mylocationRef)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera
       if(fusedLocationProviderClient!=null)
       {
           fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())
       }
    }

    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()

    }


}
