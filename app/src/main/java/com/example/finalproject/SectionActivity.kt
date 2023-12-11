package com.example.finalproject

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class SectionActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var location: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_section)

        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val fragment : SupportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        val back: Button = findViewById(R.id.back)

        Model.initSectionInfo()
        progressBar.max = Model.getMaxSeats()
        progressBar.progress = Model.getOpenSeats()

        progressBar.setOnLongClickListener() {
            Toast.makeText(this, "${progressBar.progress} seats left!", Toast.LENGTH_SHORT).show()
            true
        }

        Model.initLocation()
        location = LatLng(Model.getLat(), Model.getLng())
        fragment.getMapAsync(this)

        back.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_right,0)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0

        val marker = MarkerOptions()
        marker.position(location)
        marker.title(Model.getName())
        map.addMarker(marker)

        val camera: CameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 16.0f)
        map.moveCamera(camera)
    }
}