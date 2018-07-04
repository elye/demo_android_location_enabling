package com.elyeproj.locationdetection

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.IntentFilter


class MainActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_LOCATION_PERMISSION_CODE = 1000
    }

    private val locationManager = LocationManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        openFragment()
    }

    private fun openFragment() {
        locationManager.checkPermissionAndRequest(REQUEST_LOCATION_PERMISSION_CODE)
        if (!locationManager.isLocationPermissionGranted()) {
            openFragment(NoPermissionFragment.TAG, { NoPermissionFragment() })
        } else {
            locationManager.locationDetection { onResume() }
            if (!locationManager.isLocationEnabled()) {
                openFragment(NotEnabledFragment.TAG, { NotEnabledFragment() })
            } else {
                openFragment(LocationEnabledFragment.TAG, { LocationEnabledFragment() })
            }
        }
    }

    private fun openFragment(tag: String, instantiateFragment: () -> Fragment) {
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            supportFragmentManager.beginTransaction().replace(R.id.container, instantiateFragment(), tag).commit()
        }
    }

    override fun onPause() {
        super.onPause()
        locationManager.removeSwitchStateReceiver()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION_CODE -> {
                locationManager.requestLocationPermissionResult(grantResults, permissions,
                        onSuccess = {
                            // openPlaceMapFragment()
                        },
                        onReject = {
                            // openErrorFragmentPermissionRequest()
                        },
                        onDenied = {
                            // openErrorFragmentPermissionRequest()
                        })
            }
        }
    }
}

